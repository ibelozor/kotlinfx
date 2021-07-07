package ru.croc.ibelozor.samplefx.context

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.SupervisorJob
import ru.croc.ibelozor.samplefx.service.StorageService

/**
 * Контекст любого процесса
 * @param storageService ссылка на сервис хранения данных
 * @param superJob [SupervisorJob], к которому привязывается корутинный контекст
 */
class ProcessContext(
    override val superJob: CompletableJob,
    override val storageService: StorageService
) : Context {
    /**
     * Конструктор подчинённого процесса, процесса в процессе
     * @param superContext родительский процесс
     */
    constructor(superContext: Context) : this(SupervisorJob(superContext.superJob), superContext.storageService)
}
