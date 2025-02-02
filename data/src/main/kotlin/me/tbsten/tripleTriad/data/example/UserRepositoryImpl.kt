package me.tbsten.tripleTriad.data.example

import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tbsten.tripleTriad.data.database.AppDatabase
import me.tbsten.tripleTriad.data.database.example.user.UserEntity
import me.tbsten.tripleTriad.domain.example.user.User
import me.tbsten.tripleTriad.domain.example.user.UserId
import me.tbsten.tripleTriad.domain.example.user.UserRepository

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
