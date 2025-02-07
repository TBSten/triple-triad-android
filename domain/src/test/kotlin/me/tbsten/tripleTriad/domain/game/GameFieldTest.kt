package me.tbsten.tripleTriad.domain.game

import kotlin.test.assertEquals
import org.junit.Test

class GameFieldTest {
    @Test
    fun initAllEmptyGameField() {
        val emptyGameField = GameField.emptyAll()
        assertEquals(
            GameField(
                squares = listOf(
                    GameField.Square.Empty(0, 0),
                    GameField.Square.Empty(1, 0),
                    GameField.Square.Empty(2, 0),
                    GameField.Square.Empty(0, 1),
                    GameField.Square.Empty(1, 1),
                    GameField.Square.Empty(2, 1),
                    GameField.Square.Empty(0, 2),
                    GameField.Square.Empty(1, 2),
                    GameField.Square.Empty(2, 2),
                ),
            ),
            emptyGameField,
        )
    }
}
