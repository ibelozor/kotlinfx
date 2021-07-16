package ru.croc.ibelozor.kotlinfx.utils

import kotlinx.coroutines.CoroutineScope

typealias NewValueListener<T> = (newValue: T) -> Unit

/**
 * Интерфейс observable-значения, подписаться на который можно внутри скоупа корутин. Подписчик автоматически отпишется при
 * завершении работы скоупа. Можно отписаться вручную методом [removeListener] передав в качестве параметра объект обработчика.
 * При выполнении подписки первое выполнение метода подписчика выполняется сразу же, в него передаётся текущее значение
 * [value]
 *
 * В случае добавления подписки в момент, когда скоуп неактивен, ничего не делается (т.е. подписчик не добавится).
 *
 * При этом, если при подписке был указан скоуп корутин, то обработка изменения значения происходит в нём (асинхронно
 * методом scope.launch { }
 *
 * Метод обработки значения (подписчик) вызывается сразу же после подписки, для текущего значения
 */
interface ObservableInScope<T> {
    /**
     * Значение
     */
    val value: T

    /**
     * Значение
     */
    fun get(): T

    /**
     * Добавить блокирующего подписчика, который будет обрабатывать изменение значения в потоке, в котором изменение
     * было вызвано
     *
     * **Кажется, не очень рекомендую использовать этот метод**
     */
    fun addBlockingListener(listener: NewValueListener<T>)

    /**
     * Добавить лиснера значения (если уже был подписан, предыдущая подписка удаляется). Метод обработки выполняется сразу
     * после подписки - для текущего значения
     * @param scope скоуп
     * @param listener лиснер
     */
    fun addListener(scope: CoroutineScope, listener: NewValueListener<T>)
    /**
     * Добавить подписчика с возможностью трансформации значения. Это позволяет задавать в качестве подписчика какой-то
     * setter. Например:
     * ```
     * state.addListener(this@DeviceService, serviceBrokenInternal::set) {
     *      deviceMap.values.any { it.state.value == State.DeviceLostOrBroken }
     * }
     * ```
     * @param listener подписчик
     * @param scope скоуп
     * @param transform метод трансформации значения перед вызовом обработчика
     */
    fun <R> addListener(scope: CoroutineScope, listener: NewValueListener<R>, transform: (T) -> R)

    /**
     * Метод добавления подписчика для возможности вызова suspend-функций внутри
     * @param scope скоуп
     * @param listener подписчик
     * @return ключ, по которому можно отписаться методом [removeListener]
     */
    fun addSuspendListener(scope: CoroutineScope, listener: suspend (T) -> Unit): NewValueListener<T>

    /**
     * Метод [listener] выполнится однократно при выполнении условия [condition]. В случае необходимости можно остановить
     * подписку завершив [scope], или вручную методом [removeListener], передав результат выполнения метода. Если
     * текущее значение удовлетворяет условию, метод выполнится и подписка на изменения отменится. Метод проверки значения
     * [condition] будет выполнен синхронно при изменении значения. Если текущее значение [value] удовлетворяет условию,
     * блок выполнится сразу
     * @param scope скоуп
     * @param condition условие, при выполнении которого выполнится метод [listener] в потоке [scope] и очистится
     * подписка на изменения
     * @param listener подписчик
     * @return ключ, по которому можно отписаться методом [removeListener]
     */
    fun execOnceOnCondition(scope: CoroutineScope, condition: (T) -> Boolean, listener: suspend (T) -> Unit): NewValueListener<T>

    /**
     * Отписаться от изменения значения
     * @param listener лиснер, который был ранее подписан
     */
    fun removeListener(listener: NewValueListener<*>)

    /**
     * Очистить всех подписчиков
     */
    fun clearListeners()

    /**
     * Возвращает новый объект биндабл-значения (просто создаётся ещё один объект, который подписывается на текущий
     * методом [addBlockingListener]. Понятно, что при вызове [clearListeners] на родительском объекте этот подписчик
     * очистится вместе с остальными
     */
    fun <R> map(transform: (T) -> R): ObservableInScope<R>
}
