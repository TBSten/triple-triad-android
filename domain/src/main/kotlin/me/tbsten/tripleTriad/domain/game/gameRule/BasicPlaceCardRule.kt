package me.tbsten.tripleTriad.domain.game.gameRule

import me.tbsten.tripleTriad.common.update
import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.domain.game.MoveCardData
import me.tbsten.tripleTriad.domain.game.owner
import me.tbsten.tripleTriad.domain.game.squares

data object BasicPlaceCardRule : PlaceCardRule {
    // TODO 置いたカードの上下左右の数字を比べて必要に応じて owner を更新
    @Suppress("CyclomaticComplexMethod")
    override suspend fun afterPlaceCard(gameField: GameField, moveCardData: MoveCardData): GameField {
        var resultGameField = gameField
        val selectedCard = moveCardData.selectedCard

        gameField
            .topOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y)
            ?.let {
                if (
                    it is GameField.Square.PlacedCard &&
                    it.owner != moveCardData.placeBy &&
                    selectedCard.top >= it.placedCard.bottom
                ) {
                    resultGameField = GameField.squares.modify(resultGameField) { squares ->
                        squares.update(it, GameField.Square.PlacedCard.owner.modify(it) { moveCardData.placeBy })
                    }
                }
            }

        gameField
            .bottomOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y)
            ?.let {
                if (
                    it is GameField.Square.PlacedCard &&
                    it.owner != moveCardData.placeBy &&
                    selectedCard.bottom >= it.placedCard.top
                ) {
                    resultGameField = GameField.squares.modify(resultGameField) { squares ->
                        squares.update(it, GameField.Square.PlacedCard.owner.modify(it) { moveCardData.placeBy })
                    }
                }
            }

        gameField
            .leftOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y)
            ?.let {
                if (
                    it is GameField.Square.PlacedCard &&
                    it.owner != moveCardData.placeBy &&
                    selectedCard.left >= it.placedCard.right
                ) {
                    resultGameField = GameField.squares.modify(resultGameField) { squares ->
                        squares.update(it, GameField.Square.PlacedCard.owner.modify(it) { moveCardData.placeBy })
                    }
                }
            }

        gameField
            .rightOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y)
            ?.let {
                if (
                    it is GameField.Square.PlacedCard &&
                    it.owner != moveCardData.placeBy &&
                    selectedCard.right >= it.placedCard.left
                ) {
                    resultGameField = GameField.squares.modify(resultGameField) { squares ->
                        squares.update(it, GameField.Square.PlacedCard.owner.modify(it) { moveCardData.placeBy })
                    }
                }
            }

        return resultGameField
    }
}
