package ru.croc.ibelozor.samplefx.app.context

import ru.croc.ibelozor.samplefx.context.Context
import ru.croc.ibelozor.samplefx.context.FxScopedContext
import ru.croc.ibelozor.samplefx.model.UserData

class AuthContext(superContext: Context, var userData: UserData? = null) : FxScopedContext(superContext)
