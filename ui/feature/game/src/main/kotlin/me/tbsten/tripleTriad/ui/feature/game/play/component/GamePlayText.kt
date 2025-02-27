package me.tbsten.tripleTriad.ui.feature.game.play.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
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
    style: TextStyle = GamePlayTextDefaults.style(shadowColor),
    overflow: TextOverflow = TextOverflow.Clip,
) {
    Text(
        text = text,
        color = color,
        style = style,
        modifier = modifier,
        overflow = overflow,
    )
}

internal object GamePlayTextDefaults {
    @Composable
    fun style(
        shadowColor: Color,
    ): TextStyle = LocalTextStyle.current.merge(
        fontSize = 60.sp,
        shadow = shadow(shadowColor),
    )

    @Composable
    fun shadow(color: Color) = Shadow(
        color,
        Offset.Zero,
        with(LocalDensity.current) { 16.dp.toPx() },
    )
}
