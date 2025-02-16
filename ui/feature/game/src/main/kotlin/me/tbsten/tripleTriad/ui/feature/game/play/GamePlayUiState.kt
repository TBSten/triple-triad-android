package me.tbsten.tripleTriad.ui.feature.game.play

import me.tbsten.tripleTriad.domain.game.GameException
import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.domain.game.GamePlayer
import me.tbsten.tripleTriad.domain.game.Hands
import me.tbsten.tripleTriad.ui.feature.game.play.model.Point

internal sealed interface GamePlayUiState {
    val player: GamePlayer
    val playerHands: Hands
    val playerPoint: Point
        get() = Point(
            this.playerHands.size +
                this.gameField.filter { it is GameField.Square.PlacedCard && it.owner == player }.size,
        )

    val enemy: GamePlayer
    val enemyHands: Hands
    val enemyPoint: Point
        get() = Point(
            this.enemyHands.size +
                this.gameField.filter { it is GameField.Square.PlacedCard && it.owner == enemy }.size,
        )

    val gameField: GameField

    val playerSelectedCardIndexInHand: Int?
        get() = null
    val enemySelectedCardIndexInHand: Int?
        get() = null
    val isSquareCardClickable: Boolean
        get() = false
    val isHandsCardClickable: Boolean
        get() = false

    data class SelectingFirstPlayer(
        override val player: GamePlayer,
        override val playerHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
    ) : GamePlayUiState

    data class SelectingCard(
        override val player: GamePlayer,
        override val playerHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
    ) : GamePlayUiState,
        WithTurnPlayer {
        override val isHandsCardClickable: Boolean = turnPlayer == player
    }

    data class SelectingSquare(
        override val player: GamePlayer,
        override val playerHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
        val selectedCardIndexInHand: Int,
    ) : GamePlayUiState,
        WithTurnPlayer {
        override val playerSelectedCardIndexInHand: Int? = if (turnPlayer == player) selectedCardIndexInHand else null
        override val enemySelectedCardIndexInHand: Int? = if (turnPlayer == enemy) selectedCardIndexInHand else null
        override val isSquareCardClickable: Boolean = turnPlayer == player
        override val isHandsCardClickable: Boolean = turnPlayer == player
    }

    data class ApplyingPlaceRule(
        override val player: GamePlayer,
        override val playerHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
    ) : GamePlayUiState,
        WithTurnPlayer

    data class Finished(
        override val player: GamePlayer,
        override val playerHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
    ) : GamePlayUiState
}

internal sealed interface WithTurnPlayer : GamePlayUiState {
    val turnPlayer: GamePlayer
    val turnPlayerHands: Hands
        get() = when (turnPlayer) {
            player -> playerHands
            enemy -> enemyHands
            else -> throw GameException.IllegalPlayer("ターンプレイヤー")
        }
    val nextTurnPlayer: GamePlayer
        get() = when (turnPlayer) {
            player -> enemy
            enemy -> player
            else -> throw GameException.IllegalPlayer("ターンプレイヤー")
        }
}
