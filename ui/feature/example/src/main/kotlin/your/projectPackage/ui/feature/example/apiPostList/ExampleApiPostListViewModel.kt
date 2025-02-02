package your.projectPackage.ui.feature.example.apiPostList

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import your.projectPackage.domain.example.post.GetPostsUseCase
import your.projectPackage.error.ApplicationErrorStateHolder
import your.projectPackage.ui.BaseViewModel

@HiltViewModel
internal class ExampleApiPostListViewModel @Inject constructor(
    applicationErrorStateHolder: ApplicationErrorStateHolder,
    private val getPosts: GetPostsUseCase,
) : BaseViewModel<ExampleApiPostListUiState, ExampleApiPostListUiAction>(applicationErrorStateHolder) {
    private val _uiState = MutableStateFlow<ExampleApiPostListUiState>(
        ExampleApiPostListUiState.Loading(
            posts = null,
        ),
    )
    override val uiState: StateFlow<ExampleApiPostListUiState> = _uiState.asStateFlow()

    override fun init() {
        refresh()
    }

    override fun dispatch(action: ExampleApiPostListUiAction) = when (action) {
        is ExampleApiPostListUiAction.Refresh -> refresh()
    }

    private fun refresh() {
        viewModelScope.launchSafe {
            _uiState.update {
                ExampleApiPostListUiState.Loading(
                    posts = it.posts,
                )
            }
            runCatching {
                getPosts.execute()
            }.fold(
                onSuccess = { posts ->
                    _uiState.update {
                        ExampleApiPostListUiState.Success(posts = posts)
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        ExampleApiPostListUiState.Error(
                            posts = it.posts,
                            message = error.message ?: "不明なエラー",
                        )
                    }
                },
            )
        }
    }
}
