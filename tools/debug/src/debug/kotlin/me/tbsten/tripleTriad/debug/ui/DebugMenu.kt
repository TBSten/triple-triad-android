package me.tbsten.tripleTriad.debug.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import me.tbsten.tripleTriad.debug.ui.component.DebugMenuTabSection
import me.tbsten.tripleTriad.ui.Dispatch
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.consumeViewModel
import me.tbsten.tripleTriad.ui.error.SafeLaunchedEffect

@Composable
internal fun DebugMenu(
    modifier: Modifier = Modifier,
    viewModel: DebugMenuViewModel = hiltViewModel(),
) {
    val (uiState, dispatch) = consumeViewModel(viewModel)

    DebugMenu(
        uiState = uiState,
        dispatch = dispatch,
        modifier = modifier,
    )
}

@Composable
private fun DebugMenu(
    uiState: DebugMenuUiState,
    dispatch: Dispatch<DebugMenuUiAction>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { uiState.debugMenuTabs.size }

    SafeLaunchedEffect(pagerState.targetPage) {
        dispatch(DebugMenuUiAction.SelectTab(pagerState.targetPage))
    }

    SafeLaunchedEffect(uiState.selectedTabIndex) {
        pagerState.animateScrollToPage(uiState.selectedTabIndex)
    }

    Surface(Modifier.windowInsetsPadding(WindowInsets.systemBars)) {
        Column(modifier = modifier) {
            DebugMenuTabSection(
                uiState = uiState,
                dispatch = dispatch,
            )

            HorizontalPager(
                state = pagerState,
            ) { page ->
                val tab = uiState.debugMenuTabs[page]

                Box(Modifier.fillMaxSize()) {
                    tab.Content()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DebugMenuPreview() = PreviewRoot {
    DebugMenu(
        uiState = DebugMenuUiState(DebugMenuTab.SampleDebugMenuTab),
        dispatch = { },
    )
}
