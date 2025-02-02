package me.tbsten.tripleTriad.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import me.tbsten.tripleTriad.data.database.example.user.UserDao
import me.tbsten.tripleTriad.data.database.example.user.UserEntity

private const val DATABASE_NAME = "app-database"

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Module
@InstallIn(SingletonComponent::class)
internal object AppDatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext
        applicationContext: Context,
    ) = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        DATABASE_NAME,
    ).build()
}
