package com.github.actions

import kotlin.js.Promise

external class Core {
    fun getInput(name: String): String

    fun <T> group(name: String, block: () -> Promise<T>): Promise<T>

    fun setFailed(message: String)

    fun debug(message: String)
    fun notice(message: String)
    fun info(message: String)
    fun warning(message: String)
    fun error(message: String)
}