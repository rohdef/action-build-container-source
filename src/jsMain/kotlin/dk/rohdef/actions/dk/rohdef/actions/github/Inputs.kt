package dk.rohdef.actions.dk.rohdef.actions.github

import com.docker.actions_toolkit.lib.context.Context as ToolkitContext

data class Inputs(
    val addHosts: AddHosts,
    val annotations: Annotations,
    val context: Context,
    val dockerFile: DockerFile,
    val labels: Labels,
    val secrets: Secrets,
) {
    value class AddHosts private constructor(
        val value: List<String>,
    ) {
        companion object {
            fun fromValue(value: String): AddHosts {
                return AddHosts(value.toInputList())
            }
        }
    }

    value class Annotations private constructor(
        val value: List<String>,
    ) {
        companion object {
            fun fromValue(value: String): Annotations {
                return Annotations(value.toInputList())
            }
        }
    }

    value class Context private constructor(
        /**
         * Defaults to the git context if blank
         */
        val value: String,
    ) {
        companion object {
            fun fromValue(value: String) : Context {
                return Context(value.ifBlank { ToolkitContext.gitContext() })

            }
        }
    }

    value class DockerFile private constructor(
        val value: String,
    ) {
        companion object {
            fun fromValue(value: String) : DockerFile {
                return DockerFile(value)
            }
        }
    }

    value class Labels private constructor(
        val value: List<String>,
    ) {
        companion object {
            fun fromValue(value: String): Labels {
                return Labels(value.toInputList())
            }
        }
    }

    value class Secrets private constructor(
        val value: Map<String, String>,
    ) {
        override fun toString(): String {
            return value.keys.toString()
        }

        companion object {
            fun fromValue(value: String): Secrets {
                val inputList = value
                    .toInputList()

                if (!inputList.all { it.contains("=") }) {
                    throw IllegalArgumentException("All secrets must be key-value-pairs separated by '=', a value was found that does not follow this")
                }

                val secrets = inputList
                    .map { it.split("=") }
                    .associate { it[0].trim() to it[1] }

                return Secrets(secrets)
            }
        }
    }

    companion object {
        fun String.toInputList(): List<String> {
            return lines()
                .map { it.trim() }
                .filter { it.isNotBlank() }
        }

        fun fromInput(getInput: (String) -> String): Inputs {
            return Inputs(
                AddHosts.fromValue(getInput(InputNames.addHosts)),
                Annotations.fromValue(getInput(InputNames.annotations)),
                Context.fromValue(getInput(InputNames.context)),
                DockerFile.fromValue(getInput(InputNames.dockerFile)),
                Labels.fromValue(getInput(InputNames.labels)),
                Secrets.fromValue(getInput(InputNames.secrets)),
            )
        }
    }
}

object InputNames {
    val addHosts = "add-hosts"
    val annotations = "annotations"
    val context = "context"
    val dockerFile = "docker-file"
    val labels = "labels"
    val secrets = "secrets"
}