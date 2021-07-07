package ru.croc.ibelozor.samplefx.context

import ru.croc.ibelozor.samplefx.RootProcess

/**
 * класс описывает связку корневого процесса и контекста, который был создан для этого процесса
 * @param context контекст процесса
 * @param process тип процесса
 */
data class ContextedRootProcess(
    val process: RootProcess,
    val context: FxScopedContext
)
