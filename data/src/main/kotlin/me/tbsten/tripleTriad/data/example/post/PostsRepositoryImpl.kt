package me.tbsten.tripleTriad.data.example.post

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tbsten.tripleTriad.data.api.generated.api.PostsApi
import me.tbsten.tripleTriad.data.api.generated.model.Post as ApiPost
import me.tbsten.tripleTriad.data.example.bodyOrThrow
import me.tbsten.tripleTriad.domain.example.post.Post
import me.tbsten.tripleTriad.domain.example.post.PostsRepository

internal class PostsRepositoryImpl @Inject constructor(
    private val postsApi: PostsApi,
) : PostsRepository {
    override suspend fun getPosts(): List<Post> = withContext(Dispatchers.IO) {
        postsApi
            .getPosts()
            .bodyOrThrow()
            .map(ApiPost::toDomain)
    }
}

private fun ApiPost.toDomain() = Post(
    id = me.tbsten.tripleTriad.domain.example.post.PostId(id),
    userId = me.tbsten.tripleTriad.domain.example.post.UserId(userId),
    title = title,
    body = body,
)
