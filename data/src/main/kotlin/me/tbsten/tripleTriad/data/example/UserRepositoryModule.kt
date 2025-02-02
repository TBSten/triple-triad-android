package me.tbsten.tripleTriad.data.example

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import me.tbsten.tripleTriad.domain.example.user.UserRepository

@Module
@InstallIn(SingletonComponent::class)
internal interface UserRepositoryModule {
    @Binds
    @Singleton
    fun bindsUserRepository(
        impl: UserRepositoryImpl,
    ): UserRepository
}
