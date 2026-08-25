package com.trending.now.app.feature.auth.data.remote

data class UpdateUserRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    val profileImage: String? = null,
)
