package me.tbsten.tripleTriad.domain.game

import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.test.runTest
import me.tbsten.tripleTriad.common.update
import me.tbsten.tripleTriad.domain.game.gameRule.BasicPlaceCardRule
import org.junit.Test

@Suppress("LongMethod")
class GameStoreFlowTest {

    @Test
    fun testBasicRule() = runTest(timeout = 5.seconds) {
        val selectFirstPlayerFlow = MutableSharedFlow<GamePlayer>(replay = 10)
        val selectFirstPlayer = suspend { selectFirstPlayerFlow.first() }

        val placeCardRules = listOf(BasicPlaceCardRule)

        val initialState =
            InitialGameState(
                me = GamePlayer("test player"),
                meHands = List(5) {
                    GameCard(
                        top = CardNumber(1 + it),
                        bottom = CardNumber(2 + it),
                        left = CardNumber(3 + it),
                        right = CardNumber(4 + it),
                    )
                },
                enemy = GamePlayer("test enemy"),
                enemyHands = List(5) {
                    GameCard(
                        top = CardNumber(1 + it),
                        bottom = CardNumber(2 + it),
                        left = CardNumber(3 + it),
                        right = CardNumber(4 + it),
                    )
                },
                gameField = GameField.emptyAll(),
            )

        val store =
            GameStore(
                initialGameState = initialState,
                selectFirstPlayer = selectFirstPlayer,
                placeCardRules = placeCardRules,
                log = false,
            )
        val storeStateFlow = store.state
            .shareIn(backgroundScope, SharingStarted.Eagerly)

        // tests

        var prevState: GameState = storeStateFlow.first().also {
            assertEquals(
                GameState.SelectingFirstPlayer(
                    me = initialState.me,
                    meHands = initialState.meHands,
                    enemy = initialState.enemy,
                    enemyHands = initialState.enemyHands,
                    gameField = initialState.gameField,
                ),
                it,
            )
        }

        selectFirstPlayerFlow.emit(initialState.me)

        prevState =
            storeStateFlow.first().also {
                assertEquals(
                    GameState.SelectingCard(
                        me = prevState.me,
                        meHands = prevState.meHands,
                        enemy = prevState.enemy,
                        enemyHands = prevState.enemyHands,
                        gameField = prevState.gameField,
                        turnPlayer = initialState.me,
                    ),
                    it,
                )
            }

        // カード選択
        val selectedCardIndexInHand = 0
        val selectedCard = prevState.meHands[selectedCardIndexInHand]
        store.dispatch(GameAction.SelectCard(selectedCardIndexInHand))

        prevState =
            storeStateFlow.first().also {
                assertEquals(
                    GameState.SelectingSquare(
                        me = prevState.me,
                        meHands = prevState.meHands,
                        enemy = prevState.enemy,
                        enemyHands = prevState.enemyHands,
                        gameField = prevState.gameField,
                        turnPlayer = initialState.me,
                        selectedCardIndexInHands = selectedCardIndexInHand,
                    ),
                    it,
                )
            }

        // マス選択
        val selectedSquare = prevState.gameField.squares[0]
        store.dispatch(GameAction.SelectSquare(selectedSquare))

        prevState = storeStateFlow.first().also {
            assertEquals(
                GameState.ApplyingPlaceRule(
                    me = prevState.me,
                    meHands = prevState.meHands - selectedCard,
                    enemy = prevState.enemy,
                    enemyHands = prevState.enemyHands,
                    gameField = prevState.gameField.copy(
                        squares = prevState.gameField.squares
                            .update(
                                selectedSquare,
                                GameField.Square.PlacedCard(
                                    x = selectedSquare.x,
                                    y = selectedSquare.y,
                                    owner = initialState.me,
                                    placedCard = selectedCard,
                                ),
                            ),
                    ),
                    turnPlayer = initialState.me,
                    moveCardData = MoveCardData(
                        selectedCardIndexInHands = selectedCardIndexInHand,
                        selectedCard = selectedCard,
                        selectedSquare = selectedSquare,
                        placeBy = initialState.me,
                    ),
                    applyingPlaceRule = placeCardRules[0],
                    applyingPlaceRules = placeCardRules - placeCardRules[0],
                ),
                it,
            )
        }

        // 適用完了 して次のターンになっていること
        store.dispatch(GameAction.CompleteApplyCardPlaceRule)

        prevState = storeStateFlow.first().also {
            assertEquals(
                TurnFirstState(
                    me = initialState.me,
                    meHands = prevState.meHands,
                    enemy = initialState.enemy,
                    enemyHands = prevState.enemyHands,
                    // TODO BasicPlaceRule 適用後の状態になってることを確認すべき
                    gameField = prevState.gameField,
                    turnPlayer = prevState.enemy,
                ),
                it,
            )
        }
    }
}
