package ru.croc.ibelozor.samplefx.app.context

import ru.croc.ibelozor.samplefx.context.Context
import ru.croc.ibelozor.samplefx.context.FxScopedContext
import ru.croc.ibelozor.samplefx.model.MainProcessData

class MainProcessContext(superContext: Context, val mainData: MainProcessData) : FxScopedContext(superContext)
