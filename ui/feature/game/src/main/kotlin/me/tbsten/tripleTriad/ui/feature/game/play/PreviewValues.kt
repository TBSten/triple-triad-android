package me.tbsten.tripleTriad.ui.feature.game.play

import me.tbsten.tripleTriad.common.updateIndexed
import me.tbsten.tripleTriad.domain.game.CardNumber
import me.tbsten.tripleTriad.domain.game.GameCard
import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.domain.game.GamePlayer
import me.tbsten.tripleTriad.domain.game.squares

internal val meForPreview = GamePlayer("Test Player")
internal val enemyForPreview = GamePlayer("Test Enemy")

internal val meHandsForPreview = listOf(
    listOf(
        GameCard(CardNumber(6), CardNumber(1), CardNumber(1), CardNumber(1)),
        GameCard(CardNumber(2), CardNumber(1), CardNumber(2), CardNumber(3)),
        GameCard(CardNumber(2), CardNumber(4), CardNumber(2), CardNumber(1)),
        GameCard(CardNumber(2), CardNumber(1), CardNumber(5), CardNumber(1)),
        GameCard(CardNumber(1), CardNumber(2), CardNumber(2), CardNumber(4)),
    ),
    *List(5) {
        List(it) {
            GameCard(CardNumber(it), CardNumber(it), CardNumber(it), CardNumber(it))
        }
    }.toTypedArray(),
)

internal val enemyHandsForPreview = listOf(
    listOf(
        GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
        GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
        GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
        GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
        GameCard(CardNumber(1), CardNumber(1), CardNumber(1), CardNumber(1)),
    ),
    *List(5) {
        List(it) {
            GameCard(CardNumber(it), CardNumber(it), CardNumber(it), CardNumber(it))
        }
    }.toTypedArray(),
)

internal val gameFieldsForPreview = listOf(
    GameField.emptyAll(),
    GameField.squares.modify(GameField.emptyAll()) {
        it.updateIndexed(4) {
            GameField.Square.PlacedCard(
                1,
                1,
                owner = meForPreview,
                placedCard = GameCard(CardNumber(1), CardNumber(2), CardNumber(3), CardNumber.Ace),
            )
        }.updateIndexed(8) {
            GameField.Square.PlacedCard(
                2,
                2,
                owner = enemyForPreview,
                placedCard = GameCard(CardNumber(1), CardNumber(2), CardNumber(3), CardNumber.Ace),
            )
        }
    },
    GameField(
        List(9) {
            val number = if (it < 10) CardNumber(it) else CardNumber.Ace
            GameField.Square.PlacedCard(
                it % 3,
                it / 3,
                owner = if (it % 2 == 0) meForPreview else enemyForPreview,
                placedCard = GameCard(number, number, number, number),
            )
        },
    ),
)
