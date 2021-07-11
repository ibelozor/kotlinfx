package ru.croc.ibelozor.kotlinfx.app.module

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import ru.croc.ibelozor.kotlinfx.app.services.EmptyWfController
import ru.croc.ibelozor.kotlinfx.service.WfService

object Empty {
    val module = DI.Module("empty", allowSilentOverride = true) {
        bind<WfService>() with singleton { EmptyWfController() }
    }
}
