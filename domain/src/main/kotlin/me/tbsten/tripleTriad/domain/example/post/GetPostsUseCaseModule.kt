package me.tbsten.tripleTriad.domain.example.post

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface GetPostsUseCaseModule {
    @Binds
    @Singleton
    fun provideGetPostsUseCase(
        impl: GetPostsUseCaseImpl,
    ): GetPostsUseCase
}
