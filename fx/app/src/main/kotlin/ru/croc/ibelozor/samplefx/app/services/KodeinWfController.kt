package ru.croc.ibelozor.samplefx.app.services

import kotlinx.coroutines.delay
import org.apache.logging.log4j.LogManager
import org.kodein.di.DI
import org.kodein.di.instance
import ru.croc.ibelozor.samplefx.RootProcess
import ru.croc.ibelozor.samplefx.app.context.AuthContext
import ru.croc.ibelozor.samplefx.app.context.MainProcessContext
import ru.croc.ibelozor.samplefx.app.context.WarningContext
import ru.croc.ibelozor.samplefx.app.module.KodeinSample
import ru.croc.ibelozor.samplefx.context.Context
import ru.croc.ibelozor.samplefx.context.ContextedRootProcess
import ru.croc.ibelozor.samplefx.context.FxScopedContext
import ru.croc.ibelozor.samplefx.context.ProcessContext
import ru.croc.ibelozor.samplefx.model.MainProcessData
import ru.croc.ibelozor.samplefx.service.WfService

class KodeinWfController(di: DI) : WfService() {

    override val appContext: ProcessContext by di.instance(tag = KodeinSample.ApplicationContext)

    private val storageService by di.instance<KodeinStorageService>(tag = KodeinSample.MainStorage)

    override suspend fun launchProgramCycle(ctx: Context) {
        // создам контекст процесса авторизации
        val authCtx = AuthContext(ctx)
        // сообщаем в ui об изменении статуса
        currentProcessInternal.set(ContextedRootProcess(RootProcess.Auth, authCtx))
        authCtx.superJob.join()
        val userData = authCtx.userData
        logger.info("Процесс авторизации завершён. Пользователь: ${userData?.name}")
        if (userData == null || userData.name.isBlank()) {
            // конечно, нужно строки из ресурсов, для примера было неудобно
            WarningContext(ctx, "Пользователь не найден").apply {
                currentProcessInternal.set(ContextedRootProcess(RootProcess.Warning, this))
                superJob.join()
            }
            return
        }
        currentProcessInternal.set(null)
        // например, получаем внешние данные
        delay(1000)
        // запуск основного процесса
        MainProcessContext(ctx, MainProcessData(userData, storageService.randomData)).apply {
            currentProcessInternal.set(ContextedRootProcess(RootProcess.Main, this))
            superJob.join()
        }
        // спасибо, что воспользовались нашим сервисом
        FxScopedContext(ctx).let {
            currentProcessInternal.set(ContextedRootProcess(ThanksProcess, it))
            it.superJob.join()
        }
    }

    companion object {
        private val logger = LogManager.getLogger()
    }
}
