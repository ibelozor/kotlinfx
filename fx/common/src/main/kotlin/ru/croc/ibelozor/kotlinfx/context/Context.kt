package ru.croc.ibelozor.kotlinfx.context

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.croc.ibelozor.kotlinfx.service.StorageService

/**
 * Контекст работы
 */
interface Context : CoroutineScope {
    /**
     * родительский job, используемый в [coroutineContext] (рекомендую создавать [SupervisorJob])
     */
    val superJob: CompletableJob

    override val coroutineContext
        get() = Dispatchers.Main + superJob

    /**
     * сервис работы с данными
     */
    val storageService: StorageService

    /**
     * закрыть контекст
     */
    fun close() {
        superJob.cancel()
    }
}
