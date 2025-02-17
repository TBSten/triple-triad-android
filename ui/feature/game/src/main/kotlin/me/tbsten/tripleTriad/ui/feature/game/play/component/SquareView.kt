package me.tbsten.tripleTriad.ui.feature.game.play.component

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlin.math.max
import me.tbsten.tripleTriad.domain.game.CardNumber
import me.tbsten.tripleTriad.domain.game.GameCard
import me.tbsten.tripleTriad.domain.game.GameException
import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.domain.game.GamePlayer
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.ValuesPreviewParameterProvider
import me.tbsten.tripleTriad.ui.designSystem.TripleTriadTheme
import me.tbsten.tripleTriad.ui.feature.game.play.GamePlayUiState

private val DummyCard = GameCard(CardNumber(0), CardNumber(0), CardNumber(0), CardNumber(0))

@Composable
internal fun SquareView(
    square: GameField.Square,
    cardColor: @Composable (GameField.Square.PlacedCard) -> Color,
    onSquareClick: () -> Unit,
    onCardClick: (GameField.Square.PlacedCard) -> Unit,
    modifier: Modifier = Modifier,
    isSquareClickable: Boolean = true,
    isCardClickable: Boolean = true,
    sharedTransitionScope: SharedTransitionScope? = null,
) {
    val isPlaced = square is GameField.Square.PlacedCard
    val shape = RoundedCornerShape(12.dp)
    val borderColor by animateColorAsState(Color.White.copy(alpha = if (isPlaced) 0.6f else 0.2f))

    Box(
        modifier
            .clip(shape)
            .clickable(onClick = onSquareClick, enabled = isSquareClickable)
            .border(
                BorderStroke(2.dp, borderColor),
                shape = shape,
            )
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ) {
        // DummyCard for layout
        CardView(
            DummyCard,
            backgroundColor = Color.Transparent,
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        constraints.copy(maxWidth = Constraints.Infinity, maxHeight = Constraints.Infinity),
                    )
                    val size = max(placeable.width, placeable.height)
                    layout(size, size) { }
                },
            size = CardSize.Small,
            isClickable = false,
            onClick = {},
        )

        // placed card
        AnimatedContent(
            square,
            transitionSpec = { EnterTransition.None togetherWith ExitTransition.None },
        ) { square ->
            when (square) {
                is GameField.Square.Empty -> Unit
                is GameField.Square.PlacedCard -> {
                    CardView(
                        square.placedCard,
                        backgroundColor = cardColor(square),
                        size = CardSize.Small,
                        onClick = { onCardClick(square) },
                        isClickable = isCardClickable,
                        animatedVisibilityScope = this,
                        sharedTransitionScope = sharedTransitionScope,
                    )
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

@Preview(group = "Playable")
@Composable
private fun PlayableSquareViewPreview() = PreviewRoot {
    val square = remember {
        SquarePreviewParameters().values.drop(1).first()
    }

    var currentIndex by remember { mutableIntStateOf(0) }

    SharedTransitionLayout {
        Column {
            val squareCount = 3
            repeat(squareCount) { index ->
                SquareView(
                    square = if (currentIndex == index) square else GameField.Square.Empty(square.x, square.y),
                    cardColor = {
                        when (it.owner.name) {
                            "me" -> TripleTriadTheme.colors.me
                            else -> TripleTriadTheme.colors.enemy
                        }
                    },
                    onSquareClick = {},
                    onCardClick = { currentIndex = (currentIndex + 1) % squareCount },
                    sharedTransitionScope = this@SharedTransitionLayout,
                )
            }
        }
    }
}
