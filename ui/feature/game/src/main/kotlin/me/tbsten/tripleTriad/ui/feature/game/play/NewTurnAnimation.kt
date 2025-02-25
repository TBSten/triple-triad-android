package me.tbsten.tripleTriad.ui.feature.game.play

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.tbsten.tripleTriad.ui.designSystem.TripleTriadTheme
import me.tbsten.tripleTriad.ui.designSystem.components.HorizontalDivider
import me.tbsten.tripleTriad.ui.error.SafeLaunchedEffect
import me.tbsten.tripleTriad.ui.feature.game.play.component.GamePlayText
import me.tbsten.tripleTriad.ui.feature.game.play.component.GamePlayTextDefaults
import me.tbsten.tripleTriad.ui.feature.game.play.component.textSlideIn
import me.tbsten.tripleTriad.ui.feature.game.play.component.textSlideOut

@Composable
internal fun NewTurnAnimation(
    uiState: GamePlayUiState,
) {
    val turnPlayer = (uiState as? WithTurnPlayer)?.turnPlayer
    var showAnimation by remember { mutableStateOf(false) }

    SafeLaunchedEffect(turnPlayer) {
        turnPlayer?.let {
            showAnimation = true
            delay(800)
            showAnimation = false
        }
    }

    AnimatedContent(
        targetState = if (showAnimation) turnPlayer else null,
        transitionSpec = {
            fadeIn(tween(300)) togetherWith
                fadeOut(tween(300)) using
                SizeTransform { _, _ -> snap(if (initialState != null) 300 else 0) }
        },
    ) { player ->
        if (player != null) {
            Column(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f))
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                val isMeTurn = player == uiState.me
                val shadowColor = if (isMeTurn) {
                    TripleTriadTheme.colors.meShadow
                } else {
                    TripleTriadTheme.colors.enemyShadow
                }
                GamePlayText(
                    text = if (isMeTurn) "Your Turn" else "Enemy Turn",
                    style = TripleTriadTheme.typography.h1.copy(
                        fontSize = 40.sp,
                        lineHeight = 56.sp,
                        textAlign = TextAlign.Center,
                        shadow = GamePlayTextDefaults.shadow(shadowColor),
                    ),
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .animateEnterExit(
                            enter = textSlideIn,
                            exit = textSlideOut,
                        ),
                    shadowColor = shadowColor,
                )

                val borderColor = TripleTriadTheme.colors.meBorder
                HorizontalDivider(
                    0f to borderColor,
                    1f to borderColor.copy(alpha = 0f),
                    thickness = 4.dp,
                    modifier = Modifier.width(IntrinsicSize.Max),
                )
            }
        }
    }
}
