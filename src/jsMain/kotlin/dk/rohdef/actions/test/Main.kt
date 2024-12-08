package dk.rohdef.actions.test

import com.github.actions.Core

@JsModule("@actions/core")
@JsNonModule
external val core: Core
//@JsModule("@actions/github")
//@JsNonModule
//external val github: dynamic

fun main() {
    console.log("Hello other")
    console.log(core)

    core.setFailed("We just fail right now")
}