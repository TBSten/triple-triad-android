package me.tbsten.tripleTriad.domain.example.user

interface DeleteUserUseCase {
    suspend fun execute(user: User)
}
