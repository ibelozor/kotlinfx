package ru.croc.ibelozor.kotlinfx.app.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.apache.logging.log4j.LogManager
import ru.croc.ibelozor.kotlinfx.model.UserData
import ru.croc.ibelozor.kotlinfx.service.StorageService
import ru.croc.medkiosk.utils.SimpleObservableInScope
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class KodeinStorageService(private val params: StorageParams) : StorageService {

    override suspend fun getUser(): UserData = withContext(Dispatchers.IO) {
        logger.debug("Запустили ожидание внешнего сервиса")
        delay(params.timeout.toMillis())
        UserData("${params.userPrefix}User ${Random.nextInt(1000)}").also {
            logger.debug("Получили из внешнего сервиса пользователя ${it.name}")
        }
    }

    /**
     * случайные данные, допустим берутся из какого-то внешнего сервиса
     * в storageService'е выглядит странно, но для примера норм
     */
    val randomData = SimpleObservableInScope(0.0)

    /**
     * экзекьютор для получения данных из внешнего потока
     */
    private val executor = Executors.newSingleThreadScheduledExecutor()

    init {
        // запустим получение данных
        executor.scheduleAtFixedRate({ randomData.set(Random.nextDouble(100.0)) }, 100, 200, TimeUnit.MILLISECONDS)
    }

    override fun shutdown() {
        super.shutdown()
        executor.shutdown()
    }

    companion object {
        private val logger = LogManager.getLogger()
    }
}
