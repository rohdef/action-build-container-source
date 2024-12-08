package com.github.actions

external class Core {
    fun getInput(name: String): Any

    fun setFailed(message: String): Any
}