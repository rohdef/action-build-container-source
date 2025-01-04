package dk.rohdef.actions

import com.docker.actions_toolkit.lib.docker.Docker
import com.docker.actions_toolkit.lib.github.GitHub
import com.docker.actions_toolkit.lib.toolkit.Toolkit
import dk.rohdef.actions.dk.rohdef.actions.github.Core
import kotlinx.coroutines.await
import kotlin.js.Date

val toolkit = Toolkit()
suspend fun Core.actionInfo() {
    group("GitHub Actions runtime token ACs") {
        try {
            GitHub.printActionsRuntimeTokenACs().await()
        } catch (exception: Exception) {
            warning(exception.message ?: "Could not print token ACs")
        }
    }

    group("Docker info") {
        try {
            Docker.printVersion().await()
            Docker.printInfo().await()
        } catch (exception: Exception) {
            info(exception.message ?: "Could not get docker information")
        }
    }

    group("Buildx version") {
        toolkit.buildx.printVersion().await()
    }
}

suspend fun main() {
    Core().run(
        {
            val startedTime = Date()

            val buildxAvailable = toolkit.buildx.isAvailable().await()
            if (!buildxAvailable) {
                throw Exception("Docker buildx not found, cannot proceed")
            }

            actionInfo()

            setFailed("We just fail right now")
        },
    )
}