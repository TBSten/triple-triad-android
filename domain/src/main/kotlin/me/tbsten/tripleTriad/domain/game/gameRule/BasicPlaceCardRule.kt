package me.tbsten.tripleTriad.domain.game.gameRule

import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.domain.game.MoveCardData

data object BasicPlaceCardRule : PlaceCardRule {
    override suspend fun shouldApply(gameField: GameField, moveCardData: MoveCardData): Boolean = (
        checkReverseTop(gameField, moveCardData) ||
            checkReverseLeft(gameField, moveCardData) ||
            checkReverseRight(gameField, moveCardData) ||
            checkReverseBottom(gameField, moveCardData)
        )

    private fun checkReverseTop(gameField: GameField, moveCardData: MoveCardData): Boolean {
        val selectedSquare = moveCardData.selectedSquare
        val selectedCard = moveCardData.selectedCard
        return (
            (gameField.topOfOrNull(selectedSquare.x, selectedSquare.y) as? GameField.Square.PlacedCard)
                ?.let { it.owner != moveCardData.placeBy && selectedCard.top > it.placedCard.bottom }
            ) ?: false
    }

    private fun checkReverseLeft(gameField: GameField, moveCardData: MoveCardData): Boolean {
        val selectedSquare = moveCardData.selectedSquare
        val selectedCard = moveCardData.selectedCard
        return (
            (gameField.leftOfOrNull(selectedSquare.x, selectedSquare.y) as? GameField.Square.PlacedCard)
                ?.let { it.owner != moveCardData.placeBy && selectedCard.left > it.placedCard.right }
            ) ?: false
    }

    private fun checkReverseRight(gameField: GameField, moveCardData: MoveCardData): Boolean {
        val selectedSquare = moveCardData.selectedSquare
        val selectedCard = moveCardData.selectedCard
        return (
            (gameField.rightOfOrNull(selectedSquare.x, selectedSquare.y) as? GameField.Square.PlacedCard)
                ?.let { it.owner != moveCardData.placeBy && selectedCard.right > it.placedCard.left }
            ) ?: false
    }

    private fun checkReverseBottom(gameField: GameField, moveCardData: MoveCardData): Boolean {
        val selectedSquare = moveCardData.selectedSquare
        val selectedCard = moveCardData.selectedCard
        return (
            (gameField.bottomOfOrNull(selectedSquare.x, selectedSquare.y) as? GameField.Square.PlacedCard)
                ?.let { it.owner != moveCardData.placeBy && selectedCard.bottom > it.placedCard.top }
            ) ?: false
    }

    @Suppress("CyclomaticComplexMethod")
    override suspend fun afterPlaceCard(gameField: GameField, moveCardData: MoveCardData): GameField {
        var resultGameField = gameField
        val selectedCard = moveCardData.selectedCard

        if (checkReverseTop(resultGameField, moveCardData)) {
            val top: GameField.Square.PlacedCard = gameField
                .topOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y) as
                GameField.Square.PlacedCard
            resultGameField = resultGameField.copy(
                squares = resultGameField.squares
                    .map { if (it == top) top.copy(owner = moveCardData.placeBy) else it },
            )
        }

        if (checkReverseLeft(resultGameField, moveCardData)) {
            val left: GameField.Square.PlacedCard = gameField
                .leftOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y) as
                GameField.Square.PlacedCard
            resultGameField = resultGameField.copy(
                squares = resultGameField.squares
                    .map { if (it == left) left.copy(owner = moveCardData.placeBy) else it },
            )
        }

        if (checkReverseRight(resultGameField, moveCardData)) {
            val right: GameField.Square.PlacedCard = gameField
                .rightOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y) as
                GameField.Square.PlacedCard
            resultGameField = resultGameField.copy(
                squares = resultGameField.squares
                    .map { if (it == right) right.copy(owner = moveCardData.placeBy) else it },
            )
        }

        if (checkReverseTop(resultGameField, moveCardData)) {
            val bottom: GameField.Square.PlacedCard = gameField
                .bottomOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y) as
                GameField.Square.PlacedCard
            resultGameField = resultGameField.copy(
                squares = resultGameField.squares
                    .map { if (it == bottom) bottom.copy(owner = moveCardData.placeBy) else it },
            )
        }

//        resultGameField
//            .topOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y)
//            ?.let {
//                if (
//                    it is GameField.Square.PlacedCard &&
//                    it.owner != moveCardData.placeBy &&
//                    selectedCard.top > it.placedCard.bottom
//                ) {
//                    val top: GameField.Square.PlacedCard = it
//                    resultGameField = resultGameField.copy(
//                        squares = resultGameField.squares
//                            .map { if (it == top) top.copy(owner = moveCardData.placeBy) else it },
//                    )
//                }
//            }

//        resultGameField
//            .bottomOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y)
//            ?.let {
//                if (
//                    it is GameField.Square.PlacedCard &&
//                    it.owner != moveCardData.placeBy &&
//                    selectedCard.bottom > it.placedCard.top
//                ) {
//                    resultGameField = GameField.squares.modify(resultGameField) { squares ->
//                        squares.update(it, GameField.Square.PlacedCard.owner.modify(it) { moveCardData.placeBy })
//                    }
//                }
//            }
//
//        resultGameField
//            .leftOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y)
//            ?.let {
//                if (
//                    it is GameField.Square.PlacedCard &&
//                    it.owner != moveCardData.placeBy &&
//                    selectedCard.left > it.placedCard.right
//                ) {
//                    resultGameField = GameField.squares.modify(resultGameField) { squares ->
//                        squares.update(it, GameField.Square.PlacedCard.owner.modify(it) { moveCardData.placeBy })
//                    }
//                }
//            }
//
//        resultGameField
//            .rightOfOrNull(moveCardData.selectedSquare.x, moveCardData.selectedSquare.y)
//            ?.let {
//                if (
//                    it is GameField.Square.PlacedCard &&
//                    it.owner != moveCardData.placeBy &&
//                    selectedCard.right > it.placedCard.left
//                ) {
//                    resultGameField = GameField.squares.modify(resultGameField) { squares ->
//                        squares.update(it, GameField.Square.PlacedCard.owner.modify(it) { moveCardData.placeBy })
//                    }
//                }
//            }

        return resultGameField
    }
}
