package com.trending.now.app.feature.auth.domain.repository

import com.trending.now.app.feature.auth.domain.model.AuthSession
import com.trending.now.app.feature.auth.domain.model.AuthUser

interface BackendAuthRepository {
    suspend fun registerOrLogin(firebaseUser: AuthUser): Result<AuthSession>

    suspend fun logout()
}
