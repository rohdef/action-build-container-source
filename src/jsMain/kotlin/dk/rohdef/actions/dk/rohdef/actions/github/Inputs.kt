package dk.rohdef.actions.dk.rohdef.actions.github

data class Inputs(
    val annotations: Annotations,
    val buildArgs: BuildArgs,
    val dockerfilePath: DockerfilePath,
    val labels: Labels,
) {
    value class Annotations private constructor(
        val value: Map<String, String>,
    ) {
        companion object {
            fun fromValue(value: String): Annotations {
                val inputList = value.toInputList()

                if (!inputList.all { it.contains("=") }) {
                    throw IllegalArgumentException("All annotations must be key-value-pairs separated by '=', a value was found that does not follow this")
                }

                val annotations = inputList
                    .map { it.split("=") }
                    .associate { it[0].trim() to it[1] }

                return Annotations(annotations)
            }
        }
    }

    value class BuildArgs private constructor(
        val value: Map<String, String>,
    ) {
        companion object {
            fun fromValue(value: String): BuildArgs {
                val inputList = value.toInputList()

                if (!inputList.all { it.contains("=") }) {
                    throw IllegalArgumentException("All build args must be key-value-pairs separated by '=', a value was found that does not follow this")
                }

                val buildArgs = inputList
                    .map { it.split("=") }
                    .associate { it[0].trim() to it[1] }

                return BuildArgs(buildArgs)
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
        val value: Map<String, String>,
    ) {
        companion object {
            fun fromValue(value: String): Labels {
                val inputList = value.toInputList()

                if (!inputList.all { it.contains("=") }) {
                    throw IllegalArgumentException("All labels must be key-value-pairs separated by '=', a value was found that does not follow this")
                }

                val labels = inputList
                    .map { it.split("=") }
                    .associate { it[0].trim() to it[1] }

                return Labels(labels)
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
                BuildArgs.fromValue(getInput(InputNames.buildArgs)),
                DockerfilePath.fromValue(getInput(InputNames.dockerFile)),
                Labels.fromValue(getInput(InputNames.labels)),
            )
        }
    }
}

object InputNames {
    val annotations = "annotations"
    val dockerFile = "dockerfilePath"
    val labels = "labels"
    val buildArgs = "buildArgs"
}