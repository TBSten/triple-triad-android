package your.projectPackage.ui.error

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.compose.dropUnlessResumed
import kotlinx.coroutines.launch
import your.projectPackage.error.LaunchSafe

@Composable
fun handleUiEvent(block: suspend () -> Unit): () -> Unit {
    val coroutineScope = rememberCoroutineScope()
    if (LocalInspectionMode.current) return { coroutineScope.launch { block() } }

    val errorStateHolder = LocalErrorStateHolder.current
    val launchSafe = remember(errorStateHolder) { LaunchSafe(errorStateHolder) }
    return dropUnlessResumed {
        with(launchSafe) {
            coroutineScope.launchSafe { block() }
        }
    }
}
