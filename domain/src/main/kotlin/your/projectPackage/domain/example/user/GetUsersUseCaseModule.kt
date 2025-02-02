package your.projectPackage.domain.example.user

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface GetUsersUseCaseModule {
    @Binds
    @Singleton
    fun bindGetUsersUseCase(
        impl: GetUsersUseCaseImpl,
    ): GetUsersUseCase
}
