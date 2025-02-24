package me.tbsten.tripleTriad.ui.feature.game.play

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.domain.game.GamePlayer
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.ValuesPreviewParameterProvider
import me.tbsten.tripleTriad.ui.error.SafeLaunchedEffect
import me.tbsten.tripleTriad.ui.feature.game.R
import me.tbsten.tripleTriad.ui.testing.IgnoreVrt

@Suppress("NAME_SHADOWING")
@Composable
internal fun SelectingFirstPlayerAnimation(
    uiState: GamePlayUiState,
    onFinish: () -> Unit,
) {
    AnimatedContent(
        uiState as? GamePlayUiState.SelectingFirstPlayer,
        transitionSpec = {
            val exitTime = 350
            fadeIn() togetherWith
                fadeOut(tween(exitTime)) using
                SizeTransform { _, _ -> snap(exitTime) }
        },
        contentKey = { it == null },
    ) { uiState ->
        if (uiState != null) {
            SelectingFirstPlayerAnimation(
                uiState = uiState,
                onFinish = onFinish,
            )
        }
    }
}

@SuppressLint("ComposeUnstableReceiver")
@Composable
internal fun AnimatedContentScope.SelectingFirstPlayerAnimation(
    uiState: GamePlayUiState.SelectingFirstPlayer,
    onFinish: () -> Unit,
) {
    val rotation = triangleRotation(uiState, onFinish)

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f)),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painterResource(R.drawable.select_first_player_triangle),
            contentDescription = null,
            modifier = Modifier
                .animateEnterExit(
                    exit = fadeOut(tween(150)),
                )
                .graphicsLayer(
                    rotationZ = rotation,
                    transformOrigin = TransformOrigin(0.5f, 1.0f),
                )
                .wrapContentHeight()
                .width(60.dp),
        )
    }
}

@Composable
private fun triangleRotation(
    uiState: GamePlayUiState.SelectingFirstPlayer,
    onFinish: () -> Unit,
): Float {
    val currentFirstPlayer by rememberUpdatedState(uiState.firstPlayer)
    val currentMe by rememberUpdatedState(uiState.me)
    val rotation = remember { Animatable(0f) }

    SafeLaunchedEffect {
        while (true) {
            if (currentFirstPlayer == null) {
                rotation.animateTo(
                    rotation.targetValue + 360,
                    animationSpec = tween(200, easing = LinearEasing),
                )
            } else {
                val turnCount = 2 + if (currentFirstPlayer == currentMe) 0.5f else 0.0f
                rotation.animateTo(
                    rotation.targetValue + 360 * turnCount,
                    animationSpec = tween((200 * 2 * turnCount).toInt(), easing = EaseOutSine),
                )
                delay(500)
                onFinish()
                break
            }
        }
    }

    return rotation.value
}

private class GamePlayerPreviewParameters :
    ValuesPreviewParameterProvider<GamePlayer?>(
        null,
        meForPreview,
        enemyForPreview,
    )

@IgnoreVrt
@Preview(showBackground = true)
@Composable
private fun SelectingFirstPlayerAnimationPreview(
    @PreviewParameter(GamePlayerPreviewParameters::class)
    firstPlayer: GamePlayer?,
) = PreviewRoot {
    SafeLaunchedEffect { delay(1000) }
    SelectingFirstPlayerAnimation(
        uiState = GamePlayUiState.SelectingFirstPlayer(
            me = meForPreview,
            meHands = meHandsForPreview[0],
            enemy = enemyForPreview,
            enemyHands = enemyHandsForPreview[0],
            gameField = GameField.emptyAll(),
            firstPlayer = firstPlayer,
        ),
        onFinish = {},
    )
}
