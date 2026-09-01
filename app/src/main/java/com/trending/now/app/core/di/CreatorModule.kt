package com.trending.now.app.core.di

import com.trending.now.app.feature.creator.data.repository.CreatorRepositoryImpl
import com.trending.now.app.feature.creator.domain.repository.CreatorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CreatorModule {
    @Binds
    @Singleton
    abstract fun bindCreatorRepository(
        repository: CreatorRepositoryImpl,
    ): CreatorRepository
}
