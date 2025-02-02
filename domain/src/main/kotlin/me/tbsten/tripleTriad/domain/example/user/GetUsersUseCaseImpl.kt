package me.tbsten.tripleTriad.domain.example.user

import javax.inject.Inject

internal class GetUsersUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : GetUsersUseCase {
    override suspend fun execute(): List<User> = userRepository.getUsers()
}
