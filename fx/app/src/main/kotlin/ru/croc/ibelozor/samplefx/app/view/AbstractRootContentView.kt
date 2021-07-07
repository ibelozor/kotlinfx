package ru.croc.ibelozor.samplefx.app.view

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.HPos
import javafx.scene.control.Label
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.croc.ibelozor.samplefx.app.style.CustomStyles
import ru.croc.ibelozor.samplefx.context.FxScopedContext
import ru.croc.ibelozor.samplefx.fxutils.CommonStyles
import ru.croc.ibelozor.samplefx.fxutils.SimpleNodeProperty
import ru.croc.ibelozor.samplefx.fxutils.bindSingleChild
import ru.croc.ibelozor.samplefx.fxutils.columnRowIndex
import ru.croc.ibelozor.samplefx.fxutils.customGridPane
import ru.croc.ibelozor.samplefx.model.UserData
import tornadofx.Fragment
import tornadofx.addClass
import tornadofx.borderpane
import tornadofx.center
import tornadofx.get
import tornadofx.gridpaneConstraints
import tornadofx.label
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Абстрактный класс для отображения информативной части приложения.
 *
 * Сверху располагается полоса с текущим временем и информацией о пользователе (можно устанавливать в узел [personNode])
 *
 * В основной части экрана расположен узел с контентом ([rootContentNode])
 */
abstract class AbstractRootContentView : Fragment() {
    /**
     * контекст fx
     */
    override val scope
        get() = super.scope as FxScopedContext

    /**
     * нужен, чтоб в scope в init использовать без ворнинга идеи
     */
    private val coroutineScope
        get() = scope

    /**
     * Узел для отображения основного контента приложения
     */
    protected val rootContentNode = SimpleNodeProperty()

    /**
     * Узел для отображения информации о пользователе
     */
    protected val personNode = SimpleNodeProperty()

    /**
     * текст заголовка
     */
    protected val headerText = SimpleStringProperty(messages["rootContentView.header"])

    /**
     * текст лейбла с временем
     */
    private lateinit var timeLbl: Label

    final override val root = customGridPane(
        heights = listOf(
            1, // 0.используется информацией о пользователе
            6 // 1.используется основным контентом
        )
    ) {
        id = "Корень абстрактной view (содержит заголовок 10% и контент 90%)"
        customGridPane(
            widths = listOf(
                1, // 0.Отступ
                50, // 1.пользователь
                30, // 2.Время (позиция: справа)
                1 // 3.Отступ
            ),
            heights = listOf(
                1, // 0.отступ
                15, // 1.котент
                1 // 2.оступ
            )
        ) { // хедер
            id = "Таблица, для размещения заголовка (пользователь и таймер)"
            // нулевая строка в [root]
            columnRowIndex(0, 0)
            addClass(CommonStyles.rootHeader) // добавляем стиль
            // информация о пользователе
            customGridPane(hAlign = HPos.LEFT)
                // первая колонка в хедере
                .columnRowIndex(1, 1)
                .bindSingleChild(personNode)
            // текущее время
            timeLbl = label()
                // вторая колонка в хедере
                .columnRowIndex(2, 1)
                .gridpaneConstraints {
                    hAlignment = HPos.RIGHT
                }
                .addClass(CommonStyles.boldLabel) // добавляем стиль
        }
        // напишу заголовок
        borderpane {
            id = "заголовок вьюшки"
            columnRowIndex(0, 0)
            center {
                label(headerText).addClass(CustomStyles.hugeWhiteLabel)
            }
        }
        // нижняя строка - контент
        customGridPane(widths = listOf(1, 9, 1), heights = listOf(1, 10, 1)) {
            id = "Таблица с отступами по краям, внутри контент"
            // первая строка в [root]
            columnRowIndex(0, 1)
            addClass(CommonStyles.rootContent)
            // добавлю ещё одну табличку
            customGridPane {
                id = "Контентная таблица"
                columnRowIndex(1, 1)
                addClass(CommonStyles.roundedCornerLightBox)
                bindSingleChild(rootContentNode)
            }
        }
    }

    init {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss'\n'dd.MM.yyyy")
        // меняем текст времени раз в секунду
        coroutineScope.launch {
            while (isActive) {
                timeLbl.text = ZonedDateTime.now().format(dateTimeFormatter)
                delay(Duration.ofSeconds(1).toMillis())
            }
        }
    }

    /**
     * Задать значение для узла пациента
     */
    protected open fun setPersonNode(person: UserData) {
        personNode.set(Label("Пользователь:\n" + person.name).addClass(CommonStyles.boldLabel))
    }
}
