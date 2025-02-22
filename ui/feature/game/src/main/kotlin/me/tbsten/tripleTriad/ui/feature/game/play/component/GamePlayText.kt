package me.tbsten.tripleTriad.ui.feature.game.play.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.tbsten.tripleTriad.ui.designSystem.LocalTextStyle
import me.tbsten.tripleTriad.ui.designSystem.components.Text

@Composable
internal fun GamePlayText(
    text: String,
    color: Color,
    shadowColor: Color,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    Text(
        text = text,
        color = color,
        style = LocalTextStyle.current.merge(
            fontSize = 60.sp,
            shadow = Shadow(
                shadowColor,
                Offset.Zero,
                with(density) { 16.dp.toPx() },
            ),
        ),
        modifier = modifier,
    )
}
