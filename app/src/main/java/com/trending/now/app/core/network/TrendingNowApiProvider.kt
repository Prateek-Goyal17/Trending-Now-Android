package com.trending.now.app.core.network

import retrofit2.Retrofit

object TrendingNowApiProvider {
    private val retrofit: Retrofit by lazy {
        TrendingNowRetrofitFactory.create()
    }

    fun <T> create(service: Class<T>): T = retrofit.create(service)
}
