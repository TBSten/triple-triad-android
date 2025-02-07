package me.tbsten.tripleTriad.domain.game.gameRule

import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.domain.game.GamePlayer
import me.tbsten.tripleTriad.domain.game.MoveCardData

sealed interface GameRule

fun interface SelectFirstPlayerRule : GameRule {
    suspend fun selectFirstPlayer(): GamePlayer
}

sealed interface PlaceCardRule : GameRule {
    suspend fun afterPlaceCard(gameField: GameField, moveCardData: MoveCardData): GameField
}
