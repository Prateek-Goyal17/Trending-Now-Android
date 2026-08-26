package com.trending.now.app.feature.auth.data.remote

data class RegisterOrLoginRequest(
    val firebaseUid: String,
    val email: String?,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val profileImage: String? = null,
)
