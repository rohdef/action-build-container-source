package dk.rohdef.actions

import com.docker.actions_toolkit.lib.docker.Docker
import com.docker.actions_toolkit.lib.github.GitHub
import dk.rohdef.actions.dk.rohdef.actions.github.Core
import kotlinx.coroutines.await
import node.process.process
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

@OptIn(ExperimentalUuidApi::class, ExperimentalJsCollectionsApi::class)
suspend fun main() {
    Core().run(
        {
//            actionInfo()

            val imageName = Uuid.random()
            setOutput("imageid", "action: ${imageName}")
//            listOf("--tag", imageName.toString())

            // TODO rohdef - find a way to wrap the map
            val environment = process.env
            val runId = environment["GITHUB_RUN_ID"] ?: throw IllegalArgumentException("GITHUB_RUN_ID env variable is not set")
            val labels = mapOf("runnumber" to runId) + inputs.labels.value
            info("lbls: $labels")
            info("Running $runId")


            // getArgs
//            val context = inputs.context.value
            // [

//            "build",
//            inputs.addHosts.value.map { "--add-host" }.zip(inputs.addHosts.value),
//            inputs.annotations.value.map { "--annotation" }.zip(inputs.annotations.value)
//            inputs.secrets.value.map { "--secret" }.zip(inputs.secrets.value.map { "id=${it.key},env=${it.value}" })
//            inputs.dockerFile.value.let {
//                if (it.isNotBlank()) {
//                    listOf("--file", it)
//                } else {
//                    emptyList()
//                }
//            }
//            inputs.labels.value.map { "--label" }.zip(inputs.labels.value)
//              ...buildArgs,

            //   ...common,
            //   context,
            // ]

//            setFailed("We just fail right now")
        },
    )
}