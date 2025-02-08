package me.tbsten.tripleTriad.tools.debug.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.tbsten.tripleTriad.tools.debug.ui.DebugMenuTab
import me.tbsten.tripleTriad.tools.debug.ui.DebugMenuUiAction
import me.tbsten.tripleTriad.tools.debug.ui.DebugMenuUiState
import me.tbsten.tripleTriad.ui.Dispatch
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.designSystem.LocalContentColor
import me.tbsten.tripleTriad.ui.designSystem.TripleTriadTheme
import me.tbsten.tripleTriad.ui.designSystem.components.Text
import me.tbsten.tripleTriad.ui.error.handleUiEvent

@Composable
internal fun DebugMenuTabSection(
    uiState: DebugMenuUiState,
    dispatch: Dispatch<DebugMenuUiAction>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
    ) {
        uiState.debugMenuTabs.forEachIndexed { index, debugMenu ->
            val selected = uiState.selectedTabIndex == index
            val color = if (selected) TripleTriadTheme.colors.primary else LocalContentColor.current

            Column(
                Modifier
                    .clickable(onClick = handleUiEvent { dispatch(DebugMenuUiAction.SelectTab(index)) })
                    .padding(horizontal = 24.dp, vertical = 8.dp),
            ) {
                Text(
                    text = debugMenu.tabText,
                    color = color,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DebugMenuTabSectionPreview() = PreviewRoot {
    DebugMenuTabSection(
        uiState = DebugMenuUiState(DebugMenuTab.SampleDebugMenuTab),
        dispatch = {},
    )
}
