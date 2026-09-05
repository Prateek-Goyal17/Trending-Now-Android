package com.trending.now.app.feature.user.domain.repository

import com.trending.now.app.feature.auth.data.remote.UpdateUserRequest
import com.trending.now.app.feature.auth.domain.model.BackendUser

interface UserRepository {
    suspend fun refreshCurrentUser(): Result<BackendUser>

    suspend fun getCurrentUser(): Result<BackendUser>

    suspend fun updateUser(
        request: UpdateUserRequest,
    ): Result<BackendUser>
}
