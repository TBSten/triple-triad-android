package me.tbsten.tripleTriad.domain.example.post

interface GetPostsUseCase {
    suspend fun execute(): List<Post>
}
