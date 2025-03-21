package me.tbsten.tripleTriad.ui.feature.game.play

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import me.tbsten.tripleTriad.ui.Dispatch
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.consumeViewModel
import me.tbsten.tripleTriad.ui.designSystem.TripleTriadTheme
import me.tbsten.tripleTriad.ui.feature.game.play.component.CardSize
import me.tbsten.tripleTriad.ui.feature.game.play.component.GameFieldView
import me.tbsten.tripleTriad.ui.feature.game.play.component.GameNavigationText
import me.tbsten.tripleTriad.ui.feature.game.play.component.GamePlayText
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

@Composable
internal fun GamePlayScreen(
    uiState: GamePlayUiState,
    dispatch: Dispatch<GamePlayUiAction>,
    modifier: Modifier = Modifier,
    gameFieldAnimationState: GameFieldAnimationState = rememberGameFieldAnimationState(uiState),
) {
    SharedTransitionLayout(
        modifier = modifier
            .background(Color(0xFF2A2A2A))
            .systemGesturesPadding(),
    ) {
        GamePlayScreenLayout(
            enemyHands = {
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
            },
            enemyInfo = {
                GamePlayText(
                    text = "${uiState.enemyPoint.value}",
                    color = Color.White,
                    shadowColor = TripleTriadTheme.colors.enemyShadow,
                )

                Spacer(Modifier.weight(1f))

                GameNavigationText(
                    visible = uiState is WithTurnPlayer && uiState.turnPlayer == uiState.enemy,
                    text = uiState.enemy.name,
                    color = Color.White,
                    shadowColor = TripleTriadTheme.colors.enemyShadow,
                )
                GameNavigationText(
                    visible = uiState is WithTurnPlayer && uiState.turnPlayer == uiState.enemy,
                    text = " Turn",
                    color = Color.White,
                    shadowColor = TripleTriadTheme.colors.enemyShadow,
                )
            },
            gameField = {
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
            },
            meInfo = {
                GameNavigationText(
                    visible = uiState is WithTurnPlayer && uiState.turnPlayer == uiState.me,
                    text = uiState.me.name,
                    color = Color.White,
                    shadowColor = TripleTriadTheme.colors.meShadow,
                )
                GameNavigationText(
                    visible = uiState is WithTurnPlayer && uiState.turnPlayer == uiState.me,
                    text = " Turn",
                    color = Color.White,
                    shadowColor = TripleTriadTheme.colors.meShadow,
                )

                Spacer(Modifier.weight(1f))

                GamePlayText(
                    text = "${uiState.mePoint.value}",
                    color = Color.White,
                    shadowColor = TripleTriadTheme.colors.meShadow,
                    modifier = Modifier,
                )
            },
            meHands = {
                PlayerHandsView(
                    hands = uiState.meHands,
                    cardBackgroundColor = TripleTriadTheme.colors.me,
                    cardSize = CardSize.Medium,
                    onClick = { clickedCard ->
                        val clickedCardIndex = uiState.meHands.indexOfFirst { it == clickedCard }
                        dispatch(
                            if (uiState.meSelectedCardIndexInHands == clickedCardIndex) {
                                GamePlayUiAction.UnselectCard
                            } else {
                                GamePlayUiAction.SelectCard(clickedCardIndex)
                            },
                        )
                    },
                    isClickable = uiState.isHandsCardClickable,
                    selectedCardIndex = uiState.meSelectedCardIndexInHands,
                )
            },
        )
    }

    SelectingFirstPlayerAnimation(
        uiState = uiState,
        onFinish = { dispatch(GamePlayUiAction.CompleteSelectingFirstPlayer) },
    )

    NewTurnAnimation(
        uiState = uiState,
    )
}

@Composable
private fun GamePlayScreenLayout(
    enemyHands: @Composable ColumnScope.() -> Unit,
    enemyInfo: @Composable RowScope.() -> Unit,
    gameField: @Composable () -> Unit,
    meInfo: @Composable RowScope.() -> Unit,
    meHands: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Column(
            Modifier
                .align(Alignment.TopCenter)
                .offset(y = -20.dp)
                .fillMaxWidth()
                .zIndex(-1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            enemyHands()

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                enemyInfo()
            }
        }

        gameField()

        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 20.dp)
                .fillMaxWidth()
                .zIndex(+1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                meInfo()
            }

            meHands()
        }
    }
}

internal class GamePlayUiStatePreviewParameters :
    CollectionPreviewParameterProvider<GamePlayUiState>(
        gameFieldsForPreview.flatMap { gameField ->
            val meHands = meHandsForPreview[0]
            val enemyHands = enemyHandsForPreview[0]
            listOf(
                GamePlayUiState.SelectingFirstPlayer(
                    me = meForPreview,
                    meHands = meHands,
                    enemy = enemyForPreview,
                    enemyHands = enemyHands,
                    gameField = gameField,
                    firstPlayer = null,
                ),
                GamePlayUiState.SelectingFirstPlayer(
                    me = meForPreview,
                    meHands = meHands,
                    enemy = enemyForPreview,
                    enemyHands = enemyHands,
                    gameField = gameField,
                    firstPlayer = meForPreview,
                ),
                GamePlayUiState.SelectingFirstPlayer(
                    me = meForPreview,
                    meHands = meHands,
                    enemy = enemyForPreview,
                    enemyHands = enemyHands,
                    gameField = gameField,
                    firstPlayer = enemyForPreview,
                ),
                GamePlayUiState.SelectingCard(
                    me = meForPreview,
                    meHands = meHands,
                    enemy = enemyForPreview,
                    enemyHands = enemyHands,
                    gameField = gameField,
                    turnPlayer = meForPreview,
                ),
                GamePlayUiState.SelectingSquare(
                    me = meForPreview,
                    meHands = meHands,
                    enemy = enemyForPreview,
                    enemyHands = enemyHands,
                    gameField = gameField,
                    turnPlayer = meForPreview,
                    selectedCardIndexInHands = 1,
                ),
                GamePlayUiState.ApplyingPlaceRule(
                    me = meForPreview,
                    meHands = meHands,
                    enemy = enemyForPreview,
                    enemyHands = enemyHands,
                    gameField = gameField,
                    turnPlayer = meForPreview,
                ),
                GamePlayUiState.Finished(
                    me = meForPreview,
                    meHands = meHands,
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
