package your.projectPackage.ui.feature.example.counter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import your.projectPackage.ui.Dispatch
import your.projectPackage.ui.PreviewRoot
import your.projectPackage.ui.component.AppButton
import your.projectPackage.ui.consumeViewModel

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
    Scaffold(modifier = modifier) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("count: ${uiState.count}")

            AppButton(onClick = { dispatch(ExampleCounterUiAction.Refresh) }) {
                Text("refresh")
            }

            Row(Modifier.padding(vertical = 24.dp)) {
                AppButton(onClick = { navigateToUserList() }) {
                    Text("Go to UserList")
                }
                AppButton(onClick = { navigateToPostList() }) {
                    Text("Go to PostList")
                }
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
