package ru.croc.ibelozor.kotlinfx.app.style

import tornadofx.Stylesheet
import tornadofx.c
import tornadofx.cssclass
import tornadofx.em

class CustomStyles : Stylesheet() {
    companion object {
        val hugeWhiteLabel by cssclass()
    }

    init {
        val whiteColor = c("#FFFFFF")

        hugeWhiteLabel {
            textFill = whiteColor
            fontSize = (2.5).em
        }
    }
}
