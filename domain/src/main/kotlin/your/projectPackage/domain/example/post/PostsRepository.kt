package your.projectPackage.domain.example.post

interface PostsRepository {
    suspend fun getPosts(): List<Post>
}
