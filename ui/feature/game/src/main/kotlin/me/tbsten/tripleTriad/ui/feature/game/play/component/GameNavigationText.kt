package me.tbsten.tripleTriad.ui.feature.game.play.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

internal val textSlideIn = fadeIn(tween(100)) + slideInHorizontally(tween(300)) { 80 }
internal val textSlideOut = fadeOut(tween(100)) + slideOutHorizontally(tween(300)) { -80 }

@Composable
internal fun GameNavigationText(
    visible: Boolean,
    text: String,
    color: Color,
    shadowColor: Color,
) {
    AnimatedVisibility(
        visible = visible,
        enter = textSlideIn,
        exit = textSlideOut,
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
