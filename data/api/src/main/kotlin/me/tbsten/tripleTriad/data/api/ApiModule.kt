package me.tbsten.tripleTriad.data.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.create
import me.tbsten.tripleTriad.data.api.generated.api.PostsApi

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {
    @Provides
    @Singleton
    fun provideApi(
        retrofit: Retrofit,
    ): PostsApi = retrofit.create<PostsApi>()
}
