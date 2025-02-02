package me.tbsten.tripleTriad.domain.example.user

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import me.tbsten.tripleTriad.testing.CoroutineRule

internal class UserTest {
    @get:Rule
    val coroutineRule = CoroutineRule()

    @Test
    fun testGetUsers() = runTest {
        val expectedUsers = listOf(
            User(UserId(123), "test 1"),
            User(UserId(456), "test 2"),
        )
        val userRepository: UserRepository = mockk<UserRepository>().also {
            coEvery { it.getUsers() } returns expectedUsers
        }
        val getUsers = GetUsersUseCaseImpl(userRepository)

        val actualUsers = getUsers.execute()

        assertEquals(expectedUsers, actualUsers)
    }

    @Test
    fun testCreateUser() = runTest {
        val userRepository: UserRepository = mockk<UserRepository>().also {
            coEvery { it.createUser(any()) } returns Unit
        }
        val createUser = CreateUserUseCaseImpl(userRepository)

        createUser.execute(User(UserId(123), "test"))

        coVerify { userRepository.createUser(any()) }
    }

    @Test
    fun testDeleteUser() = runTest {
        val userRepository: UserRepository = mockk<UserRepository>().also {
            coEvery { it.deleteUser(any()) } returns Unit
        }
        val deleteUser = DeleteUserUseCaseImpl(userRepository)

        deleteUser.execute(User(UserId(123), "test"))

        coVerify { userRepository.deleteUser(any()) }
    }
}
