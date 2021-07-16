package ru.croc.ibelozor.kotlinfx.model

import ru.croc.ibelozor.kotlinfx.utils.SimpleObservableInScope

/**
 * данные основного процесса
 * @param randomModelData данные из внешней системы, которые поступают асинхронна
 * @param user текущий пользователь
 */
data class MainProcessData(
    val user: UserData,
    val randomModelData: SimpleObservableInScope<Double>
)
