package com.trending.now.app.core.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.core.navigation.AppDestination

@Composable
fun TrendingNowBottomBar(
    currentDestination: AppDestination,
    onDestinationClick: (AppDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(86.dp)
                .background(TrendingNowColors.NavigationBackground),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(TrendingNowColors.NavigationDivider),
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BottomBarItem(
                    modifier = Modifier.weight(1f),
                    destination = AppDestination.Home,
                    selected = currentDestination == AppDestination.Home,
                    onClick = { onDestinationClick(AppDestination.Home) },
                )

                Spacer(modifier = Modifier.weight(1f))

                BottomBarItem(
                    modifier = Modifier.weight(1f),
                    destination = AppDestination.Me,
                    selected = currentDestination == AppDestination.Me,
                    onClick = { onDestinationClick(AppDestination.Me) },
                )
            }
        }

        CenterCreatorItem(
            selected = currentDestination == AppDestination.Creator,
            onClick = { onDestinationClick(AppDestination.Creator) },
        )
    }
}

@Composable
private fun CenterCreatorItem(
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(TrendingNowColors.NavigationCenterSurface)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onClick,
                )
                .border(
                    width = 1.dp,
                    color = if (selected) TrendingNowColors.NavigationSelected else TrendingNowColors.NavigationCenterBorder,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = AppDestination.Creator.icon),
                contentDescription = AppDestination.Creator.label,
                tint = if (selected) TrendingNowColors.NavigationSelected else TrendingNowColors.NavigationInactive,
                modifier = Modifier.size(32.dp),
            )
        }

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = AppDestination.Creator.label,
            color = if (selected) TrendingNowColors.NavigationSelected else TrendingNowColors.NavigationInactive,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = TrendingNowTypography.Inter
        )
    }
}

@Composable
private fun BottomBarItem(
    destination: AppDestination,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (selected) {
        TrendingNowColors.NavigationSelected
    } else {
        TrendingNowColors.NavigationInactive
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(destination.icon),
            contentDescription = destination.label,
            tint = contentColor,
            modifier = Modifier.size(25.dp),
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = destination.label,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = TrendingNowTypography.Inter
        )
    }
}
