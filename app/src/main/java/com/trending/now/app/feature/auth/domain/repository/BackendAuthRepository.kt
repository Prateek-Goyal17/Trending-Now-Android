package com.trending.now.app.feature.auth.domain.repository

import com.trending.now.app.feature.auth.domain.model.AuthSession
import com.trending.now.app.feature.auth.domain.model.AuthUser
import com.trending.now.app.feature.auth.domain.model.BackendUser

interface BackendAuthRepository {
    suspend fun registerOrLogin(firebaseUser: AuthUser): Result<AuthSession>

    suspend fun updateUser(
        firstName: String?,
        lastName: String?,
        username: String?,
    ): Result<BackendUser>

    suspend fun logout()
}
