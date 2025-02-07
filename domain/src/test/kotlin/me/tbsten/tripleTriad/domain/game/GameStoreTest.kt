package me.tbsten.tripleTriad.domain.game

import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GameStoreTest {
    @Test
    fun testFinishGame() = runTest {
        val prevState = run {
            val player = GamePlayer("test player")
            val enemy = GamePlayer("test enemy")
            GameState.SelectingSquare(
                player = player,
                playerHands = listOf(allAceCard()),
                enemy = enemy,
                enemyHands = listOf(allAceCard()),
                gameField = GameField(
                    squares = listOf(
                        GameField.Square.Empty(0, 0),
                        GameField.Square.PlacedCard(1, 0, enemy, allAceCard()),
                        GameField.Square.PlacedCard(2, 0, player, allAceCard()),
                        GameField.Square.PlacedCard(0, 1, enemy, allAceCard()),
                        GameField.Square.PlacedCard(1, 1, player, allAceCard()),
                        GameField.Square.PlacedCard(2, 1, enemy, allAceCard()),
                        GameField.Square.PlacedCard(0, 2, player, allAceCard()),
                        GameField.Square.PlacedCard(1, 2, enemy, allAceCard()),
                        GameField.Square.PlacedCard(2, 2, player, allAceCard()),
                    ),
                ),
                turnPlayer = player,
                selectedCardIndexInHands = 0,
            )
        }
        val action = GameAction.SelectSquare(prevState.gameField.squares[0])
        val newState = gameReducer(
            placeCardRules = listOf(),
            state = prevState,
            action = action,
        )

        assertEquals(
            GameState.Finished(
                player = prevState.player,
                playerHands = emptyList(),
                enemy = prevState.enemy,
                enemyHands = listOf(allAceCard()),
                gameField = GameField(
                    squares = listOf(
                        GameField.Square.PlacedCard(0, 0, prevState.player, allAceCard()),
                        GameField.Square.PlacedCard(1, 0, prevState.enemy, allAceCard()),
                        GameField.Square.PlacedCard(2, 0, prevState.player, allAceCard()),
                        GameField.Square.PlacedCard(0, 1, prevState.enemy, allAceCard()),
                        GameField.Square.PlacedCard(1, 1, prevState.player, allAceCard()),
                        GameField.Square.PlacedCard(2, 1, prevState.enemy, allAceCard()),
                        GameField.Square.PlacedCard(0, 2, prevState.player, allAceCard()),
                        GameField.Square.PlacedCard(1, 2, prevState.enemy, allAceCard()),
                        GameField.Square.PlacedCard(2, 2, prevState.player, allAceCard()),
                    ),
                ),
                turnPlayer = prevState.player,
            ),
            newState,
        )
    }
}
