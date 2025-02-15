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
            playerHandsForPreview.take(3).flatMap { playerHands ->
                enemyHandsForPreview.take(3).flatMap { enemyHands ->
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
                        ),
                        GamePlayUiState.SelectingSquare(
                            player = meForPreview,
                            playerHands = playerHands,
                            enemy = enemyForPreview,
                            enemyHands = enemyHands,
                            gameField = gameField,
                        ),
                        GamePlayUiState.ApplyingPlaceRule(
                            player = meForPreview,
                            playerHands = playerHands,
                            enemy = enemyForPreview,
                            enemyHands = enemyHands,
                            gameField = gameField,
                        ),
                        GamePlayUiState.Finished(
                            player = meForPreview,
                            playerHands = playerHands,
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
