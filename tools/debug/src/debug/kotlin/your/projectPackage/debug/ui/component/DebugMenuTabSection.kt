package your.projectPackage.debug.ui.component

import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import your.projectPackage.debug.ui.DebugMenuTab
import your.projectPackage.debug.ui.DebugMenuUiAction
import your.projectPackage.debug.ui.DebugMenuUiState
import your.projectPackage.ui.Dispatch
import your.projectPackage.ui.PreviewRoot
import your.projectPackage.ui.error.handleUiEvent

@Composable
internal fun DebugMenuTabSection(
    uiState: DebugMenuUiState,
    dispatch: Dispatch<DebugMenuUiAction>,
    modifier: Modifier = Modifier,
) {
    ScrollableTabRow(
        selectedTabIndex = uiState.selectedTabIndex,
        edgePadding = 0.dp,
        modifier = modifier,
    ) {
        uiState.debugMenuTabs.forEachIndexed { index, debugMenu ->
            Tab(
                selected = uiState.selectedTabIndex == index,
                onClick = handleUiEvent { dispatch(DebugMenuUiAction.SelectTab(index)) },
                text = {
                    Text(debugMenu.tabText)
                },
            )
        }
    }
}

@Preview
@Composable
private fun DebugMenuTabSectionPreview() = PreviewRoot {
    DebugMenuTabSection(
        uiState = DebugMenuUiState(DebugMenuTab.SampleDebugMenuTab),
        dispatch = {},
    )
}
