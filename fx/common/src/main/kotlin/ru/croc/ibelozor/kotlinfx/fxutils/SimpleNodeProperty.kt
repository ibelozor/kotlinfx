package ru.croc.ibelozor.kotlinfx.fxutils

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import javafx.scene.layout.GridPane

/**
 * Проперти, которое можно забиндить в единственную ячейку таблицы (см. [GridPane.bindSingleChild])
 */
class SimpleNodeProperty : SimpleObjectProperty<Node?>()
