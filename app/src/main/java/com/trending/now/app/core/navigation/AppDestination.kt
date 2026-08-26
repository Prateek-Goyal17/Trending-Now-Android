package com.trending.now.app.core.navigation

import androidx.annotation.DrawableRes
import com.trending.now.app.R

enum class AppDestination(
    val route: String,
    val label: String,
    @param:DrawableRes val icon: Int,
) {
    Home(AppRoute.HOME, "Home", R.drawable.ic_home),
    Creator(AppRoute.CREATORS, "Creators", R.drawable.ic_creators),
    Me(AppRoute.ME, "Me", R.drawable.ic_me),
}
