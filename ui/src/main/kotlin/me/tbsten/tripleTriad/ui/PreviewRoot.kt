package me.tbsten.tripleTriad.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.tbsten.tripleTriad.error.AbstractErrorStateHolder
import me.tbsten.tripleTriad.ui.designSystem.TripleTriadTheme
import me.tbsten.tripleTriad.ui.error.LocalErrorStateHolder

@SuppressLint("ComposeModifierMissing")
@Composable
fun PreviewRoot(content: @Composable () -> Unit) {
    TripleTriadTheme {
        CompositionLocalProvider(
            LocalErrorStateHolder provides PreviewErrorStateHolder,
        ) {
            Box(Modifier.sizeIn(minWidth = 1.dp, minHeight = 1.dp)) {
                content()
            }
        }
    }
}

internal object PreviewErrorStateHolder : AbstractErrorStateHolder()
