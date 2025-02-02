package your.projectPackage.data.example

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import your.projectPackage.data.database.AppDatabase
import your.projectPackage.data.database.example.user.UserEntity
import your.projectPackage.domain.example.user.User
import your.projectPackage.domain.example.user.UserId
import your.projectPackage.domain.example.user.UserRepository

internal class UserRepositoryImpl @Inject constructor(
    appDatabase: AppDatabase,
) : UserRepository {
    private val userDao = appDatabase.userDao()
    override suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        userDao
            .getAll()
            .toDomain()
    }

    override suspend fun createUser(vararg users: User) = withContext(Dispatchers.IO) {
        val userEntities = users.toEntities()
        userDao.insertAll(*userEntities.toTypedArray())
    }

    override suspend fun deleteUser(vararg users: User) {
        val userEntities = users.toEntities()
        userDao.delete(*userEntities.toTypedArray())
    }

    private fun List<UserEntity>.toDomain() = map {
        User(
            uid = UserId(it.uid),
            name = it.name,
        )
    }

    private fun Array<out User>.toEntities() = map {
        UserEntity(
            uid = it.uid.value,
            name = it.name,
        )
    }
}
