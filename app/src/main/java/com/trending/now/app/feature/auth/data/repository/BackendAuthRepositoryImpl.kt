package com.trending.now.app.feature.auth.data.repository

import android.util.Log
import com.trending.now.app.feature.auth.data.local.AuthSessionStore
import com.trending.now.app.feature.auth.data.remote.AuthApiService
import com.trending.now.app.feature.auth.data.remote.RegisterOrLoginRequest
import com.trending.now.app.feature.auth.data.remote.UpdateUserRequest
import com.trending.now.app.feature.auth.data.remote.toAuthSession
import com.trending.now.app.feature.auth.data.remote.toBackendUser
import com.trending.now.app.feature.auth.domain.model.AuthSession
import com.trending.now.app.feature.auth.domain.model.AuthUser
import com.trending.now.app.feature.auth.domain.model.BackendUser
import com.trending.now.app.feature.auth.domain.model.toAuthProfile
import com.trending.now.app.feature.auth.domain.repository.BackendAuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException
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

    override suspend fun getCurrentUser(): Result<BackendUser> {
        return runCatching {
            Log.d(TAG, "Get current user started")
            val response = authApiService.getCurrentUser()
            val userResponse = response.requireBody()
            if (!userResponse.success) {
                error(userResponse.message ?: "Unable to get current user.")
            }
            userResponse.tokens?.let { tokens ->
                authSessionStore.saveTokens(
                    accessToken = tokens.accessToken,
                    refreshToken = tokens.refreshToken,
                )
            }

            Log.d(TAG, "Get current user succeeded")
            userResponse.toBackendUser().also { user ->
                authSessionStore.saveProfile(user.toAuthProfile())
            }
        }.onFailure { error ->
            Log.d(TAG, "Get current user failed: ${error::class.java.simpleName}")
        }
    }

    override suspend fun refreshCurrentUser(): Result<BackendUser> {
        if (!authSessionStore.hasAccessToken()) {
            Log.d(TAG, "Skipping user refresh: no local token")
            return Result.failure(IllegalStateException("No local auth token."))
        }

        return getCurrentUser().onFailure { error ->
            if (error is HttpException && error.code() == HTTP_UNAUTHORIZED) {
                Log.d(TAG, "User refresh unauthorized; clearing session")
                authSessionStore.clear()
            }
        }
    }

    override suspend fun updateUser(
        uid: String,
        request: UpdateUserRequest,
    ): Result<BackendUser> {
        return runCatching {
            Log.d(TAG, "Update user started")
            val response = authApiService.updateUser(
                uid = uid,
                body = request,
            )
            val userResponse = response.requireBody()
            if (!userResponse.success) {
                error(userResponse.message ?: "Unable to update user.")
            }

            Log.d(TAG, "Update user succeeded")
            userResponse.toBackendUser().also { user ->
                authSessionStore.saveProfile(user.toAuthProfile())
            }
        }.onFailure { error ->
            Log.d(TAG, "Update user failed: ${error::class.java.simpleName}")
        }
    }

    override suspend fun logout() {
        Log.d(TAG, "Backend session logout")
        authSessionStore.clear()
    }

    private fun <T> Response<T>.requireBody(): T {
        if (!isSuccessful) {
            throw HttpException(this)
        }

        return requireNotNull(body()) {
            "Response body is empty."
        }
    }

    private companion object {
        const val TAG = "AuthBackendRepository"
        const val HTTP_UNAUTHORIZED = 401
    }
}
