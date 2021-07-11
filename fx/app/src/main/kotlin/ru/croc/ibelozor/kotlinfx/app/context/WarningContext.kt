package ru.croc.ibelozor.kotlinfx.app.context

import ru.croc.ibelozor.kotlinfx.context.Context
import ru.croc.ibelozor.kotlinfx.context.FxScopedContext

class WarningContext(superContext: Context, val warn: String) : FxScopedContext(superContext)
