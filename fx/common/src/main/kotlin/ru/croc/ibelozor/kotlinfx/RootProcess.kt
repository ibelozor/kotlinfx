package ru.croc.ibelozor.kotlinfx

/**
 * Процесс отображения какого-либо процесса
 */
abstract class RootProcess {
    /**
     * Процесс отображения процесса авторизации
     */
    object Auth : RootProcess()

    /**
     * процесс отображения какой-либо ошибки
     */
    object Warning : RootProcess()

    /**
     * Какой-то экшен
     */
    object Main : RootProcess()
}
