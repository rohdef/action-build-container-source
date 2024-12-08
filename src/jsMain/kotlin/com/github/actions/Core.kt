package com.github.actions

import kotlin.js.Json

external class Core {
    fun getInput(name: String): Any
    fun getInputs(): Json

    fun group(name: String, contents: suspend () -> Unit)

    fun setFailed(message: String): Any

    fun debug(message: String): Any
    fun notice(message: String): Any
    fun info(message: String): Any
    fun warning(message: String): Any
    fun error(message: String): Any
}