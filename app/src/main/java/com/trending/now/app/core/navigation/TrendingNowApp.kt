package com.trending.now.app.core.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.trending.now.app.core.common.components.TrendingNowBottomBar
import com.trending.now.app.core.common.components.TrendingNowSnackbarHost
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.feature.auth.presentation.AppAuthViewModel
import com.trending.now.app.feature.auth.presentation.LoginScreen
import com.trending.now.app.feature.creator.presentation.CreatorScreen
import com.trending.now.app.feature.home.presentation.HomeScreen
import com.trending.now.app.feature.me.presentation.MeScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch

@Composable
fun TrendingNowApp() {
    AuthSessionBootstrap()

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarScope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val selectedBottomDestination = AppDestination.entries.firstOrNull { item ->
        currentDestination?.hierarchy?.any { it.route == item.route } == true
    } ?: AppDestination.Home
    val showBottomBar = AppDestination.entries.any { item ->
        currentDestination?.hierarchy?.any { it.route == item.route } == true
    }

    Scaffold(
        containerColor = TrendingNowColors.Background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            TrendingNowSnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            if (showBottomBar) {
                TrendingNowBottomBar(
                    currentDestination = selectedBottomDestination,
                    onDestinationClick = { destination ->
                        navController.navigate(destination.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(AppRoute.HOME) {
                                saveState = true
                            }
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(AppRoute.HOME) {
                HomeScreen()
            }
            composable(AppRoute.CREATORS) {
                CreatorScreen()
            }
            composable(AppRoute.ME) {
                MeScreen(
                    onLoginClick = {
                        navController.navigate(AppRoute.LOGIN) {
                            launchSingleTop = true
                        }
                    },
                )
            }
            composable(AppRoute.LOGIN) {
                LoginScreen(
                    onGoogleLoginSuccess = {
                        navController.popBackStack()
                    },
                    onGuestClick = {
                        navController.popBackStack()
                    },
                    showSnackbar = { message ->
                        snackbarScope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    },
                )
            }
        }
    }
}

@Composable
@Suppress("UNUSED_PARAMETER")
private fun AuthSessionBootstrap(
    viewModel: AppAuthViewModel = hiltViewModel(),
) = Unit
