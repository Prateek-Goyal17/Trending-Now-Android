package com.trending.now.app.feature.auth.domain.model

data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
    val user: BackendUser,
)

data class BackendUser(
    val id: String,
    val firebaseUid: String,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val profileImage: String?,
    val location: String?,
    val isVerified: Boolean,
    val lastLogin: String?,
    val createdAt: String?,
    val updatedAt: String?,
)
