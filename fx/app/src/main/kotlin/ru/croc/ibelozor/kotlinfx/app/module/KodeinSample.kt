package ru.croc.ibelozor.kotlinfx.app.module

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.multiton
import org.kodein.di.on
import org.kodein.di.provider
import org.kodein.di.singleton
import ru.croc.ibelozor.kotlinfx.Core
import ru.croc.ibelozor.kotlinfx.RootProcess
import ru.croc.ibelozor.kotlinfx.app.services.KodeinStorageService
import ru.croc.ibelozor.kotlinfx.app.services.KodeinWfController
import ru.croc.ibelozor.kotlinfx.app.services.StorageParams
import ru.croc.ibelozor.kotlinfx.app.view.MainProcessView
import ru.croc.ibelozor.kotlinfx.app.view.RootViewModel
import ru.croc.ibelozor.kotlinfx.context.ProcessContext
import ru.croc.ibelozor.kotlinfx.service.StorageService
import ru.croc.ibelozor.kotlinfx.service.WfService
import tornadofx.FX
import tornadofx.find

object KodeinSample {
    object ApplicationContext

    object MainStorage

    private val defaultStorageParams = StorageParams()

    val module = DI.Module("kodeinSample", allowSilentOverride = true) {
        importOnce(Empty.module)
        // запомним с тегом ApplicationContext основной контекст приложения
        bind<ProcessContext>(tag = ApplicationContext) with singleton {
            ProcessContext(Core.rootJob, instance(tag = MainStorage)).apply {
                superJob.invokeOnCompletion {
                    storageService.shutdown()
                }
            }
        }

        bind<StorageService>(tag = MainStorage) with provider { instance<KodeinStorageService>(tag = MainStorage) }
        // например, можем определить RwStorage, RoStorage
        bind<KodeinStorageService>(tag = MainStorage) with provider {
            // пример получения инстанса мультитона
            factory<StorageParams, KodeinStorageService>().invoke(defaultStorageParams)
        }
        // пример мультитона
        bind<KodeinStorageService>() with multiton { params: StorageParams ->
            KodeinStorageService(params)
        }
        // переопределяю WfService, который был определён в default-модуле
        bind<WfService>() with singleton { KodeinWfController(di) }

        onReady {
            // укажу view для main-процесса
            Core.rootViewFactories[RootProcess.Main] = { directDI.on(it).instance<MainProcessView>().root }
            // подпишу rootView на изменения в wf
            val rootVM = find<RootViewModel>(FX.defaultScope)
            val wfService = instance<WfService>()
            wfService.currentProcess.addListener(wfService.appContext, rootVM.currentProcessProperty::set)
        }
    }
}
