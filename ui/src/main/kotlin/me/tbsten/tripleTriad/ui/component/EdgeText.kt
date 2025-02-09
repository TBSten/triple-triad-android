package me.tbsten.tripleTriad.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.designSystem.LocalTextStyle

@Composable
fun EdgeText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current.merge(color = LocalTextStyle.current.color.reversed()),
    edgeWidth: Dp = 2.dp,
    edgeTextStyle: TextStyle = style.copy(color = style.color.reversed()),
) {
    val measurer = rememberTextMeasurer()
    val textResult = measurer.measure(
        text,
        style = style,
        skipCache = true,
    )
    val edgeResult = measurer.measure(
        text,
        style = style.merge(edgeTextStyle),
        skipCache = true,
    )

    with(LocalDensity.current) {
        Canvas(
            modifier = modifier
                .size(
                    textResult.size.width.toDp() + edgeWidth * 2,
                    textResult.size.height.toDp() + edgeWidth * 2,
                ),
        ) {
            // 縁
            drawText(
                edgeResult,
                topLeft = Offset(edgeWidth.toPx(), edgeWidth.toPx()),
                drawStyle = Stroke(edgeWidth.toPx() * 2, miter = 0f),
                color = edgeTextStyle.color,
            )

            // テキスト
            drawText(
                textResult,
                topLeft = Offset(edgeWidth.toPx(), edgeWidth.toPx()),
                color = style.color,
            )
        }
    }
}

private fun Color.reversed() = Color(
    red = 1f - red,
    green = 1f - green,
    blue = 1f - blue,
    alpha = alpha,
)

@Preview(showBackground = true, backgroundColor = 0xFFBFBFBF)
@Composable
private fun EdgeTextPreview() = PreviewRoot {
    EdgeText(
        text = "EdgeText\nEdgeText",
        style = LocalTextStyle.current.merge(fontSize = 32.sp, lineHeight = 36.sp, color = Color.White),
//        edgeTextStyle = TextStyle(color = Color.Red),
        edgeWidth = 2.dp,
    )
}
