package com.trending.now.app.core.di

import com.trending.now.app.feature.genre.data.repository.GenreRepositoryImpl
import com.trending.now.app.feature.genre.domain.repository.GenreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GenreModule {
    @Binds
    @Singleton
    abstract fun bindGenreRepository(
        repository: GenreRepositoryImpl,
    ): GenreRepository
}
