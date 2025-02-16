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
import me.tbsten.tripleTriad.domain.game.WithTurnPlayerState

interface AutoControl {
    val gameStore: GameStore
    val controlPlayer: GamePlayer

    suspend fun start() {
        gameStore.state.collect { newState ->
            if (newState is WithTurnPlayerState && newState.turnPlayer == controlPlayer) {
                when (newState) {
                    is GameState.SelectingCard ->
                        selectCard(newState)
                            ?.let {
                                gameStore.dispatch(GameAction.SelectCard(newState.handOf(controlPlayer).indexOf(it)))
                            }
                    is GameState.SelectingSquare ->
                        selectSquare(newState)
                            ?.let {
                                gameStore.dispatch(GameAction.SelectSquare(it))
                            }
                    else -> Unit
                }
            }
        }
    }

    suspend fun selectCard(state: GameState.SelectingCard): GameCard?
    suspend fun selectSquare(state: GameState.SelectingSquare): GameField.Square?
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
    override suspend fun selectCard(state: GameState.SelectingCard): GameCard {
        delay(selectCardDelay())
        return state.handOf(controlPlayer).random()
    }

    override suspend fun selectSquare(state: GameState.SelectingSquare): GameField.Square {
        delay(selectSquareDelay())
        return state.gameField.filterIsInstance<GameField.Square.Empty>().random()
    }
}
