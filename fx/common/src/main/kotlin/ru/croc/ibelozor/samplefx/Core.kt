package ru.croc.ibelozor.samplefx

import javafx.scene.Parent
import kotlinx.coroutines.SupervisorJob
import tornadofx.Scope

/**
 * Объект основных настроек приложения
 */
object Core {
    /**
     * Корневая работа, к которой привязываются все остальные корутинные контексты и работы
     */
    val rootJob = SupervisorJob()

    /**
     * фабрики view, отвечающих за отображение корневых процессов
     */
    val rootViewFactories = mutableMapOf<RootProcess?, (Scope) -> Parent>()
}
