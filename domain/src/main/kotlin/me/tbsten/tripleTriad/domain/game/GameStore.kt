package me.tbsten.tripleTriad.domain.game

import io.yumemi.tart.core.Middleware
import io.yumemi.tart.core.Store
import io.yumemi.tart.logging.LoggingMiddleware
import me.tbsten.tripleTriad.common.update
import me.tbsten.tripleTriad.domain.game.gameRule.BasicPlaceCardRule
import me.tbsten.tripleTriad.domain.game.gameRule.PlaceCardRule

@Suppress("NestedBlockDepth", "CyclomaticComplexMethod")
internal suspend fun gameReducer(
    placeCardRules: List<PlaceCardRule>,
    state: GameState,
    action: GameAction,
): GameState = when (action) {
    is GameAction.SelectCard -> {
        check(state is GameState.SelectingCardAndSquare)
        state
            .copy(selectedCardIndexInHands = action.selectedCardIndexInHands)
            .let { it.toPlacingCardState() ?: it }
    }
    is GameAction.SelectSquare -> {
        check(state is GameState.SelectingCardAndSquare)
        state
            .copy(selectedSquare = action.selectedSquare)
            .let { it.toPlacingCardState() ?: it }
    }
    is GameAction.UnselectCard -> {
        check(state is GameState.SelectingCardAndSquare)
        state
            .copy(selectedCardIndexInHands = null)
    }
    GameAction.CompletePlaceCard -> {
        check(state is GameState.PlacingCard)
        val nextRuleIndex = placeCardRules
            .indexOfFirst { it.shouldApply(state.gameField, state.moveCardData) }
        if (nextRuleIndex == -1) {
            state.toNextTurnOrFinish()
        } else {
            val nextRule = placeCardRules[nextRuleIndex]
            val newField = nextRule.afterPlaceCard(state.gameField, state.moveCardData)
            GameState.ApplyingPlaceRule(
                me = state.me,
                enemy = state.enemy,
                meHands = state.meHands,
                enemyHands = state.enemyHands,
                gameField = newField,
                turnPlayer = state.turnPlayer,
                moveCardData = state.moveCardData,
                applyingPlaceRule = nextRule,
                applyingPlaceRules = placeCardRules.subList(nextRuleIndex + 1, placeCardRules.size),
            )
        }
    }
    is GameAction.CompleteApplyCardPlaceRule -> {
        check(state is GameState.ApplyingPlaceRule)
        val nextRuleIndex = state.applyingPlaceRules
            .indexOfFirst { it.shouldApply(state.gameField, state.moveCardData) }
        if (nextRuleIndex == -1) {
            state.toNextTurnOrFinish()
        } else {
            val nextRule = state.applyingPlaceRules[nextRuleIndex]
            val newField = nextRule.afterPlaceCard(state.gameField, state.moveCardData)
            state.copy(
                gameField = newField,
                applyingPlaceRule = nextRule,
                applyingPlaceRules = state.applyingPlaceRules.subList(nextRuleIndex + 1, state.applyingPlaceRules.size),
            )
        }
    }
}

class GameStore(
    initialGameState: InitialGameState,
    private val selectFirstPlayer: suspend () -> GamePlayer =
        DefaultSelectFirstPlayer(initialGameState.me, initialGameState.enemy),
    private val placeCardRules: List<PlaceCardRule> = listOf(BasicPlaceCardRule),
    private val log: Boolean = true,
) : Store.Base<GameState, GameAction, Nothing>(initialGameState) {
    override val middlewares: List<Middleware<GameState, GameAction, Nothing>> = buildList {
        if (log) add(LoggingMiddleware())
    }

    override suspend fun onDispatch(
        state: GameState,
        action: GameAction,
    ): GameState = gameReducer(
        placeCardRules,
        state,
        action,
    )

    override suspend fun onEnter(
        state: GameState,
    ): GameState = when (state) {
        is GameState.SelectingFirstPlayer -> {
            val firstPlayer = selectFirstPlayer()
            state.toStartFirstTurnState(firstPlayer)
        }
        else -> state
    }
}

private fun GameState.SelectingFirstPlayer.toStartFirstTurnState(
    firstTurnPlayer: GamePlayer,
): TurnFirstState = GameState.SelectingCardAndSquare(
    me = this.me,
    meHands = this.meHands,
    enemy = this.enemy,
    enemyHands = this.enemyHands,
    gameField = this.gameField,
    turnPlayer = firstTurnPlayer,
    selectedCardIndexInHands = null,
    selectedSquare = null,
)

private fun GameState.SelectingCardAndSquare.toPlacingCardState(): GameState.PlacingCard? {
    val moveCardData = moveCardDataOf(prevState = this) ?: return null
    return GameState.PlacingCard(
        me = this.me,
        enemy = this.enemy,
        meHands = this.meHands,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
        turnPlayer = this.turnPlayer,
        moveCardData = moveCardData,
    ).movePlacedCardFromHandsToField(moveCardData = moveCardData)
}

@Suppress("ReturnCount")
private fun moveCardDataOf(
    prevState: GameState.SelectingCardAndSquare,
): MoveCardData? {
    return MoveCardData(
        selectedCardIndexInHands = prevState.selectedCardIndexInHands ?: return null,
        selectedCard = prevState.turnPlayerHands[prevState.selectedCardIndexInHands],
        selectedSquare = prevState.selectedSquare ?: return null,
        placeBy = prevState.turnPlayer,
    )
}

private fun GameState.PlacingCard.movePlacedCardFromHandsToField(
    moveCardData: MoveCardData,
): GameState.PlacingCard = this
    .let { placingCardState ->
        // 手札から削除
        when (placingCardState.turnPlayer) {
            placingCardState.me ->
                placingCardState.let {
                    val meHands = it.meHands
                    it.copy(
                        meHands = meHands.toMutableList()
                            .apply { removeAt(moveCardData.selectedCardIndexInHands) }
                            .toList(),
                    )
                }
            placingCardState.enemy ->
                placingCardState.let {
                    val enemyHands = it.enemyHands
                    it.copy(
                        enemyHands = enemyHands.toMutableList()
                            .apply { removeAt(moveCardData.selectedCardIndexInHands) }
                            .toList(),
                    )
                }
            else -> throw GameException.IllegalPlayer("ターンプレイヤー")
        }
    }.let { placingCardState ->
        // フィールドにカードを配置
        placingCardState.copy(
            gameField = placingCardState.gameField.copy(
                squares = placingCardState.gameField.update(moveCardData.selectedSquare) {
                    when (it) {
                        is GameField.Square.Empty ->
                            it.toPlacedCard(
                                owner = moveCardData.placeBy,
                                placedCard = moveCardData.selectedCard,
                            )
                        is GameField.Square.PlacedCard ->
                            throw GameException.AlreadyPlaced()
                    }
                },
            ),
        )
    }

private fun WithTurnPlayerState.toNextTurnOrFinish() = if (gameField.isFill()) {
    GameState.Finished(
        me = this.me,
        meHands = this.meHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
        turnPlayer = this.turnPlayer,
    )
} else {
    TurnFirstState(
        me = this.me,
        meHands = this.meHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
        turnPlayer = this.nextTurnPlayer,
        selectedCardIndexInHands = null,
        selectedSquare = null,
    )
}
