package com.trending.now.app.core.navigation

import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.trending.now.app.core.common.components.TrendingNowBottomBar
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.feature.creator.presentation.CreatorScreen
import com.trending.now.app.feature.home.presentation.HomeScreen
import com.trending.now.app.feature.me.presentation.MeScreen

@Composable
fun TrendingNowApp() {
    var destination by rememberSaveable { mutableStateOf(AppDestination.Home) }

    Scaffold(
        containerColor = TrendingNowColors.Background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            TrendingNowBottomBar(
                currentDestination = destination,
                onDestinationClick = { destination = it },
            )
        },
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (destination) {
            AppDestination.Home -> HomeScreen(modifier = modifier)
            AppDestination.Creator -> CreatorScreen(modifier = modifier)
            AppDestination.Me -> MeScreen(modifier = modifier)
        }
    }
}
