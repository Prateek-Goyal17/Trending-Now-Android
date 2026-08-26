package com.trending.now.app.feature.auth.data.local

import android.content.Context
import com.trending.now.app.core.network.AuthTokenProvider
import com.trending.now.app.feature.auth.domain.model.AuthProfile
import com.trending.now.app.feature.auth.domain.model.AuthSession
import com.trending.now.app.feature.auth.domain.model.toAuthProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthSessionStore @Inject constructor(
    @ApplicationContext context: Context,
) : AuthTokenProvider {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val _profile = MutableStateFlow(readProfile())
    val profile: StateFlow<AuthProfile?> = _profile.asStateFlow()

    override fun token(): String? = preferences.getString(KEY_ACCESS_TOKEN, null)

    fun hasAccessToken(): Boolean = !token().isNullOrBlank()

    fun saveSession(session: AuthSession) {
        val profile = session.user.toAuthProfile()
        preferences.edit()
            .putString(KEY_ACCESS_TOKEN, session.accessToken)
            .putString(KEY_REFRESH_TOKEN, session.refreshToken)
            .putString(KEY_USER_ID, profile.id)
            .putString(KEY_FIREBASE_UID, profile.firebaseUid)
            .putString(KEY_USERNAME, profile.username)
            .putString(KEY_FIRST_NAME, profile.firstName)
            .putString(KEY_LAST_NAME, profile.lastName)
            .putString(KEY_EMAIL, profile.email)
            .putString(KEY_PROFILE_IMAGE, profile.profileImage)
            .apply()
        _profile.value = profile
    }

    fun saveProfile(profile: AuthProfile) {
        preferences.edit()
            .putString(KEY_USER_ID, profile.id)
            .putString(KEY_FIREBASE_UID, profile.firebaseUid)
            .putString(KEY_USERNAME, profile.username)
            .putString(KEY_FIRST_NAME, profile.firstName)
            .putString(KEY_LAST_NAME, profile.lastName)
            .putString(KEY_EMAIL, profile.email)
            .putString(KEY_PROFILE_IMAGE, profile.profileImage)
            .apply()
        _profile.value = profile
    }

    fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        preferences.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply()
    }

    fun clear() {
        preferences.edit().clear().apply()
        _profile.value = null
    }

    private fun readProfile(): AuthProfile? {
        val id = preferences.getString(KEY_USER_ID, null) ?: return null
        val firebaseUid = preferences.getString(KEY_FIREBASE_UID, null) ?: return null

        return AuthProfile(
            id = id,
            firebaseUid = firebaseUid,
            username = preferences.getString(KEY_USERNAME, null),
            firstName = preferences.getString(KEY_FIRST_NAME, null),
            lastName = preferences.getString(KEY_LAST_NAME, null),
            email = preferences.getString(KEY_EMAIL, null),
            profileImage = preferences.getString(KEY_PROFILE_IMAGE, null),
        )
    }

    private companion object {
        const val PREFERENCES_NAME = "auth_session"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
        const val KEY_USER_ID = "user_id"
        const val KEY_FIREBASE_UID = "firebase_uid"
        const val KEY_USERNAME = "username"
        const val KEY_FIRST_NAME = "first_name"
        const val KEY_LAST_NAME = "last_name"
        const val KEY_EMAIL = "email"
        const val KEY_PROFILE_IMAGE = "profile_image"
    }
}
