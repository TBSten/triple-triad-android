package me.tbsten.tripleTriad.ui.feature.game.play.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import me.tbsten.tripleTriad.domain.game.GameField
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.designSystem.TripleTriadTheme
import me.tbsten.tripleTriad.ui.feature.game.play.gameFieldsForPreview

@Composable
fun GameFieldView(
    gameField: GameField,
    cardColor: @Composable (GameField.Square.PlacedCard) -> Color,
    isCardClickable: Boolean,
    onCardClick: (GameField.Square.PlacedCard) -> Unit,
    isSquareClickable: Boolean,
    onSquareClick: (GameField.Square) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally),
    ) {
        items(gameField.squares) { square ->
            SquareView(
                square = square,
                cardColor = cardColor,
                isCardClickable = isCardClickable,
                onCardClick = onCardClick,
                isSquareClickable = isSquareClickable,
                onSquareClick = { onSquareClick(square) },
            )
        }
    }
}

private class GameFieldPreviewParameters :
    CollectionPreviewParameterProvider<GameField>(
        gameFieldsForPreview,
    )

@Preview
@Composable
private fun GameFieldViewPreview(
    @PreviewParameter(GameFieldPreviewParameters::class)
    gameField: GameField,
) = PreviewRoot {
    Box(Modifier.size(100.dp))
    val meColor = TripleTriadTheme.colors.me
    val enemyColor = TripleTriadTheme.colors.enemy

    GameFieldView(
        gameField = gameField,
        cardColor = { if ((it.x + it.y * 3) % 2 == 0) meColor else enemyColor },
        isSquareClickable = true,
        onSquareClick = {},
        isCardClickable = true,
        onCardClick = {},
    )
}
