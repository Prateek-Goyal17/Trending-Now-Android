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
import com.trending.now.app.feature.me.presentation.components.CreatorConnectionCard
import com.trending.now.app.feature.me.presentation.components.MeHeader
import com.trending.now.app.feature.me.presentation.components.ProfileSignupCard
import com.trending.now.app.feature.me.presentation.components.SupportAndPrivacy
import com.trending.now.app.feature.me.presentation.components.TodayInYourWorld

@Composable
fun MeScreen(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()
    val logoutSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showLogoutSheet by remember { mutableStateOf(false) }

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
        TodayInYourWorld()
        CreatorConnectionCard()
        SupportAndPrivacy(
            onLogoutClick = {
                showLogoutSheet = true
            },
        )
        Spacer(Modifier.height(10.dp))
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
}
