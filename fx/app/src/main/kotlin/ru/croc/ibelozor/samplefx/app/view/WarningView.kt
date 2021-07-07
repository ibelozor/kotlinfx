package ru.croc.ibelozor.samplefx.app.view

import javafx.scene.text.TextAlignment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.croc.ibelozor.samplefx.app.context.WarningContext
import ru.croc.ibelozor.samplefx.fxutils.CommonStyles
import ru.croc.ibelozor.samplefx.fxutils.columnRowIndex
import ru.croc.ibelozor.samplefx.fxutils.customGridPane
import tornadofx.Fragment
import tornadofx.addClass
import tornadofx.text
import tornadofx.textflow

/**
 * Вьюшка отображения какой-то ошибки
 */
class WarningView : Fragment() {

    override val scope
        get() = super.scope as WarningContext

    override val root = customGridPane(widths = listOf(1, 2, 1), heights = listOf(1, 2, 1)) {
        addClass(CommonStyles.rootContent)
        textflow {
            columnRowIndex(1, 1)
            text(scope.warn).addClass(CommonStyles.boldErrorLabel)
            textAlignment = TextAlignment.CENTER
        }
    }

    init {
        scope.launch {
            // паузу нужно брать из конфига
            delay(3000)
            scope.close()
        }
    }
}
