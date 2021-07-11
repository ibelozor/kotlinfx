package ru.croc.ibelozor.kotlinfx.fxutils

import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Region
import javafx.scene.shape.Shape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore

/**
 * Класс реализации картинки, которая заполняет доступное её пространство (ширина/высота привязываются к 97% высоты/ширины
 * парент-узла). Реализация основана на том, что при изменении ширины/высоты, картинка фитится к высоте в 5 пикселей и
 * запускается корутина, которая через 200мс привязывает фит картинки к параметрам парент-узла
 * @param img картинка
 * @param imgObservable обсервабл-картинка
 * @param scope скоуп для корутины, которая изменит размер через 100мс
 * @param preserveRatio сохранить соотношения картинок
 * @param smooth параметр [ImageView.smooth]
 * @param sceneFill 0.0 - 1.0 заполнение парент узла, по умолчанию 0.97
 * @param clipCreator метод по созданию клипа заданной высоты ширины (создаётся после ресайза картинки)
 */
class AutoFittedImageView(
    img: Image? = null,
    private val imgObservable: ObservableValue<Image?>? = null,
    private val scope: CoroutineScope,
    preserveRatio: Boolean = true,
    smooth: Boolean = true,
    var sceneFill: Double = 0.97,
    private val clipCreator: ((w: Double, h: Double) -> Shape)? = null
) : ImageView() {
    /**
     * Запомненный узел, к которому прикрепляется картинка
     */
    private var parentNode: Region? = null

    /**
     * признак того, что ожидание изменения размера активно
     */
    private val resizeAwait = Semaphore(1)

    /**
     * Время последнего сброса размеров изображения
     */
    private var clearTs = System.currentTimeMillis()

    /**
     * при изменении размера парента сбрасываем значения, а через 100мс устанавливаем из парента
     */
    private val parentSizeListener = ChangeListener<Number> { _, oldSize, newSize ->
        // не реагируем на изменение размера в 1-2 пикселя (1 пиксель иногда "прыгает" при округлении double)
        if (kotlin.math.abs((oldSize?.toDouble() ?: 0.0) - (newSize?.toDouble() ?: 0.0)) > 2.0) scheduleResize()
    }

    private fun scheduleResize() {
        clearValues()
        if (resizeAwait.tryAcquire()) scope.launch {
            while (System.currentTimeMillis() - clearTs < 200) {
                delay(200)
            }
            // может уже ушли со сцены
            scene?.let { _ ->
                parentNode?.let {
                    fitHeight = it.height * sceneFill
                    fitWidth = it.width * sceneFill
                    // привязываем заданный клип (если же был привязан imgObservable, то надо на каждую смену картинки)
                    if (imgObservable == null) createClip()
                }
            }
        }.invokeOnCompletion {
            if (resizeAwait.availablePermits == 0) resizeAwait.release()
        }
    }

    /**
     * при изменении размера сцены - сбрасываем значения. потом произойдут изменения размера парента
     */
    private val sceneSizeListener = InvalidationListener {
        scheduleResize()
    }

    private val imgChangeListener = ChangeListener<Image?> { _, _, newImg ->
        // при каждом изменении картинки изменяем клип, т.к. размеры изменились
        if (newImg != null && scene != null) {
            image = newImg
            createClip()
        }
    }
    init {
        isPreserveRatio = preserveRatio
        isSmooth = smooth
        // устанавливаем картинку
        if (img != null) {
            image = img
        } else imgObservable?.addListener(imgChangeListener)
        // по началу прячем картинку
        clearValues()
        // подписываемся на прикрепление к сцене
        sceneProperty().addListener { _, oldScene, newScene ->
            // отписываемся от предыдущей сцены
            oldScene?.widthProperty()?.removeListener(sceneSizeListener)
            oldScene?.heightProperty()?.removeListener(sceneSizeListener)
            // открепились от сцены
            if (newScene == null) {
                imgObservable?.removeListener(imgChangeListener)
                // забываем парента
                parentNode?.widthProperty()?.removeListener(parentSizeListener)
                parentNode?.heightProperty()?.removeListener(parentSizeListener)
                parentNode = null
            } else {
                // находим парента
                parentNode = parent as? Region?
                // подписываемся на изменение размеров
                newScene.widthProperty().addListener(sceneSizeListener)
                newScene.heightProperty().addListener(sceneSizeListener)
                parentNode?.let {
                    it.widthProperty().addListener(parentSizeListener)
                    it.heightProperty().addListener(parentSizeListener)
                }
            }
        }
    }

    private fun createClip() {
        layoutBounds?.let { bounds ->
            clip = clipCreator?.let { it(bounds.width, bounds.height) }
        }
    }

    /**
     * сброс
     */
    private fun clearValues() {
        clip = null
        val fh = fitHeight
        if (fh > 6 || fh < 4) fitHeight = 5.0
        clearTs = System.currentTimeMillis()
        // fitWidth = 5.0
    }
}
