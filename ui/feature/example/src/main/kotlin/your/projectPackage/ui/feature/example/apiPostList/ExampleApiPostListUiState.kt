package your.projectPackage.ui.feature.example.apiPostList

import your.projectPackage.domain.example.post.Post

internal sealed interface ExampleApiPostListUiState {
    val posts: List<Post>?

    data class Loading(
        override val posts: List<Post>?,
    ) : ExampleApiPostListUiState

    data class Success(
        override val posts: List<Post>,
    ) : ExampleApiPostListUiState

    data class Error(
        override val posts: List<Post>?,
        val message: String,
    ) : ExampleApiPostListUiState
}
