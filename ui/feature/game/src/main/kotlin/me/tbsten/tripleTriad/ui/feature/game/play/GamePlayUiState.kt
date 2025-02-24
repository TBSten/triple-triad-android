package me.tbsten.tripleTriad.ui.feature.game.play

import me.tbsten.tripleTriad.domain.game.GameException
import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.domain.game.GamePlayer
import me.tbsten.tripleTriad.domain.game.Hands
import me.tbsten.tripleTriad.ui.feature.game.play.model.Point

internal sealed interface GamePlayUiState {
    val me: GamePlayer
    val meHands: Hands
    val mePoint: Point
        get() = Point(
            this.meHands.size +
                this.gameField.filter { it is GameField.Square.PlacedCard && it.owner == me }.size,
        )

    val enemy: GamePlayer
    val enemyHands: Hands
    val enemyPoint: Point
        get() = Point(
            this.enemyHands.size +
                this.gameField.filter { it is GameField.Square.PlacedCard && it.owner == enemy }.size,
        )

    val gameField: GameField

    val meSelectedCardIndexInHands: Int?
        get() = null
    val enemySelectedCardIndexInHands: Int?
        get() = null
    val isSquareCardClickable: Boolean
        get() = false
    val isHandsCardClickable: Boolean
        get() = false

    data class SelectingFirstPlayer(
        override val me: GamePlayer,
        override val meHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        val firstPlayer: GamePlayer?,
    ) : GamePlayUiState

    data class SelectingCard(
        override val me: GamePlayer,
        override val meHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
    ) : GamePlayUiState,
        WithTurnPlayer {
        override val isHandsCardClickable: Boolean = turnPlayer == me
    }

    data class SelectingSquare(
        override val me: GamePlayer,
        override val meHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
        val selectedCardIndexInHands: Int,
    ) : GamePlayUiState,
        WithTurnPlayer {
        override val meSelectedCardIndexInHands: Int? = if (turnPlayer == me) selectedCardIndexInHands else null
        override val enemySelectedCardIndexInHands: Int? = if (turnPlayer == enemy) selectedCardIndexInHands else null
        override val isSquareCardClickable: Boolean = turnPlayer == me
        override val isHandsCardClickable: Boolean = turnPlayer == me
    }

    data class PlacingCard(
        override val me: GamePlayer,
        override val meHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
    ) : GamePlayUiState,
        WithTurnPlayer

    data class ApplyingPlaceRule(
        override val me: GamePlayer,
        override val meHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
        override val turnPlayer: GamePlayer,
    ) : GamePlayUiState,
        WithTurnPlayer

    data class Finished(
        override val me: GamePlayer,
        override val meHands: Hands,
        override val enemy: GamePlayer,
        override val enemyHands: Hands,
        override val gameField: GameField,
    ) : GamePlayUiState
}

internal sealed interface WithTurnPlayer : GamePlayUiState {
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
