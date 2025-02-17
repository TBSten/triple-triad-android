package me.tbsten.tripleTriad.ui.modifier

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(condition: Boolean, modifier: Modifier.() -> Modifier) = then(
    if (condition) {
        Modifier.modifier()
    } else {
        Modifier
    },
)
