package me.tbsten.tripleTriad.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.tbsten.tripleTriad.ui.PreviewRoot

@Composable
fun AppButton(onClick: () -> Unit, modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Button(
        onClick = onClick,
        content = content,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun AppButtonPreview() = PreviewRoot {
    AppButton(onClick = {}) {
        Text("AppButtonPreview")
    }
}
