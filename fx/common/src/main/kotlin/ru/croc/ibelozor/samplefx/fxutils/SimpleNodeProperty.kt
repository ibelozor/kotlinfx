package ru.croc.ibelozor.samplefx.fxutils

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import javafx.scene.layout.GridPane

/**
 * Проперти, которое можно забиндить в единственную ячейку таблицы (см. [GridPane.bindSingleChild])
 */
class SimpleNodeProperty : SimpleObjectProperty<Node?>()
