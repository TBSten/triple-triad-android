package your.projectPackage.domain.example.user

interface CreateUserUseCase {
    suspend fun execute(user: User)
}
