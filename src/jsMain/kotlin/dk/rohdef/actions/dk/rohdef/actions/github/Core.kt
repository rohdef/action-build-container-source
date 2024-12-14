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
class Core internal constructor() {
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

    fun getInput(name: String): String = RawCore.getInput(name)

    suspend fun group(name: String, contents: suspend Core.() -> Unit) {
        RawCore.group(name) {
            GlobalScope.promise { contents() }
        }.await()
    }

    fun setFailed(message: String) = RawCore.setFailed(message)

    fun debug(message: String) = RawCore.debug(message)
    fun notice(message: String) = RawCore.notice(message)
    fun info(message: String) = RawCore.info(message)
    fun warning(message: String) = RawCore.warning(message)
    fun error(message: String) = RawCore.error(message)
}