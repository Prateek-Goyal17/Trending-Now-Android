package com.trending.now.app.feature.auth.domain.model

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val firebaseIdToken: String?,
)
