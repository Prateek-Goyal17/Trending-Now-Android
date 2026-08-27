package com.trending.now.app.feature.auth.data.repository

import android.util.Log
import com.trending.now.app.feature.auth.data.local.AuthSessionStore
import com.trending.now.app.feature.auth.data.remote.AuthApiService
import com.trending.now.app.feature.auth.data.remote.RegisterOrLoginRequest
import com.trending.now.app.feature.auth.data.remote.toAuthSession
import com.trending.now.app.feature.auth.domain.model.AuthSession
import com.trending.now.app.feature.auth.domain.model.AuthUser
import com.trending.now.app.feature.auth.domain.repository.BackendAuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.Response

@Singleton
class BackendAuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val authSessionStore: AuthSessionStore,
) : BackendAuthRepository {
    override suspend fun registerOrLogin(firebaseUser: AuthUser): Result<AuthSession> {
        return runCatching {
            Log.d(TAG, "Backend auth started")
            val response = authApiService.registerOrLogin(
                RegisterOrLoginRequest(
                    firebaseUid = firebaseUser.uid,
                    email = firebaseUser.email,
                    username = null,
                    firstName = firebaseUser.displayName?.substringBefore(" ")?.takeIf { it.isNotBlank() },
                    lastName = firebaseUser.displayName?.substringAfter(" ", "")?.takeIf { it.isNotBlank() },
                    profileImage = firebaseUser.photoUrl,
                ),
            )
            val authResponse = response.requireBody()
            if (!authResponse.success) {
                error(authResponse.message ?: "Backend auth failed.")
            }

            val session = authResponse.toAuthSession()
            authSessionStore.saveSession(session)
            Log.d(TAG, "Backend auth succeeded")
            session
        }.onFailure { error ->
            Log.d(TAG, "Backend auth failed: ${error::class.java.simpleName}")
        }
    }

    override suspend fun logout() {
        Log.d(TAG, "Backend session logout")
        authSessionStore.clear()
    }

    private fun <T> Response<T>.requireBody(): T {
        if (!isSuccessful) {
            error("Request failed with code ${code()}.")
        }

        return requireNotNull(body()) {
            "Response body is empty."
        }
    }

    private companion object {
        const val TAG = "AuthBackendRepository"
    }
}
