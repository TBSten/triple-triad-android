package me.tbsten.tripleTriad.domain.game

import io.yumemi.tart.core.Action

sealed interface GameAction : Action {
    data object CompleteSelectFirstPlayer : GameAction
    data class SelectCard(val selectedCardIndexInHands: Int) : GameAction
    data object UnselectCard : GameAction
    data class SelectSquare(val selectedSquare: GameField.Square) : GameAction
    data object CompletePlaceCard : GameAction
    data object CompleteApplyCardPlaceRule : GameAction
}
