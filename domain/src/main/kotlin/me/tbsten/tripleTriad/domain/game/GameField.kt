package me.tbsten.tripleTriad.domain.game

private const val WIDTH = 3
private const val HEIGHT = 3

data class GameField(
    val squares: List<Square>,
) {
    init {
        // validate
        this.squares.forEachIndexed { index, square ->
            if (square.y * WIDTH + square.x != index) {
                throw GameException.IllegalSquareAccess("index:$index square:$square")
            }
        }
    }

    fun getOrNull(x: Int, y: Int) = squares.singleOrNull { it.x == x && it.y == y }

    fun topOfOrNull(x: Int, y: Int) = this.getOrNull(x, y - 1)
    fun bottomOfOrNull(x: Int, y: Int) = this.getOrNull(x, y + 1)
    fun leftOfOrNull(x: Int, y: Int) = this.getOrNull(x - 1, y)
    fun rightOfOrNull(x: Int, y: Int) = this.getOrNull(x + 1, y)

    constructor(
        vararg squares: Square,
    ) : this(squares = squares.toList())

    sealed interface Square {
        val x: Int
        val y: Int

        data class Empty(
            override val x: Int,
            override val y: Int,
        ) : Square

        data class PlacedCard(
            override val x: Int,
            override val y: Int,
            val owner: GamePlayer,
            val placedCard: GameCard,
        ) : Square
    }

    companion object {
        fun emptyAll() = GameField(
            squares =
            List(WIDTH * HEIGHT) {
                Square.Empty(x = it % HEIGHT, y = it / HEIGHT)
            },
        )
    }
}

fun GameField.isFill() = squares.all { it is GameField.Square.PlacedCard }

fun GameField.Square.Empty.toPlacedCard(owner: GamePlayer, placedCard: GameCard) = GameField.Square.PlacedCard(
    x = this.x,
    y = this.y,
    owner = owner,
    placedCard = placedCard,
)
