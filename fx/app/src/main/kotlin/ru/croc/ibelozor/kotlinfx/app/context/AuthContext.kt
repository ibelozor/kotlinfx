package ru.croc.ibelozor.kotlinfx.app.context

import ru.croc.ibelozor.kotlinfx.context.Context
import ru.croc.ibelozor.kotlinfx.context.FxScopedContext
import ru.croc.ibelozor.kotlinfx.model.UserData

class AuthContext(superContext: Context, var userData: UserData? = null) : FxScopedContext(superContext)
