@file:JsModule("node:process")
@file:JsNonModule
package node

import kotlin.js.collections.JsMap

external val processx: Process

@OptIn(ExperimentalJsCollectionsApi::class)
external object Process {
    val env: JsMap<String, String>
}