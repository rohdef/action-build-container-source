package dk.rohdef.actions

import com.docker.actions_toolkit.lib.docker.Docker
import com.docker.actions_toolkit.lib.github.GitHub
import dk.rohdef.actions.dk.rohdef.actions.github.Core
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
            setOutput("imageid", "action: ${imageName}")

            val actionEnvironment = ActionEnvironment(process)

            val date = Date().toISOString()
            val defaultAnnotation = mapOf(
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
            val annotations = defaultAnnotation + inputs.annotations.value

            val annotationBuildCommand = annotations.map { "    --annotation ${it.key}=${it.value} \\" }.joinToString("\n")
            val buildArgsCommand = inputs.buildArgs.value.map { "    --build-arg ${it.key}=${it.value} \\" }.joinToString("\n")

            val commandRaw = """
                |docker build \
                |$annotationBuildCommand
                |$buildArgsCommand
                |    --tag $imageName \
                |    ${inputs.dockerfilePath.value}
            """.trimMargin( )
            val command = commandRaw.lines().filter { it.isNotBlank() }.joinToString("\n")

            info("Running the following docker build command:\n$command")

//            setFailed("We just fail right now")
        },
    )
}