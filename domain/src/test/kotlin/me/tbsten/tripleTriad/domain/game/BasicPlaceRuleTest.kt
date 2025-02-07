package me.tbsten.tripleTriad.domain.game

import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import me.tbsten.tripleTriad.domain.game.gameRule.BasicPlaceCardRule
import org.junit.Test

private val player = GamePlayer("test player")
private val enemy = GamePlayer("test enemy")

private val testCases = mapOf(
    Pair(
        GameState.SelectingSquare(
            player = player,
            playerHands = listOf(allAceCard()),
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
            turnPlayer = player,
            selectedCardIndexInHands = 0,
        ),
        GameState.ApplyingPlaceRule(
            player = player,
            playerHands = listOf(),
            enemy = enemy,
            enemyHands = listOf(allAceCard()),
            gameField = GameField(
                GameField.Square.Empty(0, 0),
                GameField.Square.PlacedCard(
                    1,
                    0,
                    player,
                    GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
                ),
                GameField.Square.Empty(2, 0),
                GameField.Square.PlacedCard(
                    0,
                    1,
                    player,
                    GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
                ),
                GameField.Square.PlacedCard(
                    1,
                    1,
                    player,
                    allAceCard(),
                ),
                GameField.Square.PlacedCard(
                    2,
                    1,
                    player,
                    GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
                ),
                GameField.Square.Empty(0, 2),
                GameField.Square.PlacedCard(
                    1,
                    2,
                    player,
                    GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
                ),
                GameField.Square.Empty(2, 2),
            ),
            turnPlayer = player,
            moveCardData = MoveCardData(
                selectedCardIndexInHands = 0,
                selectedCard = allAceCard(),
                selectedSquare = GameField.Square.Empty(1, 1),
                placeBy = player,
            ),
            applyingPlaceRule = BasicPlaceCardRule,
            applyingPlaceRules = listOf(),
        ),
    ),
    Pair(
        GameState.SelectingSquare(
            player = player,
            playerHands = listOf(GameCard(CardNumber(2), CardNumber(2), CardNumber(2), CardNumber(2))),
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
            turnPlayer = player,
            selectedCardIndexInHands = 0,
        ),
        GameState.ApplyingPlaceRule(
            player = player,
            playerHands = listOf(),
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
                GameField.Square.PlacedCard(
                    1,
                    1,
                    player,
                    GameCard(CardNumber(2), CardNumber(2), CardNumber(2), CardNumber(2)),
                ),
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
            turnPlayer = player,
            moveCardData = MoveCardData(
                selectedCardIndexInHands = 0,
                selectedCard = GameCard(CardNumber(2), CardNumber(2), CardNumber(2), CardNumber(2)),
                selectedSquare = GameField.Square.Empty(1, 1),
                placeBy = player,
            ),
            applyingPlaceRule = BasicPlaceCardRule,
            applyingPlaceRules = listOf(),
        ),
    ),
)

@Suppress("LongMethod")
class BasicPlaceRuleTest {
    @Test
    fun testBasicPlaceRule() = runTest {
        testCases.forEach { (prevState, expectedNextState) ->
            val action = GameAction.SelectSquare(prevState.gameField.squares[4])
            val newState = gameReducer(
                placeCardRules = listOf(BasicPlaceCardRule),
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
