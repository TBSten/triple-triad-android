package me.tbsten.tripleTriad.ui.error

import android.annotation.SuppressLint
import androidx.compose.runtime.compositionLocalOf
import me.tbsten.tripleTriad.error.ErrorStateHolder

@SuppressLint("ComposeCompositionLocalUsage")
val LocalErrorStateHolder = compositionLocalOf<ErrorStateHolder> {
    error("ErrorStateHolder is not provided.")
}
