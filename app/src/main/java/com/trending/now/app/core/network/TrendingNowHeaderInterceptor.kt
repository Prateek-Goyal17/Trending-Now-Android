package com.trending.now.app.core.network

import okhttp3.Interceptor
import okhttp3.Response

class TrendingNowHeaderInterceptor(
    private val appVersion: String,
    private val tokenProvider: AuthTokenProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request()
            .newBuilder()
            .header(ApiHeaders.CONTENT_TYPE, ApiHeaders.JSON)
            .header(ApiHeaders.APP_VERSION, appVersion)
            .header(ApiHeaders.PLATFORM, ApiHeaders.ANDROID_PLATFORM)

        tokenProvider.token()?.takeIf { it.isNotBlank() }?.let { token ->
            requestBuilder.header(ApiHeaders.AUTHORIZATION, "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
