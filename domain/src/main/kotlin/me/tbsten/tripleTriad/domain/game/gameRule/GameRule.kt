package me.tbsten.tripleTriad.domain.game.gameRule

import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.domain.game.MoveCardData

sealed interface GameRule

sealed interface PlaceCardRule : GameRule {
    suspend fun shouldApply(gameField: GameField, moveCardData: MoveCardData): Boolean
    suspend fun afterPlaceCard(gameField: GameField, moveCardData: MoveCardData): GameField
}
