package com.example.kahoot.presentation

import com.example.kahoot.Main
import com.example.kahoot.domain.FxInteractor
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import kotlin.system.exitProcess

abstract class BaseController<T> : DataReceiver<T> {
    protected lateinit var interactor: FxInteractor
    protected lateinit var stage: Stage

    enum class Scenes(val fxml: String) {
        MAIN("hello-view.fxml"),
        CREATE("kahoot-sizable.fxml"),
        LIST("kahoot-list.fxml"),
        LOBBY("kahoot-lobby.fxml"),
        STATISTIC("kahoot-statistic.fxml"),
        QUESTION_PREPARE("kahoot-question-prepare.fxml"),
        FINISH("kahoot-finish.fxml");
    }

    private lateinit var fxmlLoader: FXMLLoader

    open fun usualClose(): Boolean = true

    open fun onClose() {}

    fun nextScene(address: Scenes) {
        fxmlLoader = FXMLLoader(Main::class.java.getResource(address.fxml))
        val scene = Scene(fxmlLoader.load())
        stage.title = "Kahoot!"
        stage.scene = scene
        val controller = fxmlLoader.getController<BaseController<Any>>()
        controller.interactor = this.interactor
        controller.stage = this.stage
        if (!stage.isMaximized)
            stage.isMaximized = true
        scene.window.height = stage.height
        scene.window.width = stage.width
        controller.init()
    }

    fun nextScene(address: Scenes, data: Any?) {
        nextScene(address)
        if (data != null) {
            val controller: DataReceiver<Any> = fxmlLoader.getController()
            controller.getData(data)
        }
    }

    override fun getData(data: T) {
        println("${this.javaClass.simpleName} can't receive ${data.toString()}")
    }

    fun changeTitle(title: String) {
        stage.title = title
    }

    fun init(interactor: FxInteractor, stage: Stage) {
        this.interactor = interactor
        this.stage = stage
        init()
    }

    open fun init() {
        this.stage.onCloseRequest = EventHandler {
            if (usualClose()) {
                interactor.close()
                Platform.exit()
                exitProcess(0)
            } else {
                it.consume()
                onClose()
            }
        }
    }
}