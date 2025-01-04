package dk.rohdef.actions.dk.rohdef.actions.github

data class Inputs(
    val annotations: Annotations,
    val dockerfilePath: DockerfilePath,
    val labels: Labels,
    val secrets: Secrets,
) {
    value class Annotations private constructor(
        val value: List<String>,
    ) {
        companion object {
            fun fromValue(value: String): Annotations {
                return Annotations(value.toInputList())
            }
        }
    }

    value class DockerfilePath private constructor(
        val value: String,
    ) {
        companion object {
            fun fromValue(value: String) : DockerfilePath {
                return DockerfilePath(value)
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
                Annotations.fromValue(getInput(InputNames.annotations)),
                DockerfilePath.fromValue(getInput(InputNames.dockerFile)),
                Labels.fromValue(getInput(InputNames.labels)),
                Secrets.fromValue(getInput(InputNames.secrets)),
            )
        }
    }
}

object InputNames {
    val annotations = "annotations"
    val dockerFile = "dockerfile-path"
    val labels = "labels"
    val secrets = "secrets"
}