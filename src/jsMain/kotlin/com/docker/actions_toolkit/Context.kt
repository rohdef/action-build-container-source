package com.docker.actions_toolkit

import kotlin.js.Json

external class Context {
    suspend fun getInputs(): Json
}