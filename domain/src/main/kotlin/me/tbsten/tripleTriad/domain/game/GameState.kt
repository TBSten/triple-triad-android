package me.tbsten.tripleTriad.domain.game

import arrow.optics.optics
import io.yumemi.tart.core.State
import me.tbsten.tripleTriad.domain.game.gameRule.PlaceCardRule

sealed interface GameState : State {
    val me: GamePlayer
    val meHands: List<GameCard>

    val enemy: GamePlayer
    val enemyHands: List<GameCard>

    val gameField: GameField

    fun handsOf(player: GamePlayer) = when (player) {
        this.me -> meHands
        this.enemy -> enemyHands
        else -> throw throw GameException.IllegalPlayer("$player")
    }

    @optics
    data class SelectingFirstPlayer(
        override val me: GamePlayer,
        override val meHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField = GameField.emptyAll(),
    ) : GameState {
        companion object;
    }

    @optics
    data class SelectingCard(
        override val me: GamePlayer,
        override val meHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
    ) : GameState,
        WithTurnPlayerState {
        companion object;
    }

    @optics
    data class SelectingSquare(
        override val me: GamePlayer,
        override val meHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
        val selectedCardIndexInHands: Int,
    ) : GameState,
        WithTurnPlayerState {
        val selectedCard get() = turnPlayerHands[selectedCardIndexInHands]

        companion object;
    }

    @optics
    data class ApplyingPlaceRule(
        override val me: GamePlayer,
        override val meHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
        val moveCardData: MoveCardData,
        val applyingPlaceRule: PlaceCardRule,
        internal val applyingPlaceRules: List<PlaceCardRule>,
    ) : GameState,
        WithTurnPlayerState {
        companion object;
    }

    @optics
    data class Finished(
        override val me: GamePlayer,
        override val meHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
    ) : GameState,
        WithTurnPlayerState {
        companion object;
    }
}

typealias InitialGameState = GameState.SelectingFirstPlayer

typealias TurnFirstState = GameState.SelectingCard

sealed interface WithTurnPlayerState : GameState {
    val turnPlayer: GamePlayer
    val turnPlayerHands: Hands
        get() = when (turnPlayer) {
            me -> meHands
            enemy -> enemyHands
            else -> throw GameException.IllegalPlayer("ターンプレイヤー")
        }
    val nextTurnPlayer: GamePlayer
        get() = when (turnPlayer) {
            me -> enemy
            enemy -> me
            else -> throw GameException.IllegalPlayer("ターンプレイヤー")
        }
}

@optics
data class MoveCardData(
    internal val selectedCardIndexInHands: Int,
    val selectedCard: GameCard,
    val selectedSquare: GameField.Square,
    val placeBy: GamePlayer,
) {
    companion object;
}
