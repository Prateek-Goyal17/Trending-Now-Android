package com.trending.now.app.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TrendingNowRetrofitFactory {
    fun create(
        baseUrl: String = TrendingNowNetworkConfig.baseUrl,
        tokenProvider: AuthTokenProvider = NoAuthTokenProvider,
    ): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (com.trending.now.app.BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                TrendingNowHeaderInterceptor(
                    appVersion = TrendingNowNetworkConfig.API_VERSION,
                    tokenProvider = tokenProvider,
                ),
            )
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
