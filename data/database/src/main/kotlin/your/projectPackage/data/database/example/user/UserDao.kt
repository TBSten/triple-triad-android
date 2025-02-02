package your.projectPackage.data.database.example.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<UserEntity>

    @Insert
    suspend fun insertAll(vararg users: UserEntity)

    @Delete
    suspend fun delete(vararg users: UserEntity)
}
