package your.projectPackage.domain.example.user

interface GetUsersUseCase {
    suspend fun execute(): List<User>
}
