package me.tbsten.tripleTriad.ui.feature.example.localDbUserList

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.tbsten.tripleTriad.domain.example.user.User
import me.tbsten.tripleTriad.domain.example.user.UserId
import me.tbsten.tripleTriad.ui.Dispatch
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.ValuesPreviewParameterProvider
import me.tbsten.tripleTriad.ui.component.AppButton
import me.tbsten.tripleTriad.ui.consumeViewModel
import me.tbsten.tripleTriad.ui.error.handleUiEvent
import me.tbsten.tripleTriad.ui.feature.example.localDbUserList.component.UserListItem

@Composable
internal fun ExampleLocalDbUserListScreen(
    modifier: Modifier = Modifier,
    viewModel: ExampleLocalDbUserListViewModel = hiltViewModel(),
) {
    val (uiState, dispatch) = consumeViewModel(viewModel)

    ExampleLocalDbUserListScreen(
        uiState = uiState,
        dispatch = dispatch,
        modifier = modifier,
    )
}

@Composable
private fun ExampleLocalDbUserListScreen(
    uiState: ExampleLocalDbUserListUiState,
    dispatch: Dispatch<ExampleLocalDbUserListUiAction>,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Crossfade(
            if (uiState == ExampleLocalDbUserListUiState.InitialLoading) uiState else null,
            label = "ExampleLocalDbUserListScreen Loading Crossfade",
            modifier = Modifier.padding(innerPadding),
        ) { uiState ->
            uiState?.let {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        Crossfade(
            uiState is ExampleLocalDbUserListUiState.Success,
            label = "ExampleLocalDbUserListScreen Success Crossfade",
            modifier = Modifier.padding(innerPadding),
        ) { isSuccess ->
            if (isSuccess && uiState is ExampleLocalDbUserListUiState.Success) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        if (uiState.users.isEmpty()) {
                            item {
                                Text("ユーザがいません")
                            }
                        } else {
                            items(uiState.users) { user ->
                                UserListItem(
                                    user = user,
                                    onDelete = { dispatch(ExampleLocalDbUserListUiAction.OnDeleteUser(user)) },
                                )
                            }
                        }

                        item {
                            AppButton(
                                modifier = Modifier.padding(top = 32.dp),
                                onClick = handleUiEvent { dispatch(ExampleLocalDbUserListUiAction.OnAddUser) },
                            ) {
                                Text("適当なユーザを追加")
                            }
                        }
                    }
                }
            }
        }
    }
}

private class ExampleLocalDbUserListUiStatePreviewParameterProvider :
    ValuesPreviewParameterProvider<ExampleLocalDbUserListUiState>(
        ExampleLocalDbUserListUiState.InitialLoading,
        ExampleLocalDbUserListUiState.Success(
            users = listOf(),
        ),
        ExampleLocalDbUserListUiState.Success(
            users = listOf(
                User(uid = UserId(123), name = "test 1"),
                User(uid = UserId(456), name = "test 2"),
            ),
        ),
    )

@Preview
@Composable
private fun ExampleLocalDbUserListScreenPreview(
    @PreviewParameter(ExampleLocalDbUserListUiStatePreviewParameterProvider::class)
    uiState: ExampleLocalDbUserListUiState,
) = PreviewRoot {
    ExampleLocalDbUserListScreen(
        uiState = uiState,
        dispatch = { },
    )
}
