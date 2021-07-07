package ru.croc.ibelozor.samplefx.service

import ru.croc.ibelozor.samplefx.model.UserData

/**
 * интерфейс сервиса работы с данными
 */
interface StorageService {
    /**
     * получить пользователя
     */
    suspend fun getUser(): UserData

    /**
     * закрыть подключение
     */
    fun shutdown() {
    }
}
