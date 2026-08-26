package com.trending.now.app.feature.auth.domain.repository

import android.content.Context
import com.trending.now.app.feature.auth.domain.model.AuthUser

interface FirebaseAuthRepository {
    suspend fun signInWithGoogle(
        context: Context,
        serverClientId: String,
    ): Result<AuthUser>

    suspend fun signOut(context: Context)
}
