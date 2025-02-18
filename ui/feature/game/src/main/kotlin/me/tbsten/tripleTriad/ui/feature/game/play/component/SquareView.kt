package me.tbsten.tripleTriad.ui.feature.game.play.component

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.tbsten.tripleTriad.domain.game.CardNumber
import me.tbsten.tripleTriad.domain.game.GameCard
import me.tbsten.tripleTriad.domain.game.GameException
import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.domain.game.GamePlayer
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.ValuesPreviewParameterProvider
import me.tbsten.tripleTriad.ui.component.Reversible
import me.tbsten.tripleTriad.ui.component.ReversibleState
import me.tbsten.tripleTriad.ui.designSystem.TripleTriadTheme
import me.tbsten.tripleTriad.ui.designSystem.components.Button
import me.tbsten.tripleTriad.ui.error.SafeLaunchedEffect
import me.tbsten.tripleTriad.ui.feature.game.play.GamePlayUiState
import me.tbsten.tripleTriad.ui.testing.IgnoreVrt

private val enterCard = fadeIn(tween(200)) +
    slideInVertically(tween(400)) { -it / 2 } +
    scaleIn(tween(400), 1.5f)

private val exitCard = fadeOut(tween(500)) +
    scaleOut(
        tween(500, easing = EaseOutQuint),
        targetScale = 1.4f,
    )

@SuppressLint("SlotReused")
@Composable
internal fun SquareView(
    square: GameField.Square,
    cardColor: @Composable (GameField.Square.PlacedCard) -> Color,
    onSquareClick: () -> Unit,
    onCardClick: (GameField.Square.PlacedCard) -> Unit,
    modifier: Modifier = Modifier,
    isSquareClickable: Boolean = true,
    isCardClickable: Boolean = true,
    cardSize: CardSize = CardSize.Small,
) {
    val squareTransition = updateTransition(square)
    val shape = RoundedCornerShape(12.dp)
    val borderColor by squareTransition.animateColor {
        Color.White.copy(
            if (it is GameField.Square.PlacedCard) 0.6f else 0.2f,
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .clip(shape)
                .clickable(onClick = onSquareClick, enabled = isSquareClickable)
                .border(
                    BorderStroke(2.dp, borderColor),
                    shape = shape,
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            CardSizedBox(
                size = cardSize,
            )
        }
        CardInSquare(
            squareTransition = squareTransition,
            cardColor = cardColor,
            onCardClick = onCardClick,
            isCardClickable = isCardClickable,
            cardSize = cardSize,
        )
    }
}

@SuppressLint("SlotReused")
@Composable
private fun CardInSquare(
    squareTransition: Transition<GameField.Square>,
    cardColor: @Composable (GameField.Square.PlacedCard) -> Color,
    onCardClick: (GameField.Square.PlacedCard) -> Unit,
    isCardClickable: Boolean = true,
    cardSize: CardSize = CardSize.Small,
) {
    val beforeSquare = squareTransition.segment.initialState
    val afterSquare = squareTransition.segment.targetState

    var isFront by remember { mutableStateOf(true) }
    val beforeOwner = if (beforeSquare is GameField.Square.PlacedCard) beforeSquare.owner else null
    val afterOwner = if (afterSquare is GameField.Square.PlacedCard) afterSquare.owner else null

    SafeLaunchedEffect(beforeOwner, afterOwner) {
        if (beforeOwner != null && afterOwner != null && beforeOwner != afterOwner) {
            isFront = !isFront
        }
    }

    when {
        // empty to empty
        beforeSquare is GameField.Square.Empty && afterSquare is GameField.Square.Empty -> {}
        // placed to placed
        beforeSquare is GameField.Square.PlacedCard && afterSquare is GameField.Square.PlacedCard -> {
            key(beforeSquare.owner, afterSquare.owner) {
                var state by remember { mutableStateOf(ReversibleState.Front) }
                SafeLaunchedEffect { state = ReversibleState.Back }
                Reversible(
                    state = state,
                    front = {
                        CardView(
                            beforeSquare.placedCard,
                            backgroundColor = cardColor(beforeSquare),
                            size = cardSize,
                            onClick = { onCardClick(beforeSquare) },
                            isClickable = isCardClickable,
                        )
                    },
                    back = {
                        CardView(
                            afterSquare.placedCard,
                            backgroundColor = cardColor(afterSquare),
                            size = cardSize,
                            onClick = { onCardClick(afterSquare) },
                            isClickable = isCardClickable,
                        )
                    },
                    animationSpec = tween(2000),
                )
            }
        }
        // enter or exit
        beforeSquare is GameField.Square.PlacedCard || afterSquare is GameField.Square.PlacedCard -> {
            val coroutineScope = rememberCoroutineScope()
            val visible = beforeSquare is GameField.Square.Empty
            val visibleState = remember { MutableTransitionState(!visible) }
            SideEffect {
                coroutineScope.launch {
                    delay(300)
                    visibleState.targetState = visible
                }
            }
            AnimatedVisibility(
                visibleState = visibleState,
                enter = enterCard,
                exit = exitCard,
            ) {
                when {
                    beforeSquare is GameField.Square.PlacedCard -> {
                        CardView(
                            beforeSquare.placedCard,
                            backgroundColor = cardColor(beforeSquare),
                            size = cardSize,
                            onClick = { onCardClick(beforeSquare) },
                            isClickable = isCardClickable,
                        )
                    }
                    afterSquare is GameField.Square.PlacedCard -> {
                        CardView(
                            afterSquare.placedCard,
                            backgroundColor = cardColor(afterSquare),
                            size = cardSize,
                            onClick = { onCardClick(afterSquare) },
                            isClickable = isCardClickable,
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("ComposeUnstableReceiver")
@Composable
internal fun GamePlayUiState.cardColorFor(square: GameField.Square.PlacedCard): Color = when (square.owner) {
    me -> TripleTriadTheme.colors.me
    enemy -> TripleTriadTheme.colors.enemy
    else -> throw GameException.IllegalPlayer("square.owner(${square.owner})", "$me または $enemy である必要があります。")
}

private class SquarePreviewParameters :
    ValuesPreviewParameterProvider<GameField.Square>(
        GameField.Square.Empty(0, 0),
        GameField.Square.PlacedCard(
            0,
            0,
            owner = GamePlayer("me"),
            placedCard = GameCard(CardNumber(1), CardNumber(23), CardNumber(45), CardNumber.Ace),
        ),
        GameField.Square.PlacedCard(
            0,
            0,
            owner = GamePlayer("enemy"),
            placedCard = GameCard(CardNumber(1), CardNumber(23), CardNumber(45), CardNumber.Ace),
        ),
    )

@Preview
@Composable
private fun SquareViewPreview(
    @PreviewParameter(SquarePreviewParameters::class)
    square: GameField.Square,
) = PreviewRoot {
    SquareView(
        square = square,
        cardColor = {
            when (it.owner.name) {
                "me" -> TripleTriadTheme.colors.me
                else -> TripleTriadTheme.colors.enemy
            }
        },
        onSquareClick = {},
        onCardClick = { },
    )
}

@IgnoreVrt
@Preview
@Composable
private fun PlaceAnimatedPreview() = PreviewRoot {
    var square by remember { mutableStateOf<GameField.Square>(GameField.Square.Empty(0, 0)) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SquareView(
            square = square,
            cardColor = {
                when (it.owner.name) {
                    "me" -> TripleTriadTheme.colors.me
                    else -> TripleTriadTheme.colors.enemy
                }
            },
            onSquareClick = {},
            onCardClick = {},
            isSquareClickable = false,
            isCardClickable = false,
        )
        Button(
            text = "place me card",
            onClick = {
                square = GameField.Square.PlacedCard(
                    x = square.x,
                    y = square.y,
                    owner = GamePlayer("me"),
                    placedCard = GameCard(CardNumber(1), CardNumber(2), CardNumber(3), CardNumber(4)),
                )
            },
        )
        Button(
            text = "place enemy card",
            onClick = {
                square = GameField.Square.PlacedCard(
                    x = square.x,
                    y = square.y,
                    owner = GamePlayer("enemy"),
                    placedCard = GameCard(CardNumber(5), CardNumber(6), CardNumber(7), CardNumber(8)),
                )
            },
        )
        Button(
            text = "to empty",
            onClick = {
                square = GameField.Square.Empty(
                    x = square.x,
                    y = square.y,
                )
            },
        )
    }
}
