package me.tbsten.tripleTriad.ui.feature.game.play.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
internal fun GameNavigationText(
    visible: Boolean,
    text: String,
    color: Color,
    shadowColor: Color,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        GamePlayText(
            text = text,
            color = color,
            shadowColor = shadowColor,
            style = gameNavigationTextStyle(shadowColor),
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun gameNavigationTextStyle(shadowColor: Color) = GamePlayTextDefaults.style(shadowColor)
    .copy(fontSize = 18.sp, lineHeight = 26.sp)
