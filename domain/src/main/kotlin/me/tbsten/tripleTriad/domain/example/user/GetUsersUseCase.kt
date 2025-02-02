package me.tbsten.tripleTriad.domain.example.user

interface GetUsersUseCase {
    suspend fun execute(): List<User>
}
