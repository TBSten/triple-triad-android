package me.tbsten.tripleTriad.domain.game

import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import me.tbsten.tripleTriad.common.updateIndexed
import org.junit.Test

class GameStoreTest {
    @Test
    fun testFinishGame() = runTest {
        val prevState = run {
            val me = GamePlayer("test player")
            val enemy = GamePlayer("test enemy")
            GameState.SelectingSquare(
                me = me,
                meHands = listOf(allAceCard()),
                enemy = enemy,
                enemyHands = listOf(allAceCard()),
                gameField = GameField(
                    squares = listOf(
                        GameField.Square.Empty(0, 0),
                        GameField.Square.PlacedCard(1, 0, enemy, allAceCard()),
                        GameField.Square.PlacedCard(2, 0, me, allAceCard()),
                        GameField.Square.PlacedCard(0, 1, enemy, allAceCard()),
                        GameField.Square.PlacedCard(1, 1, me, allAceCard()),
                        GameField.Square.PlacedCard(2, 1, enemy, allAceCard()),
                        GameField.Square.PlacedCard(0, 2, me, allAceCard()),
                        GameField.Square.PlacedCard(1, 2, enemy, allAceCard()),
                        GameField.Square.PlacedCard(2, 2, me, allAceCard()),
                    ),
                ),
                turnPlayer = me,
                selectedCardIndexInHands = 0,
            )
        }
        val selectedSquareIndex = 0
        val action = GameAction.SelectSquare(prevState.gameField.squares[selectedSquareIndex])
        val newState = gameReducer(
            placeCardRules = listOf(),
            state = prevState,
            action = action,
        )

        assertEquals(
            GameState.Finished(
                me = prevState.me,
                meHands = emptyList(),
                enemy = prevState.enemy,
                enemyHands = prevState.enemyHands,
                gameField = GameField.squares.modify(prevState.gameField) {
                    it.updateIndexed(selectedSquareIndex) { prevSquare ->
                        GameField.Square.PlacedCard(
                            prevSquare.x,
                            prevSquare.y,
                            prevState.me,
                            prevState.selectedCard,
                        )
                    }
                },
                turnPlayer = prevState.me,
            ),
            newState,
        )
    }
}
