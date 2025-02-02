package your.projectPackage.ui.feature.example.localDbUserList

import your.projectPackage.domain.example.user.User

internal sealed interface ExampleLocalDbUserListUiState {
    data object InitialLoading : ExampleLocalDbUserListUiState
    data class Success(
        val users: List<User>,
    ) : ExampleLocalDbUserListUiState
}
