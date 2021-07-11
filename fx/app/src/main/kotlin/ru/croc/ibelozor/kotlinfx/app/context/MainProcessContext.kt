package ru.croc.ibelozor.kotlinfx.app.context

import ru.croc.ibelozor.kotlinfx.context.Context
import ru.croc.ibelozor.kotlinfx.context.FxScopedContext
import ru.croc.ibelozor.kotlinfx.model.MainProcessData

class MainProcessContext(superContext: Context, val mainData: MainProcessData) : FxScopedContext(superContext)
