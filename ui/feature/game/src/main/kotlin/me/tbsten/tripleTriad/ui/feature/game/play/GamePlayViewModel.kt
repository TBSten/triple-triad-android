package me.tbsten.tripleTriad.ui.feature.game.play

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tbsten.tripleTriad.domain.game.CardNumber
import me.tbsten.tripleTriad.domain.game.GameCard
import me.tbsten.tripleTriad.domain.game.GamePlayer
import me.tbsten.tripleTriad.domain.game.GameState
import me.tbsten.tripleTriad.domain.game.GameStore
import me.tbsten.tripleTriad.domain.game.InitialGameState
import me.tbsten.tripleTriad.domain.game.gameRule.BasicPlaceCardRule
import me.tbsten.tripleTriad.error.ApplicationErrorStateHolder
import me.tbsten.tripleTriad.ui.BaseViewModel

private val initialGameStateForTest =
    InitialGameState(
        player = GamePlayer("Test Player"),
        playerHands = listOf(
            GameCard(CardNumber(6), CardNumber(1), CardNumber(1), CardNumber(1)),
            GameCard(CardNumber(2), CardNumber(1), CardNumber(2), CardNumber(3)),
            GameCard(CardNumber(2), CardNumber(4), CardNumber(2), CardNumber(1)),
            GameCard(CardNumber(2), CardNumber(1), CardNumber(5), CardNumber(1)),
            GameCard(CardNumber(1), CardNumber(2), CardNumber(2), CardNumber(4)),
        ),
        enemy = GamePlayer("Test Enemy"),
        enemyHands = listOf(
            GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
            GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
            GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
            GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
            GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
        ),
    )

@HiltViewModel
internal class GamePlayViewModel @Inject constructor(
    applicationErrorStateHolder: ApplicationErrorStateHolder,
) : BaseViewModel<GamePlayUiState, GamePlayUiAction>(applicationErrorStateHolder) {
    private val gameStore = GameStore(
        initialGameState = initialGameStateForTest,
        placeCardRules = listOf(BasicPlaceCardRule),
    )

    override val uiState: StateFlow<GamePlayUiState> =
        gameStore.state
            .map(GameState::toUiState)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                gameStore.state.value.toUiState(),
            )

    override fun dispatch(action: GamePlayUiAction) {
        TODO("Not yet implemented")
    }
}

private fun GameState.toUiState(): GamePlayUiState = when (this) {
    is GameState.SelectingFirstPlayer -> GamePlayUiState.SelectingFirstPlayer
    is GameState.SelectingCard -> GamePlayUiState.SelectingCard
    is GameState.SelectingSquare -> GamePlayUiState.SelectingSquare
    is GameState.ApplyingPlaceRule -> GamePlayUiState.ApplyingPlaceRule
    is GameState.Finished -> GamePlayUiState.Finished
}
