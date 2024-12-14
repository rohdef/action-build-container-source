package dk.rohdef.actions.dk.rohdef.actions.github

import com.docker.actions_toolkit.ActionsToolkit
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.promise
import com.github.actions.Core as RawCore

@JsModule("@docker/actions-toolkit")
@JsNonModule
external val actionsToolkit: ActionsToolkit

@OptIn(DelicateCoroutinesApi::class)
class Core internal constructor(val rawCore: RawCore) {
    suspend fun run(
        main: suspend Core.() -> Unit,
        post: (suspend Core.() -> Unit)? = null,
    ) {
        actionsToolkit.run(
                { GlobalScope.promise { main() } },
            post?.let { GlobalScope.promise { it() } }
                ?.let { { it } },
        )
    }

    fun getInput(name: String): String =     rawCore.getInput(name)

    suspend fun group(name: String, contents: suspend Core.() -> Unit) {
        rawCore.group(name) {
            GlobalScope.promise { contents() }
        }.await()
    }

    fun setFailed(message: String) = rawCore.setFailed(message)

    fun debug(message: String) = rawCore.debug(message)
    fun notice(message: String) = rawCore.notice(message)
    fun info(message: String) = rawCore.info(message)
    fun warning(message: String) = rawCore.warning(message)
    fun error(message: String) = rawCore.error(message)
}