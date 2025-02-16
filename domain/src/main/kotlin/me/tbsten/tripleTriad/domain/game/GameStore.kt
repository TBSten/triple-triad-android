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
): GameState {
    return when (action) {
        is GameAction.SelectCard -> when (state) {
            is GameState.SelectingCard ->
                state.toSelectingSquareState(action.selectedCardIndexInHand)
            is GameState.SelectingSquare ->
                state.toSelectingSquareState(action.selectedCardIndexInHand)
            else -> throw GameException.IllegalStateTransition("state: $state action:SelectCard")
        }
        is GameAction.UnselectCard -> {
            check(state is WithTurnPlayerState)
            state.toSelectingCardState()
        }
        is GameAction.SelectSquare,
        GameAction.CompleteApplyCardPlaceRule,
        -> {
            when {
                state is GameState.SelectingSquare && action is GameAction.SelectSquare -> {
                    val movedState = state.movePlacedCardFromHandsToField(moveCardDataOf(state, action))
                    if (placeCardRules.isEmpty()) return movedState.toNextTurnOrFinish()
                    movedState.toApplyingPlaceRuleState(action.selectedSquare, placeCardRules, placeCardRules[0])
                }
                state is GameState.ApplyingPlaceRule && action is GameAction.CompleteApplyCardPlaceRule ->
                    state
                else -> throw GameException.IllegalStateTransition("state:$state action:$action")
            }.let { applyingPlaceRuleState ->
                // 配置ルール適用フェーズ
                val rules = applyingPlaceRuleState.applyingPlaceRules.toMutableList()
                val placeRule = rules.removeFirstOrNull()
                if (placeRule == null) {
                    // 適用する配置ルールがないので次のターンへ
                    applyingPlaceRuleState.toNextTurnOrFinish()
                } else {
                    // 配置ルールを適用
                    val newGameField =
                        placeRule.afterPlaceCard(
                            applyingPlaceRuleState.gameField,
                            applyingPlaceRuleState.moveCardData,
                        )
                    applyingPlaceRuleState
                        .let { GameState.ApplyingPlaceRule.gameField.modify(it) { newGameField } }
                        .let { GameState.ApplyingPlaceRule.applyingPlaceRules.modify(it) { rules.toList() } }
                }
            }
        }
        is GameAction.CompleteApplyCardPlaceRule -> {
            check(state is GameState.ApplyingPlaceRule) // TODO Replace check to custom exception
            state
        }
    }
}

class GameStore(
    initialGameState: InitialGameState,
    private val selectFirstPlayer: suspend () -> GamePlayer =
        DefaultSelectFirstPlayer(initialGameState.player, initialGameState.enemy),
    private val placeCardRules: List<PlaceCardRule> = listOf(BasicPlaceCardRule),
) : Store.Base<GameState, GameAction, Nothing>(initialGameState) {
    override val middlewares: List<Middleware<GameState, GameAction, Nothing>> = listOf(
        LoggingMiddleware(),
    )

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
): TurnFirstState = GameState.SelectingCard(
    player = this.player,
    playerHands = this.playerHands,
    enemy = this.enemy,
    enemyHands = this.enemyHands,
    gameField = this.gameField,
    turnPlayer = firstTurnPlayer,
)

private fun GameState.SelectingCard.toSelectingSquareState(selectedCardIndexInHand: Int) = GameState.SelectingSquare(
    player = this.player,
    playerHands = this.playerHands,
    enemy = this.enemy,
    enemyHands = this.enemyHands,
    gameField = this.gameField,
    turnPlayer = this.turnPlayer,
    selectedCardIndexInHands = selectedCardIndexInHand,
)

private fun GameState.SelectingSquare.toSelectingSquareState(selectedCardIndexInHand: Int) = GameState.SelectingSquare(
    player = this.player,
    playerHands = this.playerHands,
    enemy = this.enemy,
    enemyHands = this.enemyHands,
    gameField = this.gameField,
    turnPlayer = this.turnPlayer,
    selectedCardIndexInHands = selectedCardIndexInHand,
)

private fun WithTurnPlayerState.toSelectingCardState() = GameState.SelectingCard(
    player = this.player,
    playerHands = this.playerHands,
    enemy = this.enemy,
    enemyHands = this.enemyHands,
    gameField = this.gameField,
    turnPlayer = this.turnPlayer,
)

private fun GameState.SelectingSquare.toApplyingPlaceRuleState(
    selectedSquare: GameField.Square,
    placeRules: List<PlaceCardRule>,
    applyingPlaceRule: PlaceCardRule,
) = GameState.ApplyingPlaceRule(
    player = this.player,
    playerHands = this.playerHands,
    enemy = this.enemy,
    enemyHands = this.enemyHands,
    gameField = this.gameField,
    turnPlayer = this.turnPlayer,
    moveCardData = MoveCardData(
        selectedCardIndexInHands = this.selectedCardIndexInHands,
        selectedCard = (this.gameField.getOrNull(selectedSquare.x, selectedSquare.y) as GameField.Square.PlacedCard)
            .placedCard,
        selectedSquare = selectedSquare,
        placeBy = this.turnPlayer,
    ),
    applyingPlaceRule = applyingPlaceRule,
    applyingPlaceRules = placeRules,
)

private fun moveCardDataOf(
    prevState: GameState.SelectingSquare,
    action: GameAction.SelectSquare,
) = MoveCardData(
    selectedCardIndexInHands = prevState.selectedCardIndexInHands,
    selectedCard = prevState.turnPlayerHands[prevState.selectedCardIndexInHands],
    selectedSquare = action.selectedSquare,
    placeBy = prevState.turnPlayer,
)

private fun GameState.SelectingSquare.movePlacedCardFromHandsToField(
    moveCardData: MoveCardData,
): GameState.SelectingSquare = this
    .let { placingCardState ->
        // 手札から削除
        when (placingCardState.turnPlayer) {
            placingCardState.player ->
                GameState.SelectingSquare.playerHands
                    .modify(placingCardState) { it - it[moveCardData.selectedCardIndexInHands] }
            placingCardState.enemy ->
                GameState.SelectingSquare.enemyHands
                    .modify(placingCardState) { it - it[moveCardData.selectedCardIndexInHands] }
            else -> throw GameException.IllegalPlayer("ターンプレイヤー")
        }
    }.let { placingCardState ->
        // フィールドにカードを配置
        GameState.SelectingSquare.gameField.squares.modify(placingCardState) {
            it.update(moveCardData.selectedSquare) {
                if (it !is GameField.Square.Empty) {
                    throw GameException.AlreadyPlaced()
                } else {
                    it.toPlacedCard(
                        owner = moveCardData.placeBy,
                        placedCard = moveCardData.selectedCard,
                    )
                }
            }
        }
    }

private fun WithTurnPlayerState.toNextTurnOrFinish() = if (gameField.isFill()) {
    GameState.Finished(
        player = this.player,
        playerHands = this.playerHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
        turnPlayer = this.turnPlayer,
    )
} else {
    TurnFirstState(
        player = this.player,
        playerHands = this.playerHands,
        enemy = this.enemy,
        enemyHands = this.enemyHands,
        gameField = this.gameField,
        turnPlayer = this.nextTurnPlayer,
    )
}
