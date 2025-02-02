package your.projectPackage.domain.example.post

interface GetPostsUseCase {
    suspend fun execute(): List<Post>
}
