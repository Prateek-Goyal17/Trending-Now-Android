package com.trending.now.app.core.navigation

import android.net.Uri
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.trending.now.app.core.common.webview.AppWebViewScreen
import androidx.navigation.compose.rememberNavController
import com.trending.now.app.core.common.components.TrendingNowBottomBar
import com.trending.now.app.core.common.components.TrendingNowSnackbarHost
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.feature.auth.presentation.AppAuthViewModel
import com.trending.now.app.feature.auth.presentation.LoginScreen
import com.trending.now.app.feature.creator.presentation.CreatorScreen
import com.trending.now.app.feature.home.presentation.HomeScreen
import com.trending.now.app.feature.me.presentation.FollowingScreen
import com.trending.now.app.feature.me.presentation.MeScreen
import com.trending.now.app.feature.me.presentation.MyActivityScreen
import com.trending.now.app.feature.me.presentation.ReportProblemScreen
import com.trending.now.app.feature.me.presentation.SavedScreen
import com.trending.now.app.feature.me.presentation.TimeSpentScreen
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
                        navController.navigateToBottomDestination(destination)
                    },
                )
            }
        },
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.HOME,
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
                    onFollowingClick = {
                        navController.navigate(AppRoute.FOLLOWING)
                    },
                    onSavedClick = {
                        navController.navigate(AppRoute.SAVED)
                    },
                    onMyActivityClick = {
                        navController.navigate(AppRoute.MY_ACTIVITY)
                    },
                    onTimeSpentClick = {
                        navController.navigate(AppRoute.TIME_SPENT)
                    },
                    onReportProblemClick = {
                        navController.navigate(AppRoute.REPORT_PROBLEM)
                    },
                    onPrivacyPolicyClick = {
                        navController.navigate(
                            AppRoute.webView(
                                title = "Privacy Policy",
                                url = AppRoute.PRIVACY_POLICY_URL,
                            ),
                        )
                    },
                    onCommunityGuidelineClick = {
                        navController.navigate(
                            AppRoute.webView(
                                title = "Community Guideline",
                                url = AppRoute.COMMUNITY_GUIDELINE_URL,
                            ),
                        )
                    },
                    onFindFavouritesClick = {
                        navController.navigateToBottomDestination(AppDestination.Creator)
                    },
                )
            }
            composable(AppRoute.FOLLOWING) {
                FollowingScreen()
            }
            composable(AppRoute.SAVED) {
                SavedScreen()
            }
            composable(AppRoute.MY_ACTIVITY) {
                MyActivityScreen()
            }
            composable(AppRoute.TIME_SPENT) {
                TimeSpentScreen()
            }
            composable(AppRoute.REPORT_PROBLEM) {
                ReportProblemScreen()
            }
            composable(
                route = AppRoute.WEB_VIEW,
                arguments = listOf(
                    navArgument(AppRoute.WEB_VIEW_TITLE) {
                        type = NavType.StringType
                    },
                    navArgument(AppRoute.WEB_VIEW_URL) {
                        type = NavType.StringType
                    },
                ),
            ) { backStackEntry ->
                AppWebViewScreen(
                    title = Uri.decode(
                        backStackEntry.arguments?.getString(AppRoute.WEB_VIEW_TITLE).orEmpty(),
                    ),
                    url = Uri.decode(
                        backStackEntry.arguments?.getString(AppRoute.WEB_VIEW_URL).orEmpty(),
                    ),
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

private fun NavHostController.navigateToBottomDestination(destination: AppDestination) {
    navigate(destination.route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(AppRoute.HOME) {
            saveState = true
        }
    }
}
