package com.trending.now.app.core.di

import com.trending.now.app.core.network.AuthTokenProvider
import com.trending.now.app.core.network.TrendingNowRetrofitFactory
import com.trending.now.app.feature.auth.data.local.AuthSessionStore
import com.trending.now.app.feature.auth.data.remote.AuthApiService
import com.trending.now.app.feature.creator.data.remote.CreatorApiService
import com.trending.now.app.feature.genre.data.remote.GenreApiService
import com.trending.now.app.feature.home.data.remote.HomeApiService
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

    @Provides
    @Singleton
    fun provideCreatorApiService(retrofit: Retrofit): CreatorApiService {
        return retrofit.create(CreatorApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGenreApiService(retrofit: Retrofit): GenreApiService {
        return retrofit.create(GenreApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeApiService(retrofit: Retrofit): HomeApiService {
        return retrofit.create(HomeApiService::class.java)
    }
}
