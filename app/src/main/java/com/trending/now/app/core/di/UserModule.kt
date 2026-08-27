package com.trending.now.app.core.di

import com.trending.now.app.feature.user.data.repository.UserRepositoryImpl
import com.trending.now.app.feature.user.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        repository: UserRepositoryImpl,
    ): UserRepository
}
