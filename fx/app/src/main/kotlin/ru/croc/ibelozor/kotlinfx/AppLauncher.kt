package ru.croc.ibelozor.kotlinfx

import ru.croc.ibelozor.kotlinfx.app.KodeinApp
import tornadofx.launch

class AppLauncher {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch<KodeinApp>(args)
        }
    }
}
