package me.tbsten.tripleTriad.domain.game

import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import me.tbsten.tripleTriad.domain.game.gameRule.BasicPlaceCardRule
import org.junit.Test

private val me = GamePlayer("test player")
private val enemy = GamePlayer("test enemy")

@Suppress("LongMethod")
class BasicPlaceRuleTest {
    @Test
    fun testBasicPlaceRule() = runTest {
        val selectedCard = GameCard(CardNumber(9), CardNumber(9), CardNumber(9), CardNumber(9))
        val topCard = GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1))
        val leftCard = GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1))
        val rightCard = GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1))
        val bottomCard = GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1))
        val beforeField = GameField(
            GameField.Square.Empty(0, 0),
            GameField.Square.PlacedCard(
                1,
                0,
                enemy,
                topCard,
            ),
            GameField.Square.Empty(2, 0),
            GameField.Square.PlacedCard(
                0,
                1,
                enemy,
                leftCard,
            ),
            GameField.Square.PlacedCard(
                1,
                1,
                me,
                selectedCard,
            ),
            GameField.Square.PlacedCard(
                2,
                1,
                enemy,
                rightCard,
            ),
            GameField.Square.Empty(0, 2),
            GameField.Square.PlacedCard(
                1,
                2,
                enemy,
                bottomCard,
            ),
            GameField.Square.Empty(2, 2),
        )
        val afterApplyRule =
            BasicPlaceCardRule.afterPlaceCard(
                gameField = beforeField,
                moveCardData = MoveCardData(
                    selectedCardIndexInHands = -1,
                    selectedCard = selectedCard,
                    selectedSquare = GameField.Square.Empty(1, 1),
                    placeBy = me,
                ),
            )
        assertEquals(
            GameField(
                GameField.Square.Empty(0, 0),
                GameField.Square.PlacedCard(
                    1,
                    0,
                    me,
                    topCard,
                ),
                GameField.Square.Empty(2, 0),
                GameField.Square.PlacedCard(
                    0,
                    1,
                    me,
                    leftCard,
                ),
                GameField.Square.PlacedCard(
                    1,
                    1,
                    me,
                    selectedCard,
                ),
                GameField.Square.PlacedCard(
                    2,
                    1,
                    me,
                    rightCard,
                ),
                GameField.Square.Empty(0, 2),
                GameField.Square.PlacedCard(
                    1,
                    2,
                    me,
                    bottomCard,
                ),
                GameField.Square.Empty(2, 2),
            ),
            afterApplyRule,
        )
    }
}
