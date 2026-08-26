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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
) {
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
        ProfileSignupCard(onSignUpClick = onLoginClick)
        TodayInYourWorld()
        CreatorConnectionCard()
        SupportAndPrivacy()
        Spacer(Modifier.height(10.dp))
    }
}
