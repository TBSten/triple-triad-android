package me.tbsten.tripleTriad.domain.game

import io.yumemi.tart.core.State
import me.tbsten.tripleTriad.domain.game.gameRule.PlaceCardRule

sealed interface GameState : State {
    val player: GamePlayer
    val playerHands: List<GameCard>

    val enemy: GamePlayer
    val enemyHands: List<GameCard>

    val gameField: GameField

    data class SelectingFirstPlayer(
        override val player: GamePlayer,
        override val playerHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField = GameField.emptyAll(),
    ) : GameState

    data class SelectingCard(
        override val player: GamePlayer,
        override val playerHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
    ) : GameState,
        WithTurnPlayerState

    data class SelectingSquare(
        override val player: GamePlayer,
        override val playerHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
        val selectedCardIndexInHands: Int,
    ) : GameState,
        WithTurnPlayerState {
        val selectedCard get() = turnPlayerHands[selectedCardIndexInHands]
    }

    data class ApplyingPlaceRule(
        override val player: GamePlayer,
        override val playerHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
        val moveCardData: MoveCardData,
        val applyingPlaceRule: PlaceCardRule,
        internal val applyingPlaceRules: List<PlaceCardRule>,
    ) : GameState,
        WithTurnPlayerState

    data class Finished(
        override val player: GamePlayer,
        override val playerHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
    ) : GameState,
        WithTurnPlayerState
}

typealias InitialGameState = GameState.SelectingFirstPlayer

typealias TurnFirstState = GameState.SelectingCard

interface WithTurnPlayerState : GameState {
    val turnPlayer: GamePlayer
    val turnPlayerHands: Hands
        get() = when (turnPlayer) {
            player -> playerHands
            enemy -> enemyHands
            else -> throw GameException.IllegalTurnPlayer()
        }
    val nextTurnPlayer: GamePlayer
        get() = when (turnPlayer) {
            player -> enemy
            enemy -> player
            else -> throw GameException.IllegalTurnPlayer()
        }
}

data class MoveCardData(
    internal val selectedCardIndexInHands: Int,
    val selectedCard: GameCard,
    val selectedSquare: GameField.Square,
    val placeBy: GamePlayer,
)
