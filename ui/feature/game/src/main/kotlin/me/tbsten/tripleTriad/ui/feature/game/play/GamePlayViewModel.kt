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
        me = GamePlayer("Test Player"),
        meHands = listOf(
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
        firstPlayer = null,
    )

@HiltViewModel
internal class GamePlayViewModel @Inject constructor(
    applicationErrorStateHolder: ApplicationErrorStateHolder,
    private val randomAutoControlFactory: RandomAutoControlFactory,
) : BaseViewModel<GamePlayUiState, GamePlayUiAction>(applicationErrorStateHolder) {
    private val gameStore = GameStore(
        initialGameState = initialGameStateForTest,
        placeCardRules = listOf(BasicPlaceCardRule),
        selectFirstPlayer = { initialGameStateForTest.me },
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
            uiState.collect {
                if (it is GamePlayUiState.PlacingCard) {
                    delay(500)
                    dispatch(GamePlayUiAction.CompletePlaceCard)
                } else if (it is GamePlayUiState.ApplyingPlaceRule) {
                    delay(1000)
                    dispatch(GamePlayUiAction.CompleteApplyCardPlaceRule)
                }
            }
        }
    }

    override fun dispatch(action: GamePlayUiAction) = when (action) {
        GamePlayUiAction.CompleteSelectingFirstPlayer -> gameStore.dispatch(GameAction.CompleteSelectFirstPlayer)
        is GamePlayUiAction.SelectCard -> gameStore.dispatch(GameAction.SelectCard(action.selectedCardIndexInHands))
        GamePlayUiAction.UnselectCard -> gameStore.dispatch(GameAction.UnselectCard)
        is GamePlayUiAction.SelectSquare -> gameStore.dispatch(GameAction.SelectSquare(action.selectedSquare))
        GamePlayUiAction.CompletePlaceCard -> gameStore.dispatch(GameAction.CompletePlaceCard)
        GamePlayUiAction.CompleteApplyCardPlaceRule -> gameStore.dispatch(GameAction.CompleteApplyCardPlaceRule)
    }
}

private fun GameState.toUiState(): GamePlayUiState = when (this) {
    is SelectingFirstPlayer -> GamePlayUiState.SelectingFirstPlayer(
        me = this.me,
        meHands = this.meHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
        firstPlayer = this.firstPlayer,
    )
    is GameState.SelectingCardAndSquare -> {
        val selectedCardIndex = this.selectedCardIndexInHands
        if (selectedCardIndex == null) {
            GamePlayUiState.SelectingCard(
                me = this.me,
                meHands = this.meHands,
                enemy = this.enemy,
                enemyHands = this.enemyHands,
                gameField = this.gameField,
                turnPlayer = this.turnPlayer,
            )
        } else {
            GamePlayUiState.SelectingSquare(
                me = this.me,
                meHands = this.meHands,
                enemy = this.enemy,
                enemyHands = this.enemyHands,
                gameField = this.gameField,
                turnPlayer = this.turnPlayer,
                selectedCardIndexInHands = selectedCardIndex,
            )
        }
    }
    is GameState.PlacingCard -> GamePlayUiState.PlacingCard(
        me = this.me,
        meHands = this.meHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
        turnPlayer = this.turnPlayer,
    )
    is GameState.ApplyingPlaceRule -> GamePlayUiState.ApplyingPlaceRule(
        me = this.me,
        meHands = this.meHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
        turnPlayer = this.turnPlayer,
    )
    is GameState.Finished -> GamePlayUiState.Finished(
        me = this.me,
        meHands = this.meHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
    )
}
