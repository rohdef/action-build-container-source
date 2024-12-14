@file:JsModule("@docker/actions-toolkit/lib/docker/docker")
@file:JsNonModule
package com.docker.actions_toolkit.lib.docker

import kotlin.js.Promise

external object Docker {
    fun printVersion(): Promise<Unit>
    fun printInfo(): Promise<Unit>
}