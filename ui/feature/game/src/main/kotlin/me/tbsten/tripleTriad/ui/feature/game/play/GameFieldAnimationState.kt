package me.tbsten.tripleTriad.ui.feature.game.play

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import me.tbsten.tripleTriad.ui.error.SafeLaunchedEffect

@Stable
internal class GameFieldAnimationState(initialUiState: GamePlayUiState) {
    internal val fieldRotationX = Animatable(getFieldRotationX(initialUiState))
    internal val fieldScale = Animatable(getFieldScale(initialUiState))

    @Composable
    internal fun Animate(uiState: GamePlayUiState) {
        SafeLaunchedEffect(uiState) {
            listOf(
                launch { fieldRotationX.animateTo(getFieldRotationX(uiState)) },
                launch { fieldScale.animateTo(getFieldScale(uiState)) },
            ).joinAll()
        }
    }

    private fun getFieldRotationX(uiState: GamePlayUiState) = if (uiState is GamePlayUiState.SelectingSquare) {
        0f
    } else {
        10f
    }

    private fun getFieldScale(uiState: GamePlayUiState) = if (uiState is GamePlayUiState.SelectingSquare) {
        1.0f
    } else {
        0.85f
    }
}

@Composable
internal fun rememberGameFieldAnimationState(uiState: GamePlayUiState): GameFieldAnimationState = remember {
    GameFieldAnimationState(uiState)
}.also { it.Animate(uiState) }

internal fun Modifier.animateGameField(
    state: GameFieldAnimationState,
) = then(
    Modifier
        .graphicsLayer {
            transformOrigin = TransformOrigin.Center
            rotationX = state.fieldRotationX.value
            scaleX = state.fieldScale.value
            scaleY = state.fieldScale.value
        },
)
