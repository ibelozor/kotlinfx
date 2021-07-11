package ru.croc.ibelozor.kotlinfx.app.view

import javafx.beans.property.SimpleObjectProperty
import ru.croc.ibelozor.kotlinfx.context.ContextedRootProcess
import tornadofx.Controller

/**
 * Корневая модель приложения. Содержит только текущий отображаемый процесс
 */
class RootViewModel : Controller() {
    /**
     * Текущий отображаемый процесс
     */
    var currentProcessProperty = SimpleObjectProperty<ContextedRootProcess?>()

    init {
        /**
         * подпишусь на изменение текущего процесса
         */
        // перенёс в TornadoWfController для реализации kodeinSample
    }
}
