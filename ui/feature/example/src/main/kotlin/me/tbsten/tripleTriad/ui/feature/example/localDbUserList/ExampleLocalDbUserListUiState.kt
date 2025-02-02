package me.tbsten.tripleTriad.ui.feature.example.localDbUserList

import me.tbsten.tripleTriad.domain.example.user.User

internal sealed interface ExampleLocalDbUserListUiState {
    data object InitialLoading : ExampleLocalDbUserListUiState
    data class Success(
        val users: List<User>,
    ) : ExampleLocalDbUserListUiState
}
