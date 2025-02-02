package me.tbsten.tripleTriad.domain.example.user

import javax.inject.Inject

internal class CreateUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : CreateUserUseCase {
    override suspend fun execute(user: User) {
        userRepository.createUser(user)
    }
}
