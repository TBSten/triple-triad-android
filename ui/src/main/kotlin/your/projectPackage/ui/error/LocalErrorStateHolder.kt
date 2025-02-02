package your.projectPackage.ui.error

import android.annotation.SuppressLint
import androidx.compose.runtime.compositionLocalOf
import your.projectPackage.error.ErrorStateHolder

@SuppressLint("ComposeCompositionLocalUsage")
val LocalErrorStateHolder = compositionLocalOf<ErrorStateHolder> {
    error("ErrorStateHolder is not provided.")
}
