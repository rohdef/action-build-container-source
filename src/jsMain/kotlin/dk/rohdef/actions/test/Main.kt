package dk.rohdef.actions.test

import com.github.actions.Core

@JsModule("@actions/core")
@JsNonModule
external val core: Core

@JsModule("@docker/actions-toolkit")
@JsNonModule
external val actionsToolkit: dynamic


fun main() {
    actionsToolkit.run {
        core.error("Testing this thing")
    }
    core.info("Seems to be doing something")
    println("We're hopefully happy"     )

    core.setFailed("We just fail right now")
}