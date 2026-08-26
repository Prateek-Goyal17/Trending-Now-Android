package com.trending.now.app.feature.auth.data.remote

import com.trending.now.app.feature.auth.domain.model.BackendUser

data class UserResponse(
    val success: Boolean,
    val message: String?,
    val tokens: AuthTokensResponse?,
    val data: BackendUserResponse?,
)

fun UserResponse.toBackendUser(): BackendUser {
    val responseUser = requireNotNull(data) {
        "User data is missing from response."
    }

    return responseUser.toBackendUser()
}
