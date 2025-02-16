package me.tbsten.tripleTriad.domain.game

import io.yumemi.tart.core.Action

sealed interface GameAction : Action {
    data class SelectCard(val selectedCardIndexInHand: Int) : GameAction
    data object UnselectCard : GameAction
    data class SelectSquare(val selectedSquare: GameField.Square) : GameAction
    data object CompleteApplyCardPlaceRule : GameAction
}
