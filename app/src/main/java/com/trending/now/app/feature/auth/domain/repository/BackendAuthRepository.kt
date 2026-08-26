package com.trending.now.app.feature.auth.domain.repository

import com.trending.now.app.feature.auth.data.remote.UpdateUserRequest
import com.trending.now.app.feature.auth.domain.model.AuthSession
import com.trending.now.app.feature.auth.domain.model.AuthUser
import com.trending.now.app.feature.auth.domain.model.BackendUser

interface BackendAuthRepository {
    suspend fun registerOrLogin(firebaseUser: AuthUser): Result<AuthSession>

    suspend fun refreshCurrentUser(): Result<BackendUser>

    suspend fun getCurrentUser(): Result<BackendUser>

    suspend fun updateUser(
        uid: String,
        request: UpdateUserRequest,
    ): Result<BackendUser>

    suspend fun logout()
}
