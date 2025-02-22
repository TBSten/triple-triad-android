package me.tbsten.tripleTriad.ui.component.callable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.designSystem.components.Button
import me.tbsten.tripleTriad.ui.designSystem.components.Text

// return Unit Callables

fun Callable(
    content: @Composable CallableScope<Nothing, Unit>.() -> Unit,
) = Callable<Nothing, Unit>(content = { content() })

fun CallableScope<*, Unit>.end() = end(Unit)

fun <Result> Callable(
    content: CallableContent<Nothing, Result>,
) = Callable<Nothing, Result>(content = content)

@SuppressLint("ComposeUnstableReceiver")
@Composable
fun List<Callable<*, *>>.Provider(block: @Composable () -> Unit) {
    reversed().fold<Callable<*, *>, @Composable () -> Unit>({ block() }) { contents, call ->
        {
            call.Provider {
                contents()
            }
        }
    }.invoke()
}

// multi args Callables

fun <Arg1, Arg2, Result> Callable(
    content: @Composable CallableScope<Pair<Arg1, Arg2>, Result>.(Arg1, Arg2) -> Unit,
) = Callable<Pair<Arg1, Arg2>, Result>(
    content = { args ->
        content(args.first, args.second)
    },
)

fun <Arg1, Arg2, Arg3, Result> Callable(
    content: @Composable CallableScope<Triple<Arg1, Arg2, Arg3>, Result>.(Arg1, Arg2, Arg3) -> Unit,
) = Callable<Triple<Arg1, Arg2, Arg3>, Result>(
    content = { args ->
        content(args.first, args.second, args.third)
    },
)

@Composable
internal fun <Arg, Result : Any> CallablePreview(
    callable: Callable<Arg, Result>,
    arg: Arg,
) = PreviewRoot {
    callable.Provider {
        val state = callable()
        val coroutineScope = rememberCoroutineScope()

        var result by remember { mutableStateOf<Result?>(null) }

        Column(Modifier.fillMaxSize()) {
            Text("result: $result")
            Button(onClick = { coroutineScope.launch { result = state.call(arg) } }) {
                Text("Callable.call()")
            }
        }
    }
}
