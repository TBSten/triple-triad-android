package me.tbsten.tripleTriad.ui.component.callable

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.tbsten.tripleTriad.ui.designSystem.components.Text

typealias DialogArg = Pair<@Composable () -> Unit, DialogProperties>

fun <Result> nativeDialog(
    onDismissRequest: CallableScope<DialogArg, Result>.() -> Unit,
): Callable<DialogArg, Result> = Callable<DialogArg, Result> { (content, properties) ->
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = properties,
    ) {
        Column {
            content()
        }
    }
}

@Preview
@Composable
private fun NativeDialogCallablePreview() = CallablePreview(
    nativeDialog<Unit>(onDismissRequest = { end() }),
    @Composable {
        Text("dialog content")
    } to DialogProperties(),
)
