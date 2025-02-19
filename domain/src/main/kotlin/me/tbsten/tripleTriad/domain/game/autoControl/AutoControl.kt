package me.tbsten.tripleTriad.domain.game.autoControl

import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import me.tbsten.tripleTriad.domain.game.GameAction
import me.tbsten.tripleTriad.domain.game.GameCard
import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.domain.game.GamePlayer
import me.tbsten.tripleTriad.domain.game.GameState
import me.tbsten.tripleTriad.domain.game.GameStore

interface AutoControl {
    val gameStore: GameStore
    val controlPlayer: GamePlayer

    suspend fun start() {
        gameStore.state.collect { newState ->
            if (newState is GameState.SelectingCardAndSquare && newState.turnPlayer == controlPlayer) {
                if (newState.selectedCardIndexInHands == null) {
                    val selectedCard = selectCard(newState) ?: return@collect
                    gameStore.dispatch(GameAction.SelectCard(newState.handsOf(controlPlayer).indexOf(selectedCard)))
                } else if (newState.selectedSquare == null) {
                    val selectedSquare = selectSquare(newState) ?: return@collect
                    gameStore.dispatch(GameAction.SelectSquare(selectedSquare))
                }
            }
        }
    }

    suspend fun selectCard(state: GameState.SelectingCardAndSquare): GameCard?
    suspend fun selectSquare(state: GameState.SelectingCardAndSquare): GameField.Square?
}

class RandomAutoControlFactory @Inject constructor() {
    fun create(
        gameStore: GameStore,
        controlPlayer: GamePlayer,
        selectCardDelay: suspend () -> Duration = { 1.seconds },
        selectSquareDelay: suspend () -> Duration = { 1.seconds },
    ): AutoControl = RandomAutoControl(
        gameStore = gameStore,
        controlPlayer = controlPlayer,
        selectCardDelay = selectCardDelay,
        selectSquareDelay = selectSquareDelay,
    )
}

internal class RandomAutoControl(
    override val gameStore: GameStore,
    override val controlPlayer: GamePlayer,
    private val selectCardDelay: suspend () -> Duration = { 1.seconds },
    private val selectSquareDelay: suspend () -> Duration = { 1.seconds },
) : AutoControl {
    override suspend fun selectCard(state: GameState.SelectingCardAndSquare): GameCard {
        delay(selectCardDelay())
        return state.handsOf(controlPlayer).random()
    }

    override suspend fun selectSquare(state: GameState.SelectingCardAndSquare): GameField.Square {
        delay(selectSquareDelay())
        return state.gameField.filterIsInstance<GameField.Square.Empty>().random()
    }
}
