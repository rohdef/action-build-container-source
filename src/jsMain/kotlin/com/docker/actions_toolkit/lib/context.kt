@file:JsModule("@docker/actions-toolkit/lib/context")
@file:JsNonModule
package com.docker.actions_toolkit.lib.context

import kotlin.js.Json
import kotlin.js.Promise

external class Context {
    fun getInputs(): Promise<Json>
}