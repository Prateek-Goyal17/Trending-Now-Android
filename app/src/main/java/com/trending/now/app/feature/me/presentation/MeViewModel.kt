package com.trending.now.app.feature.me.presentation

import androidx.lifecycle.ViewModel
import com.trending.now.app.feature.auth.data.local.AuthSessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    authSessionStore: AuthSessionStore,
) : ViewModel() {
    val profile = authSessionStore.profile
}
