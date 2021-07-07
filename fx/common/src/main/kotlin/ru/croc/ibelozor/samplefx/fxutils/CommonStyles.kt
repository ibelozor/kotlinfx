package ru.croc.ibelozor.samplefx.fxutils

import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.c
import tornadofx.cssclass
import tornadofx.em
import tornadofx.mixin
import tornadofx.px

class CommonStyles : Stylesheet() {
    companion object {
        /**
         * класс для блока заголовка
         */
        val rootHeader by cssclass()

        /**
         * класс для блока основного контента
         */
        val rootContent by cssclass()

        /**
         * класс для отображения прямоугольника с закруглёнными краями (все процессы в таком)
         */
        val roundedCornerLightBox by cssclass()

        /**
         * класс больших лейблов
         */
        val boldLabel by cssclass()

        /**
         * сообщение об ошибке
         */
        val boldErrorLabel by cssclass()
    }

    init {
        // малый радиус скругления для границ панели
        val smallBorderRadiusBox = box(10.px)
        // Большой радиус скругления для границ панели
        val bigBorderRadius = 35.px
        // Большой радиус скругления для границ панели
        val bigBorderRadiusBox = box(bigBorderRadius)

        // основной цвет текста
        val commonTextColor = c("#470D46")
        // светлый цвет
        val lightColor = c("#FFFF0F0E")
        // красный цвет
        val errorColor = c("#E52D2D")

        label {
            fontFamily = "Open Sans"
            textAlignment = TextAlignment.CENTER
        }

        text {
            fontFamily = "Open Sans"
            textAlignment = TextAlignment.CENTER
        }

        button {
            backgroundColor += lightColor
            backgroundRadius += bigBorderRadiusBox // окгругление углов элемента
            textFill = commonTextColor
            fontWeight = FontWeight.BOLD // жирный текст
            borderWidth += box(2.px) // ширина рамки
            borderColor += box(c("#306460")) // цвет рамки
            borderRadius += bigBorderRadiusBox // окгругление рамки
            and(pressed) {
                backgroundColor += commonTextColor
                textFill = lightColor
                borderColor += box(commonTextColor)
            }
        }

        rootHeader {
            backgroundColor += c("#32bbb0") // цвет фона
        }

        rootContent {
            backgroundColor += c("#ebdee5") // цвет фона
        }

        roundedCornerLightBox {
            backgroundColor += c("#F1F9FF")
            borderRadius += smallBorderRadiusBox
            backgroundRadius += smallBorderRadiusBox
        }

        val commonLabel = mixin {
            textFill = commonTextColor // для label
            fill = commonTextColor // для textflow
        }

        boldLabel {
            +commonLabel
            fontWeight = FontWeight.BOLD
            fontSize = 1.5.em
        }

        val commonErrorLabel = mixin {
            +commonLabel
            textFill = errorColor
            fill = errorColor
        }

        boldErrorLabel {
            +commonErrorLabel
            fontWeight = FontWeight.BOLD
            fontSize = 1.5.em
        }
    }
}
