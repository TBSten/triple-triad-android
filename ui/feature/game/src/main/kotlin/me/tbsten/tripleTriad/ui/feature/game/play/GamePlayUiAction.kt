package me.tbsten.tripleTriad.ui.feature.game.play

import me.tbsten.tripleTriad.domain.game.GameField

internal sealed interface GamePlayUiAction {
    data class SelectCard(val selectedCardIndexInHands: Int) : GamePlayUiAction
    data object UnselectCard : GamePlayUiAction
    data class SelectSquare(val selectedSquare: GameField.Square) : GamePlayUiAction
    data object CompletePlaceCard : GamePlayUiAction
    data object CompleteApplyCardPlaceRule : GamePlayUiAction
}
