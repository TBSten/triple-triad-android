package me.tbsten.tripleTriad.ui.error

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import me.tbsten.tripleTriad.error.LaunchSafe

@Composable
fun SafeLaunchedEffect(
    vararg keys: Any?,
    effect: suspend CoroutineScope.() -> Unit,
) {
    val errorStateHolder = LocalErrorStateHolder.current
    val launchSafe = remember(errorStateHolder) { LaunchSafe(errorStateHolder) }

    LaunchedEffect(keys = keys) {
        with(launchSafe) {
            launchSafe {
                effect()
            }
        }
    }
}

@Composable
fun SafeDisposableEffect(
    vararg keys: Any?,
    onLaunch: suspend CoroutineScope.() -> Unit,
    onDispose: suspend CoroutineScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val errorStateHolder = LocalErrorStateHolder.current
    val launchSafe = remember(errorStateHolder) { LaunchSafe(errorStateHolder) }

    DisposableEffect(keys = keys) {
        with(launchSafe) {
            coroutineScope.launchSafe {
                onLaunch()
            }
        }
        onDispose {
            with(launchSafe) {
                coroutineScope.launchSafe {
                    onDispose()
                }
            }
        }
    }
}
