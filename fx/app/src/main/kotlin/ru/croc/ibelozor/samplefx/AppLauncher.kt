package ru.croc.ibelozor.samplefx

import ru.croc.ibelozor.samplefx.app.KodeinApp
import tornadofx.launch

class AppLauncher {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch<KodeinApp>(args)
        }
    }
}
