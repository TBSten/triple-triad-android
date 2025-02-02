package me.tbsten.tripleTriad.data.example.post

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import me.tbsten.tripleTriad.domain.example.post.PostsRepository

@Module
@InstallIn(SingletonComponent::class)
internal interface PostsRepositoryModule {
    @Binds
    @Singleton
    fun providePostsRepository(
        impl: PostsRepositoryImpl,
    ): PostsRepository
}
