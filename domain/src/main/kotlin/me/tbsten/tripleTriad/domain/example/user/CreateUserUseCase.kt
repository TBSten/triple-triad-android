package me.tbsten.tripleTriad.domain.example.user

interface CreateUserUseCase {
    suspend fun execute(user: User)
}
