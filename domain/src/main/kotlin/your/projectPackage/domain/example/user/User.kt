package your.projectPackage.domain.example.user

data class User(
    val uid: UserId,
    val name: String?,
)

@JvmInline
value class UserId(val value: Int)
