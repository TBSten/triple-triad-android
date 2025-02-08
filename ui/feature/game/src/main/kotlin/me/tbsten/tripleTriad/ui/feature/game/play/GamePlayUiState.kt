package me.tbsten.tripleTriad.ui.feature.game.play

internal sealed interface GamePlayUiState {
    data object SelectingFirstPlayer : GamePlayUiState

    data object SelectingCard : GamePlayUiState

    data object SelectingSquare : GamePlayUiState

    data object ApplyingPlaceRule : GamePlayUiState

    data object Finished : GamePlayUiState
}
