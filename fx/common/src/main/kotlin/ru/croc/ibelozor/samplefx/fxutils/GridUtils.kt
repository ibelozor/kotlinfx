package ru.croc.ibelozor.samplefx.fxutils

import javafx.event.EventTarget
import javafx.geometry.HPos
import javafx.geometry.VPos
import javafx.scene.Node
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import tornadofx.add
import tornadofx.clear
import tornadofx.constraintsForColumn
import tornadofx.constraintsForRow
import tornadofx.onChange
import tornadofx.opcr
import tornadofx.times

/**
 * Создает [GridPane] с заданным числом колонок и строк.
 *
 * @param widths список широт для каждого стоблца. значения нормализуются к сумме в 100%
 * @param heights список высот для каждой строки. значения нормализуются к сумме в 100%
 * @param hAlign горизонтальная ориентация по умолчанию
 * @param vAlign вертикальная ориентация по умолчанию
 * @param growPriority параметры роста по умолчанию для строк и столбцов. по умолчанию [Priority.SOMETIMES]
 * @param hGrowPriority параметры роста по умолчанию для столбцов. Если задана, то переопределяет [growPriority]
 * @param vGrowPriority параметры роста по умолчанию для строк. Если задана, то переопределяет [growPriority]
 * @param attachToParent прикрепить к парент-узлу. Нужно установить в [false] если нужно только создать и запомнить узел
 * @param op операция над созданным [GridPane]
 * @return [GridPane]
 */
fun EventTarget.customGridPane(
    widths: List<Number> = listOf(1),
    heights: List<Number> = listOf(1),
    hAlign: HPos? = HPos.CENTER,
    vAlign: VPos? = VPos.CENTER,
    growPriority: Priority = Priority.SOMETIMES,
    hGrowPriority: Priority? = null,
    vGrowPriority: Priority? = null,
    attachToParent: Boolean = true,
    op: GridPane.() -> Unit = { }
) = GridPane().apply {
    val heightsDouble = heights.map { it.toDouble() }
    val widthsDouble = widths.map { it.toDouble() }
    val hSum = heightsDouble.sum()
    if (hSum > 0.01) {
        val hFactor = 100.0 / hSum
        // Добавляем строчки в GridPane
        heightsDouble.forEachIndexed { i, h ->
            addRow(i) // Добавляем строчку
            if (heightsDouble.size > 1) constraintsForRow(i).percentHeight = h * hFactor // Выставляем высоту строчки
            constraintsForRow(i).vgrow = vGrowPriority ?: growPriority // Выставляем область расположения (макс)
            // вертикальная ориентация
            vAlign?.let { constraintsForRow(i).valignment = it }
        }
    }
    val wSum = widthsDouble.sum()
    if (wSum > 0.01) {
        val wFactor = 100.0 / wSum
        // Добавляем столбцы в GridPane
        widthsDouble.forEachIndexed { i, w ->
            addColumn(i) // Добавляем столбец
            if (widthsDouble.size > 1) constraintsForColumn(i).percentWidth = w * wFactor // Выставляем ширину столбца
            constraintsForColumn(i).hgrow = hGrowPriority ?: growPriority // Выставляем область расположения (макс)
            hAlign?.let { constraintsForColumn(i).halignment = it }
        }
    }
}.let {
    if (attachToParent) {
        opcr(this, it, op)
    } else {
        it.apply(op)
    }
}

/**
 * Установить в качестве единственной ячейки таблицы заданный узел
 */
fun GridPane.bindSingleChild(nodeProperty: SimpleNodeProperty) {
    // добавляем [ChangeListener]
    nodeProperty.onChange { setSingleChild(it) }
    // выполняем для текущего значения
    setSingleChild(nodeProperty.value)
}

/**
 * Установить узел в нулевую ячейку таблицы
 */
fun GridPane.setSingleChild(node: Node?) {
    children.clear()
    if (node == null) return
    children.add(node.columnRowIndex(0, 0))
}

/**
 * Задаёт индекс колонки и строки для узла в Grid, внутри которого он находится
 *
 * Решил не выносить сюда ещё всякие hAlignment, hGrow, fillHeight и т.п., пусть они
 * остаются в [Node#gridpaneConstraints]
 * @param columnIndex индекс колонки
 * @param rowIndex индекс строки
 * @param columnSpan число колонок, которые нужно объеденить в этой ячейке. Если не задано(null), то и не задаётся
 * @param rowSpan число строк, которые нужно объеденить в этой ячейке. Если не задано(null), то и не задаётся
 */
fun <T : Node> T.columnRowIndex(columnIndex: Int = 0, rowIndex: Int = 0, columnSpan: Int? = null, rowSpan: Int? = null): T {
    GridPane.setColumnIndex(this, columnIndex)
    GridPane.setRowIndex(this, rowIndex)
    columnSpan?.let { GridPane.setColumnSpan(this, it) }
    rowSpan?.let { GridPane.setRowSpan(this, it) }
    return this
}
