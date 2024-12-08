package com.docker.actions_toolkit

external class ActionsToolkit {
    suspend fun run(block: suspend () -> Unit)
}