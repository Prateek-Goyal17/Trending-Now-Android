package com.trending.now.app.feature.auth.data.local

import android.content.Context
import com.trending.now.app.core.network.AuthTokenProvider
import com.trending.now.app.feature.auth.domain.model.AuthProfile
import com.trending.now.app.feature.auth.domain.model.AuthSession
import com.trending.now.app.feature.auth.domain.model.AuthState
import com.trending.now.app.feature.auth.domain.model.toAuthProfile
import com.trending.now.app.feature.auth.domain.model.toAuthenticatedState
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
    private val _authState = MutableStateFlow(readAuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    override fun token(): String? = preferences.getString(KEY_ACCESS_TOKEN, null)

    fun hasAccessToken(): Boolean = !token().isNullOrBlank()

    fun continueAsGuest() {
        preferences.edit()
            .putBoolean(KEY_IS_GUEST, true)
            .apply()
        _authState.value = AuthState.Guest
    }

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
            .putInt(KEY_FAVORITE_CREATORS_COUNT, profile.favoriteCreatorsCount)
            .putInt(KEY_BOOKMARK_POSTS_COUNT, profile.bookmarkPostsCount)
            .putInt(KEY_LIKED_NEWS_COUNT, profile.likedNewsCount)
            .putBoolean(KEY_IS_GUEST, false)
            .apply()
        _profile.value = profile
        _authState.value = profile.toAuthenticatedState()
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
            .putInt(KEY_FAVORITE_CREATORS_COUNT, profile.favoriteCreatorsCount)
            .putInt(KEY_BOOKMARK_POSTS_COUNT, profile.bookmarkPostsCount)
            .putInt(KEY_LIKED_NEWS_COUNT, profile.likedNewsCount)
            .putBoolean(KEY_IS_GUEST, false)
            .apply()
        _profile.value = profile
        _authState.value = profile.toAuthenticatedState()
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
        _authState.value = AuthState.LoggedOut
    }

    private fun readAuthState(): AuthState {
        val profile = readProfile()
        if (profile != null) return profile.toAuthenticatedState()

        return if (preferences.getBoolean(KEY_IS_GUEST, false)) {
            AuthState.Guest
        } else {
            AuthState.LoggedOut
        }
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
            favoriteCreatorsCount = preferences.getInt(KEY_FAVORITE_CREATORS_COUNT, 0),
            bookmarkPostsCount = preferences.getInt(KEY_BOOKMARK_POSTS_COUNT, 0),
            likedNewsCount = preferences.getInt(KEY_LIKED_NEWS_COUNT, 0),
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
        const val KEY_FAVORITE_CREATORS_COUNT = "favorite_creators_count"
        const val KEY_BOOKMARK_POSTS_COUNT = "bookmark_posts_count"
        const val KEY_LIKED_NEWS_COUNT = "liked_news_count"
        const val KEY_IS_GUEST = "is_guest"
    }
}
