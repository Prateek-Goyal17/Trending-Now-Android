@file:OptIn(ExperimentalMaterial3Api::class)

package com.trending.now.app.feature.me.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.trending.now.app.R
import com.trending.now.app.core.common.bottom_sheet.AppBottomSheetConfig
import com.trending.now.app.core.common.bottom_sheet.TrendingNowCommonBottomSheet
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.feature.auth.domain.model.AuthState
import com.trending.now.app.feature.auth.domain.model.handleAuthenticatedAction
import com.trending.now.app.feature.auth.domain.model.profileOrNull
import com.trending.now.app.feature.me.presentation.components.CreatorConnectionCard
import com.trending.now.app.feature.me.presentation.components.MeHeader
import com.trending.now.app.feature.me.presentation.components.ProfileSignupCard
import com.trending.now.app.feature.me.presentation.components.SupportAndPrivacy
import com.trending.now.app.feature.me.presentation.components.TodayInYourWorld

@Composable
fun MeScreen(
    onLoginClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onSavedClick: () -> Unit,
    onMyActivityClick: () -> Unit,
    onTimeSpentClick: () -> Unit,
    onReportProblemClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onCommunityGuidelineClick: () -> Unit,
    onFindFavouritesClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()
    val logoutSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val restrictedActionSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showLogoutSheet by remember { mutableStateOf(false) }
    var restrictedActionConfig by remember { mutableStateOf<AppBottomSheetConfig?>(null) }

    fun handleTodayCardClick(
        config: AppBottomSheetConfig,
        onAuthenticatedClick: () -> Unit,
    ) {
        authState.handleAuthenticatedAction(
            onAllowed = onAuthenticatedClick,
            onRestricted = {
                restrictedActionConfig = config
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 15.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        MeHeader()
        ProfileSignupCard(
            authState = authState,
            onSignUpClick = onLoginClick,
        )
        TodayInYourWorld(
            followingValue = authState.profileOrNull?.favoriteCreatorsCount?.toString() ?: "--",
            savedValue = authState.profileOrNull?.bookmarkPostsCount?.toString() ?: "--",
            myActivityValue = authState.profileOrNull?.likedNewsCount?.toString() ?: "--",
            timeSpentValue = "--",
            onFollowingClick = {
                handleTodayCardClick(
                    config = followingBottomSheetConfig(),
                    onAuthenticatedClick = onFollowingClick,
                )
            },
            onSavedClick = {
                handleTodayCardClick(
                    config = savedBottomSheetConfig(),
                    onAuthenticatedClick = onSavedClick,
                )
            },
            onMyActivityClick = {
                handleTodayCardClick(
                    config = myActivityBottomSheetConfig(),
                    onAuthenticatedClick = onMyActivityClick,
                )
            },
            onTimeSpentClick = {
                handleTodayCardClick(
                    config = timeSpentBottomSheetConfig(),
                    onAuthenticatedClick = onTimeSpentClick,
                )
            },
        )
        CreatorConnectionCard(
            authState = authState,
            onSignUpClick = onLoginClick,
            onFindFavouritesClick = onFindFavouritesClick,
        )
        SupportAndPrivacy(
            onReportProblemClick = onReportProblemClick,
            onPrivacyPolicyClick = onPrivacyPolicyClick,
            onCommunityGuidelineClick = onCommunityGuidelineClick,
            onLogoutClick = {
                showLogoutSheet = true
            },
        )
        Spacer(Modifier.height(80.dp))
    }

    if (showLogoutSheet) {
        TrendingNowCommonBottomSheet(
            config = AppBottomSheetConfig(
                iconRes = R.drawable.ic_logout,
                title = "Log out?",
                description = "You can continue as a guest or sign in again anytime.",
                primaryButtonText = "Log out",
            ),
            sheetState = logoutSheetState,
            onDismiss = {
                showLogoutSheet = false
            },
            onPrimaryClick = {
                showLogoutSheet = false
                viewModel.logout(context)
            },
        )
    }

    restrictedActionConfig?.let { config ->
        TrendingNowCommonBottomSheet(
            config = config,
            sheetState = restrictedActionSheetState,
            onDismiss = {
                restrictedActionConfig = null
            },
            onPrimaryClick = {
                restrictedActionConfig = null
                onLoginClick()
            },
        )
    }
}

private fun followingBottomSheetConfig(): AppBottomSheetConfig {
    return AppBottomSheetConfig(
        iconRes = R.drawable.ic_profile_following,
        title = "Follow Your Favorite Creators",
        description = "Create an account to follow creators and keep all their latest updates in one place.",
        primaryButtonText = "Sign Up",
    )
}

private fun savedBottomSheetConfig(): AppBottomSheetConfig {
    return AppBottomSheetConfig(
        iconRes = R.drawable.ic_profile_saved,
        title = "Build Your Collections",
        description = "Save creator posts and we'll automatically organize them into your collections.",
        primaryButtonText = "Sign Up",
    )
}

private fun myActivityBottomSheetConfig(): AppBottomSheetConfig {
    return AppBottomSheetConfig(
        iconRes = R.drawable.ic_profile_activity,
        title = "Join the Conversation",
        description = "Create an account to comment, vote in polls, and keep track of your activity.",
        primaryButtonText = "Sign Up",
    )
}

private fun timeSpentBottomSheetConfig(): AppBottomSheetConfig {
    return AppBottomSheetConfig(
        iconRes = R.drawable.ic_profile_time,
        title = "Track Your Creator Journey",
        description = "Create an account to see your daily activity, usage insights, and creator habits.",
        primaryButtonText = "Sign Up",
    )
}
