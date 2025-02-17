package me.tbsten.tripleTriad.ui.feature.game.play.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import me.tbsten.tripleTriad.domain.game.GameCard
import me.tbsten.tripleTriad.domain.game.Hands
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.designSystem.TripleTriadTheme
import me.tbsten.tripleTriad.ui.designSystem.components.Text
import me.tbsten.tripleTriad.ui.error.handleUiEvent
import me.tbsten.tripleTriad.ui.feature.game.play.enemyHandsForPreview
import me.tbsten.tripleTriad.ui.feature.game.play.playerHandsForPreview
import me.tbsten.tripleTriad.ui.modifier.darken
import me.tbsten.tripleTriad.ui.modifier.thenIf

@Composable
internal fun PlayerHandsView(
    hands: Hands,
    cardBackgroundColor: Color,
    onClick: (GameCard) -> Unit,
    modifier: Modifier = Modifier,
    isClickable: Boolean = true,
    selectedCardIndex: Int? = null,
    cardSize: CardSize = CardSize.Medium,
) {
    val centerIndex = (hands.size - 1f) / 2f

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        hands.forEachIndexed { index, card ->
            val rotation by animateFloatAsState(
                (index - centerIndex) * 10f *
                    if (selectedCardIndex != null) 1.5f else 1f,
            )
            val isSelected = index == selectedCardIndex
            val cardScale by animateFloatAsState(if (isSelected) 1.05f else 1.0f)
            val cardDarkenFactor by animateFloatAsState(
                if (selectedCardIndex != null && isSelected.not()) {
                    0.5f
                } else {
                    1.0f
                },
            )

            CardView(
                card = card,
                backgroundColor = cardBackgroundColor,
                onClick = handleUiEvent { onClick(card) },
                isClickable = (isClickable),
                size = cardSize,
                modifier = Modifier
                    .thenIf(isSelected) { zIndex(2f) }
                    .offset(x = (index - centerIndex) * 20.dp)
                    .scale(cardScale)
                    .graphicsLayer {
                        rotationZ = rotation
                        transformOrigin = TransformOrigin(0.5f, 1.5f)
                    }
                    .darken(cardDarkenFactor),
            )
        }
    }
}

class HandsViewPreviewParameters :
    CollectionPreviewParameterProvider<Pair<Hands, Int?>>(
        (playerHandsForPreview + enemyHandsForPreview)
            .filter { it.isNotEmpty() }
            .flatMap {
                listOf(
                    it to null,
                    it to 0,
                    it to it.size / 2,
                    it to it.size - 1,
                )
            },
    )

@Preview(widthDp = 400)
@Composable
private fun HandsViewPreview(
    @PreviewParameter(HandsViewPreviewParameters::class)
    hands: Pair<Hands, Int?>,
) = PreviewRoot {
    val (hands, defaultSelectedCardIndex) = hands

    var selectedCardIndex by remember { mutableStateOf<Int?>(defaultSelectedCardIndex) }

    Column {
        PlayerHandsView(
            hands = hands,
            cardBackgroundColor = TripleTriadTheme.colors.me,
            onClick = { selectedCardIndex = hands.indexOf(it) },
            selectedCardIndex = selectedCardIndex,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 48.dp),
        )
        Text(
            text = "selectedCardIndex = $selectedCardIndex",
            modifier = Modifier
                .background(Color.Black.copy(0.25f))
                .padding(8.dp),
            color = Color.White,
        )
    }
}
