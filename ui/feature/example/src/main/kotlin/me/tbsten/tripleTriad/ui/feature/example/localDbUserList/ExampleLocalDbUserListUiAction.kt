package me.tbsten.tripleTriad.ui.feature.example.localDbUserList

import me.tbsten.tripleTriad.domain.example.user.User

sealed interface ExampleLocalDbUserListUiAction {
    data object OnAddUser : ExampleLocalDbUserListUiAction
    data class OnDeleteUser(val user: User) : ExampleLocalDbUserListUiAction
}
