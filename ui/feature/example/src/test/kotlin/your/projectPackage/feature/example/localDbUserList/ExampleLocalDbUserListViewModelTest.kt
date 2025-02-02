package your.projectPackage.feature.example.localDbUserList

import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import your.projectPackage.domain.example.user.GetUsersUseCase
import your.projectPackage.domain.example.user.User
import your.projectPackage.domain.example.user.UserId
import your.projectPackage.error.ApplicationErrorStateHolder
import your.projectPackage.testing.CoroutineRule
import your.projectPackage.ui.feature.example.localDbUserList.ExampleLocalDbUserListUiState
import your.projectPackage.ui.feature.example.localDbUserList.ExampleLocalDbUserListViewModel

internal class ExampleLocalDbUserListViewModelTest {
    @get:Rule
    val coroutineRule = CoroutineRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testUiStateInit() = runTest {
        val userList = listOf(
            User(uid = UserId(123), name = "test 1"),
            User(uid = UserId(456), name = "test 2"),
        )
        val viewModel: ExampleLocalDbUserListViewModel = run {
            val errorStateHolder = ApplicationErrorStateHolder()
            val getUsers: GetUsersUseCase = mockk<GetUsersUseCase>().also {
                coEvery { it.execute() } returns userList
            }
            ExampleLocalDbUserListViewModel(
                errorStateHolder,
                getUsers = getUsers,
                createUser = mockk(),
                deleteUser = mockk(),
            )
        }

        assertEquals(viewModel.uiState.value, ExampleLocalDbUserListUiState.InitialLoading)

        viewModel.init()
        advanceTimeBy(1001)

        val uiStateAfterInit = viewModel.uiState.value
        assertIs<ExampleLocalDbUserListUiState.Success>(uiStateAfterInit)
        assertEquals(uiStateAfterInit.users, userList)
    }
}
