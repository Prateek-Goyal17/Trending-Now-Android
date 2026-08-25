package com.trending.now.app.core.network

object NoAuthTokenProvider : AuthTokenProvider {
    override fun token(): String? = null
}
