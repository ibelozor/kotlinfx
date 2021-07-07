package ru.croc.ibelozor.samplefx.app.context

import ru.croc.ibelozor.samplefx.context.Context
import ru.croc.ibelozor.samplefx.context.FxScopedContext

class WarningContext(superContext: Context, val warn: String) : FxScopedContext(superContext)
