package com.trending.now.app.feature.auth.domain.model

data class AuthProfile(
    val id: String,
    val firebaseUid: String,
    val username: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val profileImage: String?,
)

fun BackendUser.toAuthProfile(): AuthProfile {
    return AuthProfile(
        id = id,
        firebaseUid = firebaseUid,
        username = username,
        firstName = firstName,
        lastName = lastName,
        email = email,
        profileImage = profileImage,
    )
}
