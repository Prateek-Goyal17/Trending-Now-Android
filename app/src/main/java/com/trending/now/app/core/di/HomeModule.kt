package com.trending.now.app.core.di

import com.trending.now.app.feature.home.data.repository.HomeRepositoryImpl
import com.trending.now.app.feature.home.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {
    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        repository: HomeRepositoryImpl,
    ): HomeRepository
}
