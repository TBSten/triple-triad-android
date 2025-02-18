package me.tbsten.tripleTriad.ui.feature.game.play.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import me.tbsten.tripleTriad.domain.game.CardNumber
import me.tbsten.tripleTriad.domain.game.GameCard
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.component.EdgeText
import me.tbsten.tripleTriad.ui.designSystem.LocalTextStyle
import me.tbsten.tripleTriad.ui.error.handleUiEvent

@Composable
fun CardView(
    card: GameCard,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: CardSize = CardSize.Large,
    isClickable: Boolean = true,
) {
    CardViewContent(
        card = card,
        backgroundColor = backgroundColor,
        onClick = handleUiEvent(onClick),
        size = size,
        isClickable = isClickable,
        modifier = modifier,
    )
}

@Composable
fun CardSizedBox(
    size: CardSize,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content:
    @Composable @UiComposable
    (BoxWithConstraintsScope.() -> Unit) = {},
) {
    BoxWithConstraints(
        modifier = modifier
            .width(size.width)
            .aspectRatio(CardSizeAspectRatio),
        contentAlignment = contentAlignment,
        content = content,
    )
}

@Composable
private fun CardViewContent(
    card: GameCard,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: CardSize = CardSize.Large,
    isClickable: Boolean = true,
) {
    val baseTextStyle = LocalTextStyle.current

    val shape = RoundedCornerShape(size.borderRadius)

    val numbersText = "${card.top}\n${card.left} ${card.right}\n${card.bottom}"

    CardSizedBox(
        size = size,
        modifier = modifier
            .testTag("CardView:${card.id}")
            .clip(shape)
            .then(if (isClickable) Modifier.clickable(onClick = onClick) else Modifier)
            .background(animateColorAsState(backgroundColor).value, shape = shape)
            .border(
                size.borderWidth,
                Brush.verticalGradient(
                    0f to Color(0xFFD2C38B),
                    1f to Color(0xFFA28C3B),
                ),
                shape = shape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        val autoFitFontSize =
            (this.maxWidth.value * size.textStyle.fontSize.value / size.width.value).sp
        val autoFitLineHeight =
            (size.textStyle.lineHeight.value * autoFitFontSize.value / size.textStyle.fontSize.value).sp

        val numbersTextStyle = baseTextStyle
            .merge(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            ).merge(size.textStyle)
            .merge(fontSize = autoFitFontSize, lineHeight = autoFitLineHeight)

        EdgeText(
            text = numbersText,
            style = numbersTextStyle.merge(color = Color.White),
            edgeTextStyle = numbersTextStyle.merge(color = Color.Black),
            edgeWidth = size.textBorderWidth,
            modifier = Modifier.wrapContentSize(),
            overflow = TextOverflow.Visible,
        )
    }
}

private const val CardSizeAspectRatio = 7f / 9f

enum class CardSize(
    val width: Dp,
    val textStyle: TextStyle,
    val textBorderWidth: Dp,
    val borderWidth: Dp,
    val borderRadius: Dp,
) {
    Large(
        140.dp,
        TextStyle(fontSize = 40.sp, lineHeight = 32.sp, letterSpacing = -0.05.em),
        4.dp,
        8.dp,
        12.dp,
    ),
    Medium(
        120.dp,
        TextStyle(fontSize = 34.sp, lineHeight = 24.sp, letterSpacing = -0.05.em),
        3.dp,
        6.dp,
        8.dp,
    ),
    Small(
        90.dp,
        TextStyle(fontSize = 26.sp, lineHeight = 22.sp, letterSpacing = -0.05.em),
        2.dp,
        4.dp,
        6.dp,
    ),
}

internal class GameCardPreviewParameters : PreviewParameterProvider<GameCardPreviewParameters.Params> {

    class Params(
        val card: GameCard,
        val size: CardSize,
    )

    override val values: Sequence<Params> = sequenceOf(
        Params(
            GameCard(
                top = CardNumber(1),
                bottom = CardNumber(2),
                left = CardNumber(3),
                right = CardNumber(4),
            ),
            CardSize.Large,
        ),
        Params(
            GameCard(
                top = CardNumber(1),
                bottom = CardNumber(12),
                left = CardNumber(34),
                right = CardNumber.Ace,
            ),
            CardSize.Large,
        ),
        Params(
            GameCard(
                top = CardNumber(1),
                bottom = CardNumber(2),
                left = CardNumber(3),
                right = CardNumber(4),
            ),
            CardSize.Medium,
        ),
        Params(
            GameCard(
                top = CardNumber(1),
                bottom = CardNumber(12),
                left = CardNumber(34),
                right = CardNumber.Ace,
            ),
            CardSize.Medium,
        ),
        Params(
            GameCard(
                top = CardNumber(1),
                bottom = CardNumber(2),
                left = CardNumber(3),
                right = CardNumber(4),
            ),
            CardSize.Small,
        ),
        Params(
            GameCard(
                top = CardNumber(1),
                bottom = CardNumber(12),
                left = CardNumber(34),
                right = CardNumber.Ace,
            ),
            CardSize.Small,
        ),
    )
}

@Preview(group = "default width")
@Preview(widthDp = 120, group = "120 width")
@Preview(widthDp = 100, group = "100 width")
@Composable
private fun CardViewPreview(
    @PreviewParameter(GameCardPreviewParameters::class)
    params: GameCardPreviewParameters.Params,
) = PreviewRoot {
    CardView(
        card = params.card,
        backgroundColor = Color(0xFF618AB9),
        size = params.size,
        modifier = Modifier.padding(16.dp),
        onClick = {},
    )
}
