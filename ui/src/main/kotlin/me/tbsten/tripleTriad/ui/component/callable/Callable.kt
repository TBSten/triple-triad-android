package me.tbsten.tripleTriad.ui.component.callable

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.launch
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.designSystem.components.Button
import me.tbsten.tripleTriad.ui.designSystem.components.Surface
import me.tbsten.tripleTriad.ui.designSystem.components.Text

typealias CallableContent<Arg, Result> = @Composable CallableScope<Arg, Result>.(Arg) -> Unit

class Callable<Arg, Result>(
    private val content: CallableContent<Arg, Result>,
) {
    @SuppressLint("ComposeCompositionLocalUsage")
    @Suppress("ktlint:standard:property-naming", "VariableNaming", "PrivatePropertyName")
    private val LocalCallableState = compositionLocalOf<CallableState<Arg, Result>> {
        error("Not provide Callable(${this::class})")
    }

    @SuppressLint("ComposeNamingUppercase", "ComposeUnstableReceiver")
    @Composable
    operator fun invoke(): CallableState<Arg, Result> = LocalCallableState.current

    @SuppressLint("ComposeUnstableReceiver")
    @Composable
    fun Provider(
        state: CallableState<Arg, Result> = remember { CallableState(content = content) },
        block: @Composable () -> Unit,
    ) {
        CompositionLocalProvider(
            LocalCallableState provides state,
        ) {
            block()
        }
        state.entries.forEach { entry ->
            val entryArg = entry.arg
            val callableScope = entry.toScope()
            content.invoke(callableScope, entryArg)
        }
    }
}

interface CallableScope<Arg, Result> {
    val arg: Arg
    fun end(result: Result)
}

class CallableState<Arg, Result> internal constructor(private val content: CallableContent<Arg, Result>) {
    internal val entries = mutableStateListOf<Entry>()
    suspend fun call(arg: Arg) = suspendCoroutine { continuation ->
        entries.add(
            Entry(
                arg = arg,
                content = content,
                onDispose = { entry, result ->
                    println("CallableState.onDispose($result)")
                    continuation.resume(result)
                    dispose(entry, result)
                },
            ),
        )
    }

    private fun dispose(entry: Entry, result: Result) {
        entries.remove(entry)
    }

    internal inner class Entry(
        val arg: Arg,
        val content: CallableContent<Arg, Result>,
        val onDispose: (Entry, Result) -> Unit,
    ) {
        fun toScope() = object : CallableScope<Arg, Result> {
            override val arg: Arg = this@Entry.arg

            override fun end(result: Result) {
                this@Entry.onDispose(this@Entry, result)
            }
        }
    }
}

private val confirm = Callable<_, Boolean> { text: String ->
    Box(
        Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    end(false)
                }
            }
            .background(Color.Black.copy(alpha = 0.25f))
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Surface {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .testTag("confirm-dialog"),
            ) {
                Text(text)
                Button(text = "cancel", onClick = { end(false) })
                Button(text = "ok", onClick = { end(true) }, modifier = Modifier.testTag("confirm-ok"))
            }
        }
    }
}

@Preview
@Composable
private fun CallablePreview() = PreviewRoot {
    Box(Modifier.fillMaxSize()) {
        listOf(
            confirm,
        ).Provider {
            Column {
                val confirmState = confirm()
                val coroutineScope = rememberCoroutineScope()

                var result by remember { mutableStateOf<Boolean?>(null) }

                Button(
                    text = "confirmState.call",
                    onClick = {
                        repeat(3) {
                            coroutineScope.launch { result = confirmState.call("test") }
                        }
                    },
                )
                Text("result: $result")
            }
        }
    }
}
