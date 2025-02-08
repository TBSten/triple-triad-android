package me.tbsten.tripleTriad.domain.game

import arrow.optics.optics

@optics
data class GameCard(
    val top: CardNumber,
    val bottom: CardNumber,
    val left: CardNumber,
    val right: CardNumber,
) {
    override fun toString(): String = "Card(t=$top,b=$bottom,l=$left,r=$right)"

    companion object;
}

sealed interface CardNumber {
    operator fun compareTo(other: CardNumber): Int = when {
        this is Ace && other is Ace -> 0
        this is Ace && other !is Ace -> +1
        this !is Ace && other is Ace -> -1
        else -> {
            (this as Number).number - (other as Number).number
        }
    }

    data class Number(
        val number: Int,
    ) : CardNumber {
        override fun toString(): String = "$number"
    }

    data object Ace : CardNumber {
        override fun toString(): String = "A"
    }

    companion object {
        operator fun invoke(number: Int) = Number(number)
    }
}
