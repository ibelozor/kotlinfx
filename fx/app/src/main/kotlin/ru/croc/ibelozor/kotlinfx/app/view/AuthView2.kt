package ru.croc.ibelozor.kotlinfx.app.view

import javafx.scene.layout.VBox
import tornadofx.action
import tornadofx.button
import tornadofx.textfield

class AuthView2 : AbstractRootContentView() {

    private val vm by inject<AuthViewModel>()

    init {
        rootContentNode.set(
            VBox().apply {
                textfield { textProperty().bindBidirectional(vm.userNameInput) }
                button("Сменить вью") {
                    action {
                        replaceWith(find<AuthView>())
                    }
                }
            }
        )
    }
}
