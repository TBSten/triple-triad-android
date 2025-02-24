package me.tbsten.tripleTriad.ui.feature.game.play

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import me.tbsten.tripleTriad.ui.MultiScreenSizePreview
import me.tbsten.tripleTriad.ui.PreviewRoot

private class GamePlayScreenViewPreviewParameterForVrt :
    CollectionPreviewParameterProvider<GamePlayUiState>(
        gameFieldsForPreview.take(3).flatMap { gameField ->
            meHandsForPreview.take(3).flatMap { meHands ->
                enemyHandsForPreview.take(3).flatMap { enemyHands ->
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
                            selectedCardIndexInHands = 1,
                            turnPlayer = meForPreview,
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
                }
            }
        },
    )

@MultiScreenSizePreview
@Preview
@Composable
private fun GamePlayScreenPreviewForVrt(
    @PreviewParameter(GamePlayScreenViewPreviewParameterForVrt::class)
    uiState: GamePlayUiState,
) = PreviewRoot {
    GamePlayScreen(
        uiState = uiState,
        dispatch = {},
    )
}
