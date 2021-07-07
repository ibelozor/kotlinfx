package ru.croc.ibelozor.samplefx.app.services

import kotlinx.coroutines.Job
import ru.croc.ibelozor.samplefx.context.Context
import ru.croc.ibelozor.samplefx.context.ProcessContext
import ru.croc.ibelozor.samplefx.service.WfService

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
