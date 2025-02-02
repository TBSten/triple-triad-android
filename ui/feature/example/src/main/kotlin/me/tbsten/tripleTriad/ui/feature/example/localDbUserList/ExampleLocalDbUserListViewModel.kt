package me.tbsten.tripleTriad.ui.feature.example.localDbUserList

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.tbsten.tripleTriad.domain.example.user.CreateUserUseCase
import me.tbsten.tripleTriad.domain.example.user.DeleteUserUseCase
import me.tbsten.tripleTriad.domain.example.user.GetUsersUseCase
import me.tbsten.tripleTriad.domain.example.user.User
import me.tbsten.tripleTriad.domain.example.user.UserId
import me.tbsten.tripleTriad.error.ApplicationErrorStateHolder
import me.tbsten.tripleTriad.ui.BaseViewModel

private const val INITIAL_FETCH_DELAY = 1000L
private val randomRange = 1..1_000_000

@HiltViewModel
internal class ExampleLocalDbUserListViewModel @Inject constructor(
    applicationErrorStateHolder: ApplicationErrorStateHolder,
    private val getUsers: GetUsersUseCase,
    private val createUser: CreateUserUseCase,
    private val deleteUser: DeleteUserUseCase,
) : BaseViewModel<ExampleLocalDbUserListUiState, ExampleLocalDbUserListUiAction>(applicationErrorStateHolder) {
    private val _uiState = MutableStateFlow<ExampleLocalDbUserListUiState>(
        ExampleLocalDbUserListUiState.InitialLoading,
    )
    override val uiState = _uiState.asStateFlow()

    override fun init() {
        viewModelScope.launchSafe {
            delay(INITIAL_FETCH_DELAY)
            refresh()
        }
    }

    override fun dispatch(action: ExampleLocalDbUserListUiAction) {
        viewModelScope.launchSafe {
            when (action) {
                ExampleLocalDbUserListUiAction.OnAddUser -> onAddUser()
                is ExampleLocalDbUserListUiAction.OnDeleteUser -> onDeleteUser(action.user)
            }
        }
    }

    private suspend fun onAddUser() {
        val randomId = Random.nextInt(randomRange)
        createUser.execute(
            User(
                uid = UserId(randomId),
                name = "User-$randomId",
            ),
        )
        refresh()
    }

    private suspend fun onDeleteUser(user: User) {
        deleteUser.execute(user)
        refresh()
    }

    private suspend fun refresh() {
        val users = getUsers.execute()
        _uiState.update {
            ExampleLocalDbUserListUiState.Success(
                users = users,
            )
        }
    }
}
