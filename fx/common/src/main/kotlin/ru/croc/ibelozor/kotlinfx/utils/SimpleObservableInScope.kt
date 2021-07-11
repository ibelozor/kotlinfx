package ru.croc.medkiosk.utils

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Observable-класс, к значению которого можно биндить свойства. В основном, нужна для биндинга SimpleProperty к объектам,
 * которые ничего не знают о javaFx, но полезно везде
 * @param initialValue значение
 */
class SimpleObservableInScope<T>(initialValue: T) : ObservableInScope<T> {
    /**
     * коллекция слушателей, подписавшихся на изменение события. В ключе - сам подписчик, в значении - структура некой
     * имплементации подписчика с джобой по его отписке
     */
    private val listeners = ConcurrentHashMap<NewValueListener<*>, ListenerWithUnbindJob<T>>()

    override var value: T = initialValue // by Delegates.observable(initialValue) { _, _, newValue ->
        private set(value) {
            if (field == value) return
            field = value
            // выполняем onChange для лиснеров
            listeners.values.forEach {
                it.listener(value)
            }
        }

    override fun get() = value

    fun set(newVal: T) {
        value = newVal
    }

    override fun addBlockingListener(listener: NewValueListener<T>) =
        createInternalListener(listener, null, listener)

    override fun addListener(scope: CoroutineScope, listener: NewValueListener<T>) =
        createInternalListener(listener, scope) {
            scope.launch {
                listener(it)
            }
        }

    override fun <R> addListener(scope: CoroutineScope, listener: NewValueListener<R>, transform: (T) -> R) =
        createInternalListener(listener, scope) {
            scope.launch {
                listener(transform(it))
            }
        }

    override fun addSuspendListener(scope: CoroutineScope, listener: suspend (T) -> Unit): NewValueListener<T> {
        val inScopeListener: NewValueListener<T> = {
            scope.launch { listener(it) }
        }
        createInternalListener(inScopeListener, scope, inScopeListener)
        return inScopeListener
    }

    override fun execOnceOnCondition(
        scope: CoroutineScope,
        condition: (T) -> Boolean,
        listener: suspend (T) -> Unit
    ): NewValueListener<T> {
        // использую объект, чтобы можно было отписаться (в анонимном классе использовать this)
        val changeListener = object : NewValueListener<T> {
            override fun invoke(newValue: T) {
                // если условие выполнилось
                if (condition(newValue)) {
                    // отпишемся
                    removeListener(this)
                    // запустимся
                    scope.launch {
                        listener(newValue)
                    }
                }
            }
        }
        createInternalListener(changeListener, scope, changeListener)
        return changeListener
    }

    override fun removeListener(listener: NewValueListener<*>) {
        // по идее, при отмене задачи - выполнится invokeOnCompletion, который ещё раз вызовет этот метод, но уже isActive
        // будет равно false
        listeners[listener]?.unBindJob?.takeIf { !it.isCompleted }?.apply {
            cancel()
            return
        }
        listeners.remove(listener)
    }

    override fun clearListeners() {
        // очищаем мапу
        listeners.clear()
    }

    override fun <R> map(transform: (T) -> R): ObservableInScope<R> =
        SimpleObservableInScope(transform(get())).also { result ->
            addBlockingListener { result.set(transform(it)) }
        }

    /**
     * Создать объект подписчика с джобой для его отписки
     * @param mapKey ключ для мапы подписчиков [listeners]
     * @param scope скоуп, к завершению которого привязываем прекращение подписки
     * @param listener собственно, подписчик. если был задан [scope], то обработка нового значения произойдёт внутри этого скоупа
     */
    private fun createInternalListener(
        mapKey: NewValueListener<*>,
        scope: CoroutineScope? = null,
        listener: NewValueListener<T>
    ) {
        // ничего не делаем, если скоуп уже неактивен
        if (scope?.isActive == false) return
        // на всякий случай отменим джобу. Если подписчик уже был подписан, то он тут же отпишется (cancel вызывает немедленный
        // invokeOnCompletion, в котором удаляется лиснер
        listeners[mapKey]?.unBindJob?.cancel()
        // создам работу, которая не будет выполняться. Но она отменится когда отменится скоуп, соответственно вызовется onCompletion
        val unBindJob: Job? = scope?.launch(start = CoroutineStart.LAZY) { }
        // при завершении работы - отпишемся
        unBindJob?.invokeOnCompletion { removeListener(mapKey) }
        // добавляю подписчика в мапу
        listeners[mapKey] = ListenerWithUnbindJob(unBindJob, listener)
        // выполним метод для текущего значения
        listener(value)
    }

    /**
     * объект подписчика с job, по завершению/отмене которого должна отменяться подписка
     */
    private data class ListenerWithUnbindJob<T>(val unBindJob: Job?, val listener: NewValueListener<T>)

    override fun toString(): String {
        return value.toString()
    }
}
