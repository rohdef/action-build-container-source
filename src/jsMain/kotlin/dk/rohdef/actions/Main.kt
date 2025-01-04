package dk.rohdef.actions

import com.docker.actions_toolkit.lib.docker.Docker
import com.docker.actions_toolkit.lib.github.GitHub
import com.docker.actions_toolkit.lib.toolkit.Toolkit
import dk.rohdef.actions.dk.rohdef.actions.github.Core
import kotlinx.coroutines.await
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

@OptIn(ExperimentalUuidApi::class)
suspend fun main() {
    Core().run(
        {
            val startedTime = Date()

            actionInfo()

                val imageName = Uuid.random()
            listOf("--tag", imageName.toString())
            inputs.labels.value.map { "--label" }.zip(inputs.labels.value)


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

            setFailed("We just fail right now")
        },
    )
}