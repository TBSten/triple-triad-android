package me.tbsten.tripleTriad.domain.game

import kotlin.uuid.Uuid

class GameCard internal constructor(
    val id: Id = Id(),
    val top: CardNumber,
    val bottom: CardNumber,
    val left: CardNumber,
    val right: CardNumber,
) {
    constructor(
        top: CardNumber,
        bottom: CardNumber,
        left: CardNumber,
        right: CardNumber,
    ) : this(id = Id(), top = top, bottom = bottom, left = left, right = right)

    override fun toString(): String = "Card(t=$top,b=$bottom,l=$left,r=$right)"

    override fun hashCode(): Int = this.id.hashCode()

    override fun equals(other: Any?): Boolean = other is GameCard && this.id == other.id

    @JvmInline
    value class Id(val value: Uuid = Uuid.random())
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
