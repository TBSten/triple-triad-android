package me.tbsten.tripleTriad.debug.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.view.KeyEvent
import android.view.Window
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.tbsten.tripleTriad.ui.error.SafeLaunchedEffect

@SuppressLint("ComposeModifierMissing")
@Composable
fun InjectDebugMenu() {
    var show by remember { mutableStateOf(false) }

    val window = (LocalContext.current as Activity).window
    SafeLaunchedEffect(Unit) {
        window.callback = DebugWindowCallback(
            baseCallback = window.callback,
            onKeyEvent = {
                if (it?.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    show = true
                }
            },
        )
    }

    if (show) {
        Dialog(
            onDismissRequest = { show = false },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            Box(Modifier.padding(16.dp)) {
                DebugMenu()
            }
        }
    }
}

private class DebugWindowCallback(
    private val baseCallback: Window.Callback,
    private val onKeyEvent: (event: KeyEvent?) -> Unit,
) : Window.Callback by baseCallback {
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        onKeyEvent(event)
        return baseCallback.dispatchKeyEvent(event)
    }
}
