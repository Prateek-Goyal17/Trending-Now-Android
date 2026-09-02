package com.trending.now.app.feature.auth.domain.model

sealed interface AuthState {
    data object LoggedOut : AuthState
    data object Guest : AuthState
    data class NewUser(val profile: AuthProfile) : AuthState
    data class ExistingUser(val profile: AuthProfile) : AuthState
}

fun AuthProfile.toAuthenticatedState(): AuthState {
    return if (favoriteCreatorsCount > 0) {
        AuthState.ExistingUser(this)
    } else {
        AuthState.NewUser(this)
    }
}

val AuthState.isAuthenticated: Boolean
    get() = this is AuthState.NewUser || this is AuthState.ExistingUser

val AuthState.isGuestLike: Boolean
    get() = this == AuthState.Guest || this == AuthState.LoggedOut

val AuthState.profileOrNull: AuthProfile?
    get() = when (this) {
        is AuthState.NewUser -> profile
        is AuthState.ExistingUser -> profile
        AuthState.Guest,
        AuthState.LoggedOut,
        -> null
    }

inline fun AuthState.handleAuthenticatedAction(
    onRestricted: () -> Unit,
    onAllowed: () -> Unit,
) {
    if (isAuthenticated) {
        onAllowed()
    } else {
        onRestricted()
    }
}
