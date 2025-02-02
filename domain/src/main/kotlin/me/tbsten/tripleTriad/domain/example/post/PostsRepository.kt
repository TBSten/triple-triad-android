package me.tbsten.tripleTriad.domain.example.post

interface PostsRepository {
    suspend fun getPosts(): List<Post>
}
