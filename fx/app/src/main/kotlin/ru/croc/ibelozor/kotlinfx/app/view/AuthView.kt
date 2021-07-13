package ru.croc.ibelozor.kotlinfx.app.view

import javafx.scene.input.KeyCode
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import ru.croc.ibelozor.kotlinfx.fxutils.AutoFittedImageView
import tornadofx.action
import tornadofx.button
import tornadofx.get
import tornadofx.gridpane
import tornadofx.label
import tornadofx.objectBinding
import tornadofx.textfield
import tornadofx.vgrow

open class AuthView : AbstractRootContentView() {

    private val vm by inject<AuthViewModel>()

    init {
        rootContentNode.set(
            VBox(10.0).apply {
                label(messages["authView.enterName"])
                textfield {
                    textProperty().bindBidirectional(vm.userNameInput)
                    // авторизуем по имени:
                    setOnKeyPressed {
                        if (it.code != KeyCode.ENTER) return@setOnKeyPressed else it.consume()
                        vm.setUserName(text)
                    }
                }
                button {
                    // меняем текст на кнопке в процессе получения данных
                    textProperty().bind(
                        vm.storageIsBusyProperty.objectBinding {
                            if (it == true) messages["authView.cancel"] else messages["authView.storage"]
                        }
                    )
                    action { vm.getOrCancelUserFromStorage() }
                }
                gridpane {
                    vgrow = Priority.ALWAYS
                    add(
                        AutoFittedImageView(
                            img = resources.image("/img/Screenshot from 2021-04-14 08-27-47.png"),
                            scope = scope
                        )
                    )
                }
            }
        )
    }
}
