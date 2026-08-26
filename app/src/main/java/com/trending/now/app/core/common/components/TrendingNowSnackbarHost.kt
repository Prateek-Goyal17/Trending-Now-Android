package com.trending.now.app.core.common.components

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.trending.now.app.core.constants.TrendingNowColors

@Composable
fun TrendingNowSnackbarHost(
    hostState: SnackbarHostState,
) {
    SnackbarHost(hostState = hostState) { snackbarData ->
        TrendingNowSnackbar(snackbarData = snackbarData)
    }
}

@Composable
private fun TrendingNowSnackbar(
    snackbarData: SnackbarData,
) {
    Snackbar(
        containerColor = TrendingNowColors.CardSurface,
        contentColor = TrendingNowColors.CardTitle,
        actionContentColor = TrendingNowColors.AuthTitleGradientStart,
    ) {
        Text(text = snackbarData.visuals.message)
    }
}
