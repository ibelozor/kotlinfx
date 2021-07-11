package ru.croc.ibelozor.kotlinfx.app.view

import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import ru.croc.ibelozor.kotlinfx.Core
import ru.croc.ibelozor.kotlinfx.fxutils.SimpleNodeProperty
import ru.croc.ibelozor.kotlinfx.fxutils.bindSingleChild
import ru.croc.ibelozor.kotlinfx.fxutils.customGridPane
import tornadofx.*

/**
 * Корневое View - содержит контейнер для отображения текущего процесса ([BorderPane#centerProperty])
 */
class RootView() : View() {
    /**
     * корневой отображаемый узел
     */
    private val rootNode = SimpleNodeProperty()

    /**
     * в корне - просто одноклеточный грид
     */
    override val root = customGridPane {
        id = "Корень rootView"
        // привязываем узел к 0й ячейке таблицы
        bindSingleChild(rootNode)
    }

    /**
     * вью-модель содержит текущий отображаемый процесс
     */
    private val vm: RootViewModel by inject()

    init {
        title = messages["root-view-title"]
        // связываю высоту шрифта с размерами окна
        root.sceneProperty().onChange { newScene ->
            if (newScene == null) {
                root.styleProperty().unbind()
            } else {
                root.styleProperty().bind(
                    newScene.heightProperty().objectBinding(newScene.widthProperty()) {
                        val k = if (newScene.width < newScene.height) newScene.width / newScene.height else 1.0
                        val size = 0.03 * (it?.toDouble() ?: 0.0) * k
                        "-fx-font-size: $size;"
                    }
                )
            }
        }
        // view текущего процесса:
        rootNode.bind(
            vm.currentProcessProperty.objectBinding {
                val context = it?.context
                // заодно повещу на кнопку Esc прекращение процесса
                if (context != null) {
                    primaryStage.scene.onKeyReleased = EventHandler {
                        if (it.code == KeyCode.ESCAPE) context.close()
                    }
                }
                Core.rootViewFactories[it?.process]?.invoke(context ?: FX.defaultScope)
                    ?: Label(messages["rootView.unknownState"])
            }
        )
    }
}
