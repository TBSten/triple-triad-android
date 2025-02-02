package your.projectPackage.domain.example.user

import javax.inject.Inject

internal class DeleteUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : DeleteUserUseCase {
    override suspend fun execute(user: User) {
        userRepository.deleteUser(user)
    }
}
