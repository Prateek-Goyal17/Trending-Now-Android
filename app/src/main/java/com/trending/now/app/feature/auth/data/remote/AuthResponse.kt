package com.trending.now.app.feature.auth.data.remote

import com.google.gson.annotations.SerializedName
import com.trending.now.app.feature.auth.domain.model.AuthSession
import com.trending.now.app.feature.auth.domain.model.BackendUser

data class AuthResponse(
    val success: Boolean,
    val message: String?,
    val tokens: AuthTokensResponse?,
    val data: BackendUserResponse?,
)

data class AuthTokensResponse(
    val accessToken: String,
    val refreshToken: String,
)

data class BackendUserResponse(
    @SerializedName("_id")
    val id: String,
    val firebaseUid: String,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val profileImage: String?,
    val location: String?,
    val isVerified: Boolean?,
    val lastLogin: String?,
    val createdAt: String?,
    val updatedAt: String?,
)

fun AuthResponse.toAuthSession(): AuthSession {
    val responseTokens = requireNotNull(tokens) {
        "Auth tokens are missing from response."
    }
    val responseUser = requireNotNull(data) {
        "User data is missing from response."
    }

    return AuthSession(
        accessToken = responseTokens.accessToken,
        refreshToken = responseTokens.refreshToken,
        user = responseUser.toBackendUser(),
    )
}

fun BackendUserResponse.toBackendUser(): BackendUser {
    return BackendUser(
        id = id,
        firebaseUid = firebaseUid,
        username = username,
        firstName = firstName,
        lastName = lastName,
        email = email,
        profileImage = profileImage,
        location = location,
        isVerified = isVerified ?: false,
        lastLogin = lastLogin,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
