package ru.croc.ibelozor.kotlinfx.app.services

import kotlinx.coroutines.Job
import ru.croc.ibelozor.kotlinfx.context.Context
import ru.croc.ibelozor.kotlinfx.context.ProcessContext
import ru.croc.ibelozor.kotlinfx.service.WfService

/**
 * пустой workflow-контроллер
 */
class EmptyWfController : WfService() {
    override val appContext = ProcessContext(Job(), KodeinStorageService(StorageParams()))

    override suspend fun launchProgramCycle(ctx: Context) {
        appContext.storageService.shutdown()
        appContext.close()
    }
}
