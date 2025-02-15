package me.tbsten.tripleTriad.ui.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.designSystem.components.Text

/**
 * 暗く黒く する Modifier
 */
fun Modifier.darken(factor: Float = 0.75f) = then(
    Modifier.drawWithContent {
        val colorMatrix = ColorMatrix().apply {
            setToScale(factor, factor, factor, 1f) // RGB の明るさを調整
        }
        val paint = Paint().apply {
            colorFilter = ColorFilter.colorMatrix(colorMatrix)
        }

        with(drawContext.canvas) {
            saveLayer(size.toRect(), paint)
            drawContent()
            restore()
        }
    },
)

@Preview
@Composable
private fun DarkenPreview() = PreviewRoot {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(
            Modifier
                .background(Color.Red)
                .size(80.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("before")
        }

        Box(
            Modifier
                .darken()
                .background(Color.Red)
                .size(80.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("after")
        }
    }
}
