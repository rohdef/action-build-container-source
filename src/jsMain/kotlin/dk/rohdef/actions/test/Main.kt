package dk.rohdef.actions.test

@JsModule("@actions/core")
@JsNonModule
external val core: dynamic
@JsModule("@actions/github")
@JsNonModule
external val github: dynamic

fun main() {
    console.log("Hello other")
    console.log(core)

    core.setFailure("We just fail right now")
}