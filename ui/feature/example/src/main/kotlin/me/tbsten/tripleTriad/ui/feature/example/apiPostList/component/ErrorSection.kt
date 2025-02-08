package me.tbsten.tripleTriad.ui.feature.example.apiPostList.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.designSystem.TripleTriadTheme
import me.tbsten.tripleTriad.ui.designSystem.components.Surface
import me.tbsten.tripleTriad.ui.designSystem.components.Text

@Composable
internal fun ErrorSection(
    message: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = TripleTriadTheme.colors.error,
        contentColor = TripleTriadTheme.colors.onError,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(message)
        }
    }
}

@Preview
@Composable
private fun ErrorSectionPreview() = PreviewRoot {
    ErrorSection(
        message = "通信エラー",
    )
}
