package me.tbsten.tripleTriad.ui.feature.example.counter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.tbsten.tripleTriad.ui.Dispatch
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.consumeViewModel
import me.tbsten.tripleTriad.ui.designSystem.components.Button
import me.tbsten.tripleTriad.ui.designSystem.components.Text

@Composable
internal fun ExampleCounterScreen(
    navigateToUserList: () -> Unit,
    navigateToPostList: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExampleCounterViewModel = hiltViewModel(),
) {
    val (uiState, dispatch) = consumeViewModel(viewModel)

    ExampleCounterScreen(
        uiState = uiState,
        dispatch = dispatch,
        modifier = modifier,
        navigateToUserList = navigateToUserList,
        navigateToPostList = navigateToPostList,
    )
}

@Composable
private fun ExampleCounterScreen(
    uiState: ExampleCounterUiState,
    dispatch: Dispatch<ExampleCounterUiAction>,
    navigateToUserList: () -> Unit,
    navigateToPostList: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars)) {
        Text("count: ${uiState.count}")

        Button(onClick = { dispatch(ExampleCounterUiAction.Refresh) }) {
            Text("refresh")
        }

        Row(Modifier.padding(vertical = 24.dp)) {
            Button(onClick = { navigateToUserList() }) {
                Text("Go to UserList")
            }
            Button(onClick = { navigateToPostList() }) {
                Text("Go to PostList")
            }
        }
    }
}

@Preview
@Composable
private fun ExampleCounterScreenPreview() = PreviewRoot {
    ExampleCounterScreen(
        uiState = ExampleCounterUiState(
            count = 100,
        ),
        dispatch = { },
        navigateToUserList = {},
        navigateToPostList = {},
    )
}
