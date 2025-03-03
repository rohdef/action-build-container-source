package dk.rohdef.actions

import com.docker.actions_toolkit.lib.docker.Docker
import com.docker.actions_toolkit.lib.github.GitHub
import com.github.actions.Exec
import dk.rohdef.actions.github.Core
import dk.rohdef.actions.github.OutputName
import kotlinx.coroutines.await
import node.process.Process
import node.process.process
import kotlin.js.Date
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
}

class ActionEnvironment(
    val process: Process,
) {
    fun Process.getEnvironmentValue(key: String): String =
        process.env[key] ?: throw IllegalArgumentException("$key environment variable is not set")

    val ACTOR = "GITHUB_ACTOR"
    val COMMIT_SHA = "GITHUB_SHA"
    val REPOSITORY = "GITHUB_REPOSITORY"
    val RUN_ID = "GITHUB_RUN_ID"
    val SERVER_URL = "GITHUB_SERVER_URL"

    val actor = process.getEnvironmentValue(ACTOR)
    val commitSha = process.getEnvironmentValue(COMMIT_SHA)
    val repository = process.getEnvironmentValue(REPOSITORY)
    val runId = process.getEnvironmentValue(RUN_ID)
    val serverUrl = process.getEnvironmentValue(SERVER_URL)

    val projectUrl = "$serverUrl/$repository"
}

@OptIn(ExperimentalUuidApi::class)
suspend fun main() {
    Core().run(
        {
            actionInfo()

            val imageName = Uuid.random()
            setOutput(OutputName.IMAGE_ID, "${imageName}")

            val actionEnvironment = ActionEnvironment(process)

            val date = Date ().toISOString()
            val defaultAnnotations = mapOf(
                "dk.rohdef.actions.runnumber" to actionEnvironment.runId,
                "dk.rohdef.actions.builder" to "rohdef build container action",
                "org.opencontainers.image.created" to date,
                "org.opencontainers.image.authors" to actionEnvironment.actor,
                "org.opencontainers.image.url" to actionEnvironment.projectUrl,
                "org.opencontainers.image.documentation" to actionEnvironment.projectUrl,
                "org.opencontainers.image.source" to actionEnvironment.projectUrl,
                "org.opencontainers.image.version" to "not specified",
                "org.opencontainers.image.revision" to actionEnvironment.commitSha,
                "org.opencontainers.image.vendor" to actionEnvironment.actor,
                "org.opencontainers.image.licenses" to "not specified",
                "org.opencontainers.image.ref.name" to "not specified",
                "org.opencontainers.image.title" to "not specified",
                "org.opencontainers.image.description" to "not specified",
            )
            val annotations = inputs.annotations.value + defaultAnnotations

            val annotationParameters = annotations.entries.fold(emptyList<String>()) { accumulator, entry ->
                accumulator + listOf("--annotation", "${entry.key}=${entry.value}")
            }
            val buildArgsParameters = inputs.buildArgs.value.entries.fold(emptyList<String>()) { accumulator, entry ->
                accumulator + listOf("--build-arg", "${entry.key}=${entry.value}")
            }

            val parameters = listOf("build", "-t", "${imageName}")  + annotationParameters + buildArgsParameters + listOf(inputs.dockerfilePath.value)

            val dockerBuildOutput = Exec.getExecOutput("docker", parameters.toTypedArray()).await()

            when (dockerBuildOutput.exitCode) {
                0 -> info("Successfully built docker container")
                else -> setFailed("Could not build docker container")
            }
        },
    )
}