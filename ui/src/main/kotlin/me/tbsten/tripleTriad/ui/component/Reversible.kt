package me.tbsten.tripleTriad.ui.component

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.designSystem.components.Button
import me.tbsten.tripleTriad.ui.designSystem.components.Text

@Composable
fun Reversible(
    state: ReversibleState,
    front: @Composable () -> Unit,
    back: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    direction: ReversibleDirection = ReversibleDirection.Vertical,
    animationSpec: AnimationSpec<Float> = spring(),
) {
    val frontRotation by
        animateFloatAsState(
            when (state) {
                ReversibleState.Front -> 0f
                ReversibleState.Back -> 180f
            },
            animationSpec = animationSpec,
        )
    val isFront = abs(frontRotation % 360) in 0f..90f || frontRotation % 360 in 270f..360f

    if (isFront) {
        Box(
            modifier.graphicsLayer {
                when (direction) {
                    ReversibleDirection.Horizontal -> {
                        rotationX = frontRotation
                    }
                    ReversibleDirection.Vertical -> {
                        rotationY = frontRotation
                    }
                }
            },
        ) {
            front()
        }
    } else {
        val backRotation = frontRotation - 180f
        Box(
            modifier.graphicsLayer {
                when (direction) {
                    ReversibleDirection.Horizontal -> {
                        rotationX = backRotation
                    }
                    ReversibleDirection.Vertical -> {
                        rotationY = backRotation
                    }
                }
            },
        ) {
            back()
        }
    }
}

enum class ReversibleState {
    Front,
    Back,
}

enum class ReversibleDirection {
    Vertical,
    Horizontal,
}

@Preview
@Composable
private fun ReversiblePreview() = PreviewRoot {
    var state by remember { mutableStateOf(ReversibleState.Front) }
    var direction by remember { mutableStateOf(ReversibleDirection.Vertical) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Reversible(
            state = state,
            direction = direction,
            front = {
                Box(
                    Modifier
                        .background(Color.Red)
                        .size(150.dp),
                ) { Text("front", color = Color.White) }
            },
            back = {
                Box(
                    Modifier
                        .background(Color.Blue)
                        .size(150.dp),
                ) { Text("back", color = Color.White) }
            },
            animationSpec = tween(
                2500,
                easing = EaseOutElastic,
            ),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                text = "Reverse",
                onClick = {
                    state = when (state) {
                        ReversibleState.Front -> ReversibleState.Back
                        ReversibleState.Back -> ReversibleState.Front
                    }
                },
            )
            Button(
                text = "Dir: $direction",
                onClick = {
                    direction = when (direction) {
                        ReversibleDirection.Horizontal -> ReversibleDirection.Vertical
                        ReversibleDirection.Vertical -> ReversibleDirection.Horizontal
                    }
                },
            )
        }
    }
}
