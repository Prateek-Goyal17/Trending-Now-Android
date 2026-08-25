package com.trending.now.app.core.network

interface AuthTokenProvider {
    fun token(): String?
}
