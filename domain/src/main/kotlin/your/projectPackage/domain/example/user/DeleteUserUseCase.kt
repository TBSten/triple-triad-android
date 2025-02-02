package your.projectPackage.domain.example.user

interface DeleteUserUseCase {
    suspend fun execute(user: User)
}
