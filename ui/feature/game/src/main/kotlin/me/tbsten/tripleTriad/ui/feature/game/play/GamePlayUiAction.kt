package me.tbsten.tripleTriad.ui.feature.game.play

import me.tbsten.tripleTriad.domain.game.GameField

internal sealed interface GamePlayUiAction {
    data class SelectCard(val selectedCardIndexInHand: Int) : GamePlayUiAction
    data object UnselectCard : GamePlayUiAction
    data class SelectSquare(val selectedSquare: GameField.Square) : GamePlayUiAction
    data object CompleteApplyCardPlaceRule : GamePlayUiAction
}
