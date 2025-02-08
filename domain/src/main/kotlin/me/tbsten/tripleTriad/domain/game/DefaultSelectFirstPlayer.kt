package me.tbsten.tripleTriad.domain.game

class DefaultSelectFirstPlayer(private vararg val players: GamePlayer) : suspend () -> GamePlayer {
    override suspend fun invoke(): GamePlayer = players.random()
}
