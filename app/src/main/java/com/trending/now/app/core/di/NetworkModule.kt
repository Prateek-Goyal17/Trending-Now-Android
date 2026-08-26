package com.trending.now.app.core.di

import com.trending.now.app.core.network.AuthTokenProvider
import com.trending.now.app.core.network.TrendingNowRetrofitFactory
import com.trending.now.app.feature.auth.data.local.AuthSessionStore
import com.trending.now.app.feature.auth.data.remote.AuthApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindingModule {
    @Binds
    @Singleton
    abstract fun bindAuthTokenProvider(
        sessionStore: AuthSessionStore,
    ): AuthTokenProvider
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(
        authTokenProvider: AuthTokenProvider,
    ): Retrofit {
        return TrendingNowRetrofitFactory.create(tokenProvider = authTokenProvider)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }
}
