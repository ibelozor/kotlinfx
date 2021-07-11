package ru.croc.ibelozor.kotlinfx.app.view

import ru.croc.ibelozor.kotlinfx.app.context.MainProcessContext
import ru.croc.ibelozor.kotlinfx.fxutils.CommonStyles
import ru.croc.ibelozor.kotlinfx.fxutils.columnRowIndex
import ru.croc.ibelozor.kotlinfx.fxutils.customGridPane
import tornadofx.*

class MainProcessView : AbstractRootContentView() {

    private val vm by inject<MainProcessViewModel>()

    override val scope
        get() = super.scope as MainProcessContext

    init {
        headerText.set("Основной процесс")
        setPersonNode(scope.mainData.user)
        rootContentNode.set(
            customGridPane(heights = listOf(1, 5, 2, 1), widths = listOf(1, 1, 1), attachToParent = false) {
                // чекбокс
                val c = checkbox {
                    columnRowIndex(0, 0, 3)
                    textProperty().bind(vm.externalDataProperty)
                }
                label(messages["mainProcess.data"]) {
                    columnRowIndex(1, 1)
                    addClass(CommonStyles.boldLabel)
                    visibleProperty().bind(c.selectedProperty())
                }
                button(messages["mainProcess.finish"]) {
                    columnRowIndex(1, 3)
                    useMaxWidth = true
                    action {
                        vm.close()
                    }
                }
            }
        )
    }
}
