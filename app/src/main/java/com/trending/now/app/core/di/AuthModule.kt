package com.trending.now.app.core.di

import com.google.firebase.auth.FirebaseAuth
import com.trending.now.app.feature.auth.data.repository.BackendAuthRepositoryImpl
import com.trending.now.app.feature.auth.data.repository.FirebaseAuthRepositoryImpl
import com.trending.now.app.feature.auth.domain.repository.BackendAuthRepository
import com.trending.now.app.feature.auth.domain.repository.FirebaseAuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        repository: FirebaseAuthRepositoryImpl,
    ): FirebaseAuthRepository

    @Binds
    @Singleton
    abstract fun bindAuthBackendRepository(
        repository: BackendAuthRepositoryImpl,
    ): BackendAuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
    }
}
