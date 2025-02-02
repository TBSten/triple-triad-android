package your.projectPackage.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun LifecycleCreateEffect(lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current, onEvent: () -> Unit) {
    LifecycleEventEffect(
        event = Lifecycle.Event.ON_CREATE,
        lifecycleOwner = lifecycleOwner,
        onEvent = onEvent,
    )
}
