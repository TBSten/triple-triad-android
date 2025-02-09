package me.tbsten.tripleTriad.ui.feature.game.play

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import me.tbsten.tripleTriad.ui.Dispatch
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.ValuesPreviewParameterProvider
import me.tbsten.tripleTriad.ui.consumeViewModel

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
) {
    TODO()
}

internal class GamePlayUiStatePreviewParameters : ValuesPreviewParameterProvider<GamePlayUiState>()

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
