package me.tbsten.tripleTriad.ui.feature.example.apiPostList

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import me.tbsten.tripleTriad.domain.example.post.Post
import me.tbsten.tripleTriad.domain.example.post.PostId
import me.tbsten.tripleTriad.domain.example.post.UserId
import me.tbsten.tripleTriad.ui.Dispatch
import me.tbsten.tripleTriad.ui.PreviewRoot
import me.tbsten.tripleTriad.ui.ValuesPreviewParameterProvider
import me.tbsten.tripleTriad.ui.component.AppButton
import me.tbsten.tripleTriad.ui.consumeViewModel
import me.tbsten.tripleTriad.ui.error.handleUiEvent
import me.tbsten.tripleTriad.ui.feature.example.apiPostList.component.ErrorSection
import me.tbsten.tripleTriad.ui.feature.example.apiPostList.component.PostList

@Composable
internal fun ExampleApiPostListScreen(
    modifier: Modifier = Modifier,
    viewModel: ExampleApiPostListViewModel = hiltViewModel(),
) {
    val (uiState, dispatch) = consumeViewModel(viewModel)

    ExampleApiPostListScreen(
        uiState = uiState,
        dispatch = dispatch,
        modifier = modifier,
    )
}

@Composable
internal fun ExampleApiPostListScreen(
    uiState: ExampleApiPostListUiState,
    dispatch: Dispatch<ExampleApiPostListUiAction>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
            AppButton(onClick = handleUiEvent { dispatch(ExampleApiPostListUiAction.Refresh) }) {
                Text("Refresh")
            }
            AnimatedVisibility(uiState is ExampleApiPostListUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(IntrinsicSize.Max),
                )
            }
        }

        AnimatedContent(
            uiState,
        ) { uiState ->
            if (uiState is ExampleApiPostListUiState.Error) {
                ErrorSection(
                    message = uiState.message,
                    modifier = Modifier.padding(24.dp),
                )
            }
        }

        AnimatedContent(
            targetState = uiState.posts,
            modifier = Modifier.padding(vertical = 24.dp),
        ) { posts ->
            posts?.let {
                PostList(
                    posts = it,
                )
            }
        }
    }
}

private class ExampleApiPostListScreenPreviewParameters :
    ValuesPreviewParameterProvider<ExampleApiPostListUiState>(
        ExampleApiPostListUiState.Loading(posts = null),
        ExampleApiPostListUiState.Success(posts = emptyList()),
        ExampleApiPostListUiState.Success(
            posts = listOf(
                Post(
                    id = PostId(123),
                    userId = UserId(456),
                    title = "Preview Post 1",
                    body = "This is a Preview Post 1.",
                ),
                Post(
                    id = PostId(123),
                    userId = UserId(456),
                    title = "Preview Post 2".repeat(20),
                    body = "This is a Preview Post 2. ".repeat(100),
                ),
            ),
        ),
        ExampleApiPostListUiState.Loading(
            posts = listOf(
                Post(
                    id = PostId(123),
                    userId = UserId(456),
                    title = "Preview Post 1",
                    body = "This is a Preview Post 1.",
                ),
                Post(
                    id = PostId(123),
                    userId = UserId(456),
                    title = "Preview Post 2".repeat(20),
                    body = "This is a Preview Post 2. ".repeat(100),
                ),
            ),
        ),
        ExampleApiPostListUiState.Error(posts = null, message = "通信エラー"),
        ExampleApiPostListUiState.Error(posts = emptyList(), message = "通信エラー"),
        ExampleApiPostListUiState.Error(
            posts = listOf(
                Post(
                    id = PostId(123),
                    userId = UserId(456),
                    title = "Preview Post 1",
                    body = "This is a Preview Post 1.",
                ),
                Post(
                    id = PostId(123),
                    userId = UserId(456),
                    title = "Preview Post 2".repeat(20),
                    body = "This is a Preview Post 2. ".repeat(100),
                ),
            ),
            message = "通信エラー",
        ),
    )

@Preview(showBackground = true)
@Composable
private fun ExampleApiPostListScreenPreview(
    @PreviewParameter(ExampleApiPostListScreenPreviewParameters::class)
    uiState: ExampleApiPostListUiState,
) = PreviewRoot {
    ExampleApiPostListScreen(
        uiState = uiState,
        dispatch = { },
    )
}
