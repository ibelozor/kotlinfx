package ru.croc.ibelozor.kotlinfx.service

import ru.croc.ibelozor.kotlinfx.model.UserData

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
