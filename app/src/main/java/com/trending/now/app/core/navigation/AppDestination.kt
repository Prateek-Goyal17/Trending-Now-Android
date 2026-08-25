package com.trending.now.app.core.navigation

import androidx.annotation.DrawableRes
import com.trending.now.app.R

enum class AppDestination(
    val route: String,
    val label: String,
    @param:DrawableRes val icon: Int,
) {
    Home("home", "Home", R.drawable.ic_home),
    Creator("creators", "Creators", R.drawable.ic_creators),
    Me("me", "Me", R.drawable.ic_me),
}
