package your.projectPackage.domain.example.post

import javax.inject.Inject

internal class GetPostsUseCaseImpl @Inject constructor(
    private val postsRepository: PostsRepository,
) : GetPostsUseCase {
    override suspend fun execute(): List<Post> = postsRepository.getPosts()
}
