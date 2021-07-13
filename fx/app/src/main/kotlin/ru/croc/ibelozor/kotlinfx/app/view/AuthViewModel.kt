package ru.croc.ibelozor.kotlinfx.app.view

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.apache.logging.log4j.LogManager
import ru.croc.ibelozor.kotlinfx.app.context.AuthContext
import ru.croc.ibelozor.kotlinfx.model.UserData
import tornadofx.Controller

class AuthViewModel : Controller() {
    override val scope
        get() = super.scope as AuthContext

    val storageIsBusyProperty = SimpleBooleanProperty(false)

    private var storageRequestJob: Job? = null

    val userNameInput = SimpleStringProperty("")

    /**
     * получим пользователя из внешнего сервиса
     */
    fun getOrCancelUserFromStorage() {
        // отменю текущую работу получения пользователя
        storageRequestJob?.cancel()
        // если работа не была запущена - создам работу по получению пользователя
        storageRequestJob = if (storageRequestJob == null) {
            storageIsBusyProperty.set(true)
            scope.launch {
                val user = scope.storageService.getUser()
                setUser(user)
            }.apply {
                invokeOnCompletion {
                    if (it is CancellationException) logger.info("Отменили получение пользователя с сервера")
                }
            }
        } else {
            // работа была - значит покажем в интерфейсе, что уже перестали получать
            storageIsBusyProperty.set(false)
            // установлю null для следующей попытки
            null
        }
    }

    /**
     * задать пользователя из интерфейса
     */
    fun setUserName(name: String) = setUser(UserData(name)).also {
        logger.debug("Установили имя пользователя $name из интерфейса")
    }

    /**
     * задаёт данные пользователя в контекст и закрывает его
     */
    private fun setUser(userData: UserData) {
        scope.userData = userData
        scope.close()
    }

    companion object {
        private val logger = LogManager.getLogger()
    }
}
