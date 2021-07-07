package ru.croc.ibelozor.samplefx.service

import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import ru.croc.ibelozor.samplefx.context.Context
import ru.croc.ibelozor.samplefx.context.ContextedRootProcess
import ru.croc.ibelozor.samplefx.context.ProcessContext
import ru.croc.medkiosk.utils.ObservableInScope
import ru.croc.medkiosk.utils.SimpleObservableInScope

/**
 * сервис смены состояний приложения
 */
abstract class WfService {
    /**
     * Текущий процесс (для наследников)
     */
    protected val currentProcessInternal = SimpleObservableInScope<ContextedRootProcess?>(null)

    /**
     * текущий процесс
     */
    val currentProcess: ObservableInScope<ContextedRootProcess?>
        get() = currentProcessInternal

    /**
     * контекст приложения
     */
    abstract val appContext: ProcessContext

    /**
     * запуск работ
     */
    open fun startWork() {
        restartCycle()
    }

    /**
     * перезапустить цикл программы (т.е. смена состояний авторизация -> основной процесс -> спасибо за участие и т.д.)
     */
    private fun restartCycle() {
        // при завершении приложения завершается appContext (а вместе с ним и cycleContext), запускается метод restartExamCycle,
        // поэтому необходимо проверить, а не завершён ли сейчас контекст приложения
        if (!appContext.isActive) return
        logger.info("Запущен новый цикл смены состояний")
        // создаём контекст для процесса полного цикла приложения
        val newCycleCtx = ProcessContext(appContext).also {
            it.superJob.invokeOnCompletion {
                logger.info("Завершён цикл смены состояний")
                // по завершению - запускаем снова
                restartCycle()
            }
        }
        newCycleCtx.launch {
            launchProgramCycle(newCycleCtx)
            // завершаем процесс цикла приложения
            newCycleCtx.close()
        }
    }

    abstract suspend fun launchProgramCycle(ctx: Context)

    companion object {
        private val logger = LogManager.getLogger()
    }
}
