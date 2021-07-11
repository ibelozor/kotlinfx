package ru.croc.ibelozor.kotlinfx.context

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.SupervisorJob
import ru.croc.ibelozor.kotlinfx.service.StorageService
import tornadofx.Scope

/**
 * контекст работ в скоупе tornadoFx
 * @param superContext родительский контекст
 */
open class FxScopedContext(
    superContext: Context
) : Scope(), Context {
    override val storageService: StorageService = superContext.storageService
    final override val superJob: CompletableJob = SupervisorJob(superContext.superJob)
    init {
        // при завершении контекста дерегистрируем скоуп торнадо
        superJob.invokeOnCompletion {
            deregister()
        }
    }
}
