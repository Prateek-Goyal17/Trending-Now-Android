package com.trending.now.app.feature.auth.domain.model

sealed interface AuthState {
    data object LoggedOut : AuthState
    data object Guest : AuthState
    data class NewUser(val profile: AuthProfile) : AuthState
    data class OldUser(val profile: AuthProfile) : AuthState
}

fun AuthProfile.toAuthenticatedState(): AuthState {
    return if (favoriteCreatorsCount > 0) {
        AuthState.OldUser(this)
    } else {
        AuthState.NewUser(this)
    }
}
