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
import com.trending.now.app.feature.creator.presentation.CreatorDetailScreen
import com.trending.now.app.feature.creator.presentation.CreatorScreen
import com.trending.now.app.feature.creator.presentation.CreatorVideoFeedScreen
import com.trending.now.app.feature.creator.presentation.CreatorViewModel
import com.trending.now.app.feature.creator.presentation.PickFavoriteCreatorsRoute
import com.trending.now.app.feature.home.presentation.HomeScreen
import com.trending.now.app.feature.genre.presentation.CreatorSearchRoute
import com.trending.now.app.feature.genre.presentation.GenreCreatorsRoute
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
    val isCreatorSearch = currentDestination?.hierarchy?.any {
        it.route == AppRoute.CREATOR_SEARCH
    } == true
    val selectedBottomDestination = if (isCreatorSearch) {
        AppDestination.Creator
    } else {
        AppDestination.entries.firstOrNull { item ->
            currentDestination?.hierarchy?.any { it.route == item.route } == true
        } ?: AppDestination.Home
    }
    val showBottomBar = isCreatorSearch || AppDestination.entries.any { item ->
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
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.HOME,
        ) {
            composable(AppRoute.HOME) {
                HomeScreen(contentPadding = paddingValues)
            }
            composable(AppRoute.CREATORS) {
                CreatorScreen(
                    onPersonalizeFeedClick = {
                        navController.navigate(AppRoute.PICK_FAVORITE_CREATORS)
                    },
                    onTrendingVideoClick = { index, postId ->
                        navController.navigate(AppRoute.creatorVideoFeed(index, postId)) {
                            launchSingleTop = true
                        }
                    },
                    onCreatorClick = { creatorSlug ->
                        navController.navigate(AppRoute.creatorDetail(creatorSlug))
                    },
                    onSearchClick = {
                        navController.navigate(AppRoute.CREATOR_SEARCH)
                    },
                )
            }
            composable(AppRoute.CREATOR_SEARCH) {
                CreatorSearchRoute(
                    onGenreClick = { genreId ->
                        navController.navigate(AppRoute.genreCreators(genreId))
                    },
                    onCreatorClick = { creatorSlug ->
                        navController.navigate(AppRoute.creatorDetail(creatorSlug))
                    },
                )
            }
            composable(
                route = AppRoute.GENRE_CREATORS,
                arguments = listOf(
                    navArgument(AppRoute.GENRE_ID) {
                        type = NavType.StringType
                    },
                ),
            ) { backStackEntry ->
                GenreCreatorsRoute(
                    genreId = Uri.decode(
                        backStackEntry.arguments?.getString(AppRoute.GENRE_ID).orEmpty(),
                    ),
                    onBack = {
                        navController.popBackStack()
                    },
                    onCreatorClick = { creatorSlug ->
                        navController.navigate(AppRoute.creatorDetail(creatorSlug))
                    },
                )
            }
            composable(AppRoute.PICK_FAVORITE_CREATORS) {
                PickFavoriteCreatorsRoute(
                    onBack = {
                        navController.popBackStack()
                    },
                    onSignUp = {
                        navController.navigate(AppRoute.LOGIN) {
                            launchSingleTop = true
                        }
                    },
                    onCompleted = {
                        navController.popBackStack()
                    },
                )
            }
            composable(
                route = AppRoute.CREATOR_VIDEO_FEED,
                arguments = listOf(
                    navArgument(AppRoute.VIDEO_INDEX) { type = NavType.IntType },
                    navArgument(AppRoute.VIDEO_POST_ID) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
            ) { backStackEntry ->
                val creatorEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(AppRoute.CREATORS)
                }
                CreatorVideoFeedScreen(
                    initialIndex = backStackEntry.arguments?.getInt(AppRoute.VIDEO_INDEX) ?: 0,
                    initialPostId = backStackEntry.arguments?.getString(AppRoute.VIDEO_POST_ID).orEmpty(),
                    onBack = { navController.popBackStack() },
                    viewModel = hiltViewModel<CreatorViewModel>(creatorEntry),
                )
            }
            composable(
                route = AppRoute.CREATOR_DETAIL,
                arguments = listOf(
                    navArgument(AppRoute.CREATOR_SLUG) {
                        type = NavType.StringType
                    },
                ),
            ) { backStackEntry ->
                CreatorDetailScreen(
                    creatorSlug = Uri.decode(
                        backStackEntry.arguments?.getString(AppRoute.CREATOR_SLUG).orEmpty(),
                    ),
                    onBack = {
                        navController.popBackStack()
                    },
                )
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
