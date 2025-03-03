package dk.rohdef.actions.github

enum class InputName(val actionName: String) {
    ANNOTATIONS("annotations"),
    DOCKERFILE("dockerfile-path"),
    LABELS("labels"),
    BUILD_ARGS("build-args"),
}