package ru.croc.ibelozor.samplefx.app

import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.stage.Stage
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import org.kodein.di.on
import org.kodein.di.tornadofx.installTornadoSource
import ru.croc.ibelozor.samplefx.Core
import ru.croc.ibelozor.samplefx.RootProcess
import ru.croc.ibelozor.samplefx.app.module.Empty
import ru.croc.ibelozor.samplefx.app.module.KodeinSample
import ru.croc.ibelozor.samplefx.app.style.CustomStyles
import ru.croc.ibelozor.samplefx.app.view.AuthView
import ru.croc.ibelozor.samplefx.app.view.RootView
import ru.croc.ibelozor.samplefx.app.view.WarningView
import ru.croc.ibelozor.samplefx.fxutils.CommonStyles
import ru.croc.ibelozor.samplefx.fxutils.ProgressView
import ru.croc.ibelozor.samplefx.service.WfService
import tornadofx.App
import tornadofx.FX
import tornadofx.find

class KodeinApp : App(RootView::class, CommonStyles::class, CustomStyles::class), DIAware {

    override fun start(stage: Stage) {
        stage.width = 640.toDouble()
        stage.height = 480.toDouble()
        FX.layoutDebuggerShortcut = KeyCodeCombination(KeyCode.getKeyCode("J"), KeyCombination.ALT_DOWN)
        //  stage.initStyle(StageStyle.UNDECORATED)
        // stage.isMaximized = true
        // stage.isFullScreen = true

        super.start(stage)
    }

    override fun stop() {
        // завершу все корутины
        Core.rootJob.cancel()
        super.stop()
    }

    override val di = DI {
        // связываем кодеин с торнадо (библиотека kodein-di-framework-tornadofx-jvm):
        installTornadoSource()
        importOnce(Empty.module, allowOverride = true)
        // можем переопределить биндинги, созданные "по умолчанию". Т.е. если закомментировать эту строку, то будет
        // запускаться пустое приложение:
        importOnce(KodeinSample.module, allowOverride = true)
        onReady {
            // по умолчанию крутилка
            Core.rootViewFactories[null] = { directDI.on(it).instance<ProgressView>(it).root }
            Core.rootViewFactories[RootProcess.Warning] = { find<WarningView>(it).root }
            Core.rootViewFactories[RootProcess.Auth] = { find<AuthView>(it).root }

            instance<WfService>().startWork()
        }
    }
}
