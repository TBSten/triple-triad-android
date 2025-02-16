package me.tbsten.tripleTriad.ui.feature.game.play

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import me.tbsten.tripleTriad.ui.Dispatch
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.consumeViewModel
import me.tbsten.tripleTriad.ui.designSystem.LocalTextStyle
import me.tbsten.tripleTriad.ui.designSystem.TripleTriadTheme
import me.tbsten.tripleTriad.ui.designSystem.components.Text
import me.tbsten.tripleTriad.ui.feature.game.play.component.CardSize
import me.tbsten.tripleTriad.ui.feature.game.play.component.GameFieldView
import me.tbsten.tripleTriad.ui.feature.game.play.component.PlayerHandsView
import me.tbsten.tripleTriad.ui.feature.game.play.component.cardColorFor
import me.tbsten.tripleTriad.ui.modifier.darken
import me.tbsten.tripleTriad.ui.testing.IgnoreVrt

@Composable
internal fun GamePlayScreen(
    modifier: Modifier = Modifier,
    viewModel: GamePlayViewModel = hiltViewModel(),
) {
    val (uiState, dispatch) = consumeViewModel(viewModel)

    GamePlayScreen(
        uiState = uiState,
        dispatch = dispatch,
        modifier = modifier,
    )
}

@Suppress("UNUSED_PARAMETER")
@Composable
internal fun GamePlayScreen(
    uiState: GamePlayUiState,
    dispatch: Dispatch<GamePlayUiAction>,
    modifier: Modifier = Modifier,
    gameFieldAnimationState: GameFieldAnimationState = rememberGameFieldAnimationState(uiState),
) {
    val density = LocalDensity.current

    SharedTransitionLayout(
        modifier = modifier
            .background(Color(0xFF2A2A2A))
            .systemGesturesPadding(),
    ) {
        Box {
            Column(
                Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = -20.dp)
                    .fillMaxWidth()
                    .zIndex(-1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PlayerHandsView(
                    hands = uiState.enemyHands,
                    cardBackgroundColor = TripleTriadTheme.colors.enemy,
                    cardSize = CardSize.Large,
                    isClickable = false,
                    onClick = {},
                    modifier = Modifier
                        .darken(0.75f)
                        .scale(0.5f)
                        .fillMaxWidth(),
                )
                Text(
                    text = "${uiState.enemyPoint.value}",
                    color = Color.White,
                    style = LocalTextStyle.current.merge(
                        fontSize = 60.sp,
                        shadow = Shadow(
                            Color.Red,
                            Offset.Zero,
                            with(density) { 16.dp.toPx() },
                        ),
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 12.dp),
                )
            }

            GameFieldView(
                gameField = uiState.gameField,
                cardColor = { uiState.cardColorFor(it) },
                modifier = Modifier
                    .animateGameField(gameFieldAnimationState)
                    .padding(16.dp)
                    .zIndex(0f),
                isCardClickable = uiState.isSquareCardClickable,
                onCardClick = {},
                isSquareClickable = uiState.isSquareCardClickable,
                onSquareClick = { dispatch(GamePlayUiAction.SelectSquare(it)) },
            )

            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 20.dp)
                    .fillMaxWidth()
                    .zIndex(+1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${uiState.playerPoint.value}",
                    color = Color.White,
                    style = LocalTextStyle.current.merge(
                        fontSize = 60.sp,
                        shadow = Shadow(
                            Color.Blue,
                            Offset.Zero,
                            with(density) { 16.dp.toPx() },
                        ),
                    ),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 12.dp),
                )

                PlayerHandsView(
                    hands = uiState.playerHands,
                    cardBackgroundColor = TripleTriadTheme.colors.me,
                    cardSize = CardSize.Medium,
                    onClick = { clickedCard ->
                        val clickedCardIndex = uiState.playerHands.indexOfFirst { it == clickedCard }
                        dispatch(
                            if (uiState.playerSelectedCardIndexInHand == clickedCardIndex) {
                                GamePlayUiAction.UnselectCard
                            } else {
                                GamePlayUiAction.SelectCard(clickedCardIndex)
                            },
                        )
                    },
                    isClickable = uiState.isHandsCardClickable,
                    selectedCardIndex = uiState.playerSelectedCardIndexInHand,
                )
            }
        }
    }
}

internal class GamePlayUiStatePreviewParameters :
    CollectionPreviewParameterProvider<GamePlayUiState>(
        gameFieldsForPreview.flatMap { gameField ->
            val playerHands = playerHandsForPreview[0]
            val enemyHands = enemyHandsForPreview[0]
            listOf(
                GamePlayUiState.SelectingFirstPlayer(
                    player = meForPreview,
                    playerHands = playerHands,
                    enemy = enemyForPreview,
                    enemyHands = enemyHands,
                    gameField = gameField,
                ),
                GamePlayUiState.SelectingCard(
                    player = meForPreview,
                    playerHands = playerHands,
                    enemy = enemyForPreview,
                    enemyHands = enemyHands,
                    gameField = gameField,
                    turnPlayer = meForPreview,
                ),
                GamePlayUiState.SelectingSquare(
                    player = meForPreview,
                    playerHands = playerHands,
                    enemy = enemyForPreview,
                    enemyHands = enemyHands,
                    gameField = gameField,
                    turnPlayer = meForPreview,
                    selectedCardIndexInHand = 1,
                ),
                GamePlayUiState.ApplyingPlaceRule(
                    player = meForPreview,
                    playerHands = playerHands,
                    enemy = enemyForPreview,
                    enemyHands = enemyHands,
                    gameField = gameField,
                    turnPlayer = meForPreview,
                ),
                GamePlayUiState.Finished(
                    player = meForPreview,
                    playerHands = playerHands,
                    enemy = enemyForPreview,
                    enemyHands = enemyHands,
                    gameField = gameField,
                ),
            )
        },
    )

@IgnoreVrt
@Preview
@Composable
private fun GamePlayScreenPreview(
    @PreviewParameter(GamePlayUiStatePreviewParameters::class)
    uiState: GamePlayUiState,
) = PreviewRoot {
    GamePlayScreen(
        uiState = uiState,
        dispatch = {},
    )
}
