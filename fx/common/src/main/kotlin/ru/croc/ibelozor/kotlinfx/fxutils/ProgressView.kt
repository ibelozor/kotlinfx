package ru.croc.ibelozor.kotlinfx.fxutils

import tornadofx.Fragment
import tornadofx.addClass
import tornadofx.progressindicator

/**
 * Фрагмент с крутилкой прогресса
 */
class ProgressView() : Fragment() {

    override val root = customGridPane {
        addClass(CommonStyles.rootContent)
        progressindicator {
            isPickOnBounds = false
            maxHeightProperty().bind(this@customGridPane.widthProperty().multiply(0.25))
            maxWidthProperty().bind(this@customGridPane.heightProperty().multiply(0.25))
        }
    }
}
