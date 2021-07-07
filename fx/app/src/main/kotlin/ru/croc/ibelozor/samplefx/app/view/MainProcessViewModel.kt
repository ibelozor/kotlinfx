package ru.croc.ibelozor.samplefx.app.view

import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.instanceOrNull
import org.kodein.di.tornadofx.kodeinDI
import ru.croc.ibelozor.samplefx.app.context.MainProcessContext
import ru.croc.ibelozor.samplefx.app.module.KodeinSample
import ru.croc.ibelozor.samplefx.app.services.KodeinStorageService
import tornadofx.Controller
import tornadofx.get
import java.util.Locale
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class MainProcessViewModel : Controller() {

    override val scope
        get() = super.scope as MainProcessContext

    /**
     * можем получить di
     */
    private val di = kodeinDI()

    private val scheduledService by di.instanceOrNull<ScheduledExecutorService>()

    /**
     * какие-то внешние данные
     */
    val externalDataProperty = SimpleStringProperty("")

    init {
        // можем подписаться
        val listener = scope.mainData.randomModelData.addSuspendListener(scope) {
            // при изменении данных например читаем файл
            withContext(Dispatchers.IO) { delay(100) }
            // а потом изменим значение в интерфейсе
            externalDataProperty.value = formatExternalData(it)
        }
        // можем отписаться
        scope.mainData.randomModelData.removeListener(listener)
        // или подпишемся на то же самое, но через eager-инстанс kodein:
        di.direct.instance<KodeinStorageService>(tag = KodeinSample.MainStorage).randomData
            .addListener(scope, externalDataProperty::set, this::formatExternalData)
        // если определён биндинг, запустим работу по расписанию
        scheduledService?.schedule(this::close, 10, TimeUnit.SECONDS)
    }

    private fun formatExternalData(data: Double) =
        "${messages["mainProcess.externalData"]}: %.${2}f".format(Locale.US, data)

    /**
     * завершить работу
     */
    fun close() {
        scope.close()
    }
}
