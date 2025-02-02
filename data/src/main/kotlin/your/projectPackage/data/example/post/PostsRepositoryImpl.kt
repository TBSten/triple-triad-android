package your.projectPackage.data.example.post

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import your.projectPackage.data.api.generated.api.PostsApi
import your.projectPackage.data.api.generated.model.Post as ApiPost
import your.projectPackage.data.example.bodyOrThrow
import your.projectPackage.domain.example.post.Post
import your.projectPackage.domain.example.post.PostsRepository

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
    id = your.projectPackage.domain.example.post.PostId(id),
    userId = your.projectPackage.domain.example.post.UserId(userId),
    title = title,
    body = body,
)
