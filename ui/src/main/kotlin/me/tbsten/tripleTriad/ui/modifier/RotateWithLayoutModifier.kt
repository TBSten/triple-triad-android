package me.tbsten.tripleTriad.ui.modifier

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.designSystem.components.Button
import me.tbsten.tripleTriad.ui.designSystem.components.ButtonVariant
import me.tbsten.tripleTriad.ui.designSystem.components.Text

// FIXME not full implemented
@SuppressLint("ComposeModifierComposed")
fun Modifier.rotateWithLayout(
    degress: Float,
    transformOrigin: TransformOrigin = TransformOrigin.Center,
) = composed {
    var d by remember { mutableStateOf<Offset?>(null) }

    then(
        Modifier
            .drawWithContent {
                drawContent()
                d?.let { d ->
                    drawLine(
                        color = Color.White,
                        start = Offset.Zero,
                        end = d,
                        strokeWidth = 2.dp.toPx(),
                    )
                }
            }
            .graphicsLayer {
                rotationZ = degress
                this.transformOrigin = transformOrigin
            }
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                val (boundingBox, delta) = boundingBoxSize(
                    placeable.width.toDouble(),
                    placeable.height.toDouble(),
                    degress.toDouble(),
                    transformOrigin.pivotFractionX.toDouble(),
                    transformOrigin.pivotFractionY.toDouble(),
                )
                d = delta
                layout(boundingBox.width.toInt(), boundingBox.height.toInt()) {
                    placeable.place(delta.x.toInt(), delta.y.toInt())
                }
            },
    )
}

private fun boundingBoxSize(
    w: Double,
    h: Double,
    degrees: Double,
    pivotX: Double,
    pivotY: Double,
): Pair<Size, Offset> {
    val radians = Math.toRadians(degrees) // 角度をラジアンに変換
    val cosD = cos(radians)
    val sinD = sin(radians)

    // 元の長方形の4つの頂点（左上基準）
    val corners = listOf(
        -pivotX * w to -pivotY * h, // 左上
        (1 - pivotX) * w to -pivotY * h, // 右上
        -pivotX * w to (1 - pivotY) * h, // 左下
        (1 - pivotX) * w to (1 - pivotY) * h, // 右下
    )

    // 回転後の頂点の座標を計算
    val rotatedCorners = corners.map { (x, y) ->
        val newX = x * cosD - y * sinD
        val newY = x * sinD + y * cosD
        newX to newY
    }

    // バウンディングボックスの左上の座標
    val minX = rotatedCorners.minOf { it.first }
    val minY = rotatedCorners.minOf { it.second }
    val maxX = rotatedCorners.maxOf { it.first }
    val maxY = rotatedCorners.maxOf { it.second }

    // 外接長方形のサイズ
    val boundingWidth = maxX - minX
    val boundingHeight = maxY - minY

    // 回転前の左上の点 (-pivotX * w, -pivotY * h) を回転
    val originalTopLeftX = -pivotX * w * cosD + pivotY * h * sinD
    val originalTopLeftY = -pivotX * w * sinD - pivotY * h * cosD

    // 左上からバウンディングボックスの左上までの移動量
    val deltaX = minX - originalTopLeftX
    val deltaY = minY - originalTopLeftY

    return Size(boundingWidth.toFloat(), boundingHeight.toFloat()) to Offset(deltaX.toFloat(), deltaY.toFloat())
}

@Preview
@Composable
private fun RotateWithLayoutPreview() = PreviewRoot {
    var degress by remember { mutableStateOf(30f) }
    val animatedDegress by animateFloatAsState(degress)
    val transformOrigin = TransformOrigin(0f, 0f)

    Column {
        Row {
            Button(onClick = { degress += -15 }, text = "-", variant = ButtonVariant.Ghost)
            Text("$degress")
            Button(onClick = { degress += +15 }, text = "+", variant = ButtonVariant.Ghost)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            Box(
                Modifier
                    // layout bounds
                    .border(4.dp, Color.Green)
                    .graphicsLayer {
                        rotationZ = animatedDegress
                        this.transformOrigin = transformOrigin
                    }
                    // after rotate bounds
                    .background(Color.Red)
                    .size(200.dp),
            ) {
                Text("")
            }

            Box(
                Modifier
                    // layout bounds
                    .border(4.dp, Color.Yellow)
                    .rotateWithLayout(animatedDegress, transformOrigin)
                    // after rotate bounds
                    .background(Color.Blue)
                    .size(100.dp),
            )
        }
    }
}
