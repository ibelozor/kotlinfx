package ru.croc.ibelozor.kotlinfx.app.services

import java.time.Duration

data class StorageParams(
    val userPrefix: String = "kodein",
    val timeout: Duration = Duration.ofSeconds(5)
)
