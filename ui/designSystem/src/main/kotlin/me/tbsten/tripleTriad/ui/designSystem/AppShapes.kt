package me.tbsten.tripleTriad.ui.designSystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import me.tbsten.tripleTriad.common.lazyWithReceiver

interface AppShapes {
    /* Add your shapes */
    val medium: Shape
    val full: Shape
}

internal object DefaultAppShapes : AppShapes {
    override val medium: Shape = RoundedCornerShape(16.dp)
    override val full: Shape = RoundedCornerShape(100)
}

/**
 * AppShapes を MaterialTheme.shapes に変換する拡張関数。
 */
internal val AppShapes.asMaterial: Shapes by lazyWithReceiver {
    Shapes(
        extraSmall = this.medium as RoundedCornerShape,
        small = this.medium as RoundedCornerShape,
        medium = this.medium as RoundedCornerShape,
        large = this.medium as RoundedCornerShape,
        extraLarge = this.medium as RoundedCornerShape,
    )
}
