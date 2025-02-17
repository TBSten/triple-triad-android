package me.tbsten.tripleTriad.ui.feature.game.play

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tbsten.tripleTriad.domain.game.CardNumber
import me.tbsten.tripleTriad.domain.game.GameAction
import me.tbsten.tripleTriad.domain.game.GameCard
import me.tbsten.tripleTriad.domain.game.GamePlayer
import me.tbsten.tripleTriad.domain.game.GameState
import me.tbsten.tripleTriad.domain.game.GameState.SelectingFirstPlayer
import me.tbsten.tripleTriad.domain.game.GameStore
import me.tbsten.tripleTriad.domain.game.autoControl.RandomAutoControlFactory
import me.tbsten.tripleTriad.domain.game.gameRule.BasicPlaceCardRule
import me.tbsten.tripleTriad.error.ApplicationErrorStateHolder
import me.tbsten.tripleTriad.ui.BaseViewModel

private val initialGameStateForTest =
    SelectingFirstPlayer(
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
            GameCard(CardNumber(4), CardNumber(1), CardNumber(1), CardNumber(1)),
            GameCard(CardNumber(1), CardNumber(4), CardNumber(1), CardNumber(1)),
            GameCard(CardNumber(1), CardNumber(1), CardNumber(4), CardNumber(1)),
            GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(4)),
            GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
        ),
    )

@HiltViewModel
internal class GamePlayViewModel @Inject constructor(
    applicationErrorStateHolder: ApplicationErrorStateHolder,
    private val randomAutoControlFactory: RandomAutoControlFactory,
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

    override fun init() {
        viewModelScope.launchSafe {
            randomAutoControlFactory
                .create(gameStore, gameStore.state.value.enemy)
                .start()
        }
        // FIXME dispatch from ui
        viewModelScope.launchSafe {
            gameStore.state.collect {
                if (it is GameState.ApplyingPlaceRule) {
                    delay(1000)
                    dispatch(GamePlayUiAction.CompleteApplyCardPlaceRule)
                }
            }
        }
    }

    override fun dispatch(action: GamePlayUiAction) = when (action) {
        is GamePlayUiAction.SelectCard -> gameStore.dispatch(GameAction.SelectCard(action.selectedCardIndexInHand))
        GamePlayUiAction.UnselectCard -> gameStore.dispatch(GameAction.UnselectCard)
        is GamePlayUiAction.SelectSquare -> gameStore.dispatch(GameAction.SelectSquare(action.selectedSquare))
        GamePlayUiAction.CompleteApplyCardPlaceRule -> gameStore.dispatch(GameAction.CompleteApplyCardPlaceRule)
    }
}

private fun GameState.toUiState(): GamePlayUiState = when (this) {
    is SelectingFirstPlayer -> GamePlayUiState.SelectingFirstPlayer(
        player = this.player,
        playerHands = this.playerHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
    )
    is GameState.SelectingCard -> GamePlayUiState.SelectingCard(
        player = this.player,
        playerHands = this.playerHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
        turnPlayer = this.turnPlayer,
    )
    is GameState.SelectingSquare -> GamePlayUiState.SelectingSquare(
        player = this.player,
        playerHands = this.playerHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
        turnPlayer = this.turnPlayer,
        selectedCardIndexInHand = this.selectedCardIndexInHands,
    )
    is GameState.ApplyingPlaceRule -> GamePlayUiState.ApplyingPlaceRule(
        player = this.player,
        playerHands = this.playerHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
        turnPlayer = this.turnPlayer,
    )
    is GameState.Finished -> GamePlayUiState.Finished(
        player = this.player,
        playerHands = this.playerHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
    )
}
