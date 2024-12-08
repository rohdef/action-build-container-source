package dk.rohdef.actions.test

import com.docker.actions_toolkit.ActionsToolkit
import com.docker.actions_toolkit.Context
import com.github.actions.Core
import kotlin.js.Date

@JsModule("@actions/core")
@JsNonModule
external val core: Core

@JsModule("@docker/actions-toolkit")
@JsNonModule
external val actionsToolkit: ActionsToolkit

@JsModule("@docker/actions-toolkit/lib/context")
@JsNonModule
external val context: Context

@JsModule("@docker/actions-toolkit/lib/docker/docker")
@JsNonModule
external val docker: dynamic

@JsModule("@docker/actions-toolkit/lib/github")
@JsNonModule
external val github: dynamic


suspend fun main() {
    actionsToolkit.run {
        val startedTime = Date()

    //        val inputs = context.getInputs()
    //        core.info("We got inputs: $inputs")

        core.group( "GitHub Actions runtime token ACs") {
            try {
                github.GitHub.printActionsRuntimeTokenACs()
            } catch (exception: Exception) {
                core.warning(exception.message)
            }
        }

        core.group("Docker info") {
            try {
                docker.Docker.printVersion()
                docker.Docker   .printInfo()
            } catch (exception: Exception) {
                core.info(exception.message)
            }
        }

        core.info("Ok, so we are running, let's see what we can do")
    }
    core.info("Seems to be doing something")

    core.setFailed("We just fail right now")
}