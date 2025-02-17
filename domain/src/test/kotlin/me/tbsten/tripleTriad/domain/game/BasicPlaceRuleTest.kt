package me.tbsten.tripleTriad.domain.game

import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.test.runTest
import me.tbsten.tripleTriad.common.updateIndexed
import me.tbsten.tripleTriad.domain.game.gameRule.BasicPlaceCardRule
import org.junit.Test

private val me = GamePlayer("test player")
private val enemy = GamePlayer("test enemy")

private val placeCardRules = listOf(BasicPlaceCardRule)
private const val SelectedSquareIndex = 4

private val testCases = mapOf(
    run {
        val prevState = GameState.SelectingSquare(
            me = me,
            meHands = listOf(allAceCard()),
            enemy = enemy,
            enemyHands = listOf(allAceCard()),
            gameField = GameField(
                GameField.Square.Empty(0, 0),
                GameField.Square.PlacedCard(
                    1,
                    0,
                    enemy,
                    GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
                ),
                GameField.Square.Empty(2, 0),
                GameField.Square.PlacedCard(
                    0,
                    1,
                    enemy,
                    GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
                ),
                GameField.Square.Empty(1, 1),
                GameField.Square.PlacedCard(
                    2,
                    1,
                    enemy,
                    GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
                ),
                GameField.Square.Empty(0, 2),
                GameField.Square.PlacedCard(
                    1,
                    2,
                    enemy,
                    GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
                ),
                GameField.Square.Empty(2, 2),
            ),
            turnPlayer = me,
            selectedCardIndexInHands = 0,
        )

        prevState to GameState.ApplyingPlaceRule(
            me = prevState.me,
            meHands = prevState.meHands.let { it - it[prevState.selectedCardIndexInHands] },
            enemy = prevState.enemy,
            enemyHands = prevState.enemyHands,
            gameField =
            GameField(
                prevState.gameField
                    .updateIndexed(SelectedSquareIndex) {
                        GameField.Square.PlacedCard(
                            x = it.x,
                            y = it.y,
                            owner = prevState.turnPlayer,
                            placedCard = prevState.selectedCard,
                        )
                    }.updateIndexed(1) {
                        assertIs<GameField.Square.PlacedCard>(it)
                        GameField.Square.PlacedCard(
                            x = it.x,
                            y = it.y,
                            owner = prevState.turnPlayer,
                            placedCard = it.placedCard,
                        )
                    }.updateIndexed(3) {
                        assertIs<GameField.Square.PlacedCard>(it)
                        GameField.Square.PlacedCard(
                            x = it.x,
                            y = it.y,
                            owner = prevState.turnPlayer,
                            placedCard = it.placedCard,
                        )
                    }.updateIndexed(5) {
                        assertIs<GameField.Square.PlacedCard>(it)
                        GameField.Square.PlacedCard(
                            x = it.x,
                            y = it.y,
                            owner = prevState.turnPlayer,
                            placedCard = it.placedCard,
                        )
                    }.updateIndexed(7) {
                        assertIs<GameField.Square.PlacedCard>(it)
                        GameField.Square.PlacedCard(
                            x = it.x,
                            y = it.y,
                            owner = prevState.turnPlayer,
                            placedCard = it.placedCard,
                        )
                    },
            ),
            turnPlayer = prevState.me,
            moveCardData = MoveCardData(
                selectedCardIndexInHands = 0,
                selectedCard = prevState.selectedCard,
                selectedSquare = prevState.gameField.squares[SelectedSquareIndex],
                placeBy = me,
            ),
            applyingPlaceRule = placeCardRules[0],
            applyingPlaceRules = placeCardRules - placeCardRules[0],
        )
    },
    run {
        val prevState = GameState.SelectingSquare(
            me = me,
            meHands = listOf(GameCard(CardNumber(2), CardNumber(2), CardNumber(2), CardNumber(2))),
            enemy = enemy,
            enemyHands = listOf(allAceCard()),
            gameField = GameField(
                GameField.Square.Empty(0, 0),
                GameField.Square.PlacedCard(
                    1,
                    0,
                    enemy,
                    GameCard(CardNumber(5), CardNumber(5), CardNumber(5), CardNumber(5)),
                ),
                GameField.Square.Empty(2, 0),
                GameField.Square.PlacedCard(
                    0,
                    1,
                    enemy,
                    GameCard(CardNumber(5), CardNumber(5), CardNumber(5), CardNumber(5)),
                ),
                GameField.Square.Empty(1, 1),
                GameField.Square.PlacedCard(
                    2,
                    1,
                    enemy,
                    GameCard(CardNumber(5), CardNumber(5), CardNumber(5), CardNumber(5)),
                ),
                GameField.Square.Empty(0, 2),
                GameField.Square.PlacedCard(
                    1,
                    2,
                    enemy,
                    GameCard(CardNumber(5), CardNumber(5), CardNumber(5), CardNumber(5)),
                ),
                GameField.Square.Empty(2, 2),
            ),
            turnPlayer = me,
            selectedCardIndexInHands = 0,
        )
        prevState to GameState.ApplyingPlaceRule(
            me = prevState.me,
            meHands = listOf(),
            enemy = prevState.enemy,
            enemyHands = prevState.enemyHands,
            gameField = GameField(
                prevState.gameField.updateIndexed(SelectedSquareIndex) {
                    GameField.Square.PlacedCard(
                        x = it.x,
                        y = it.y,
                        owner = prevState.turnPlayer,
                        placedCard = prevState.selectedCard,
                    )
                },
            ),
            turnPlayer = me,
            moveCardData = MoveCardData(
                selectedCardIndexInHands = 0,
                selectedCard = prevState.selectedCard,
                selectedSquare = prevState.gameField.squares[SelectedSquareIndex],
                placeBy = me,
            ),
            applyingPlaceRule = placeCardRules[0],
            applyingPlaceRules = placeCardRules.filterIndexed { index, _ -> index != 0 },
        )
    },
)

@Suppress("LongMethod")
class BasicPlaceRuleTest {
    @Test
    fun testBasicPlaceRule() = runTest {
        testCases.forEach { (prevState, expectedNextState) ->
            val action = GameAction.SelectSquare(prevState.gameField.squares[SelectedSquareIndex])
            val newState = gameReducer(
                placeCardRules = placeCardRules,
                state = prevState,
                action = action,
            )

            assertEquals(
                expectedNextState,
                newState,
            )
        }
    }
}
