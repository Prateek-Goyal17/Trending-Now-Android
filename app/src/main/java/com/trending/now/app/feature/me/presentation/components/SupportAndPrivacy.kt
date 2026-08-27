package com.trending.now.app.feature.me.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun SupportAndPrivacy(
    onReportProblemClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onCommunityGuidelineClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Support & Privacy",
            color = TrendingNowColors.CardTitle,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter,
        )
        SettingsRow(
            title = "Report a Problem",
            prefixIcon = R.drawable.ic_report,
            suffixIcon = R.drawable.ic_right_arrow,
            onClick = onReportProblemClick
        )
        SettingsRow(
            title = "Privacy Policy",
            prefixIcon = R.drawable.ic_privacy,
            suffixIcon = R.drawable.ic_right_arrow,
            onClick = onPrivacyPolicyClick
        )
        SettingsRow(
            title = "Community Guideline",
            prefixIcon = R.drawable.ic_community,
            suffixIcon = R.drawable.ic_right_arrow,
            onClick = onCommunityGuidelineClick
        )
        SettingsRow(
            title = "Log out",
            prefixIcon = R.drawable.ic_logout,
            suffixIcon = R.drawable.ic_right_arrow,
            contentColor = Color(0xFFFD4C35),
            borderColor = Color(0xFFFD4C35),
            onClick = onLogoutClick
        )

    }
}
@Composable
fun SettingsRow(
    title: String,
    @DrawableRes prefixIcon: Int,
    @DrawableRes suffixIcon: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = TrendingNowColors.CardTitle,
    borderColor: Color = TrendingNowColors.CardBorder.copy(0.15f),
    backgroundColor: Color = TrendingNowColors.TodayPanelBackground,
    height: Dp = 59.dp,
    cornerRadius: Dp = 10.dp,
    iconSize: Dp = 20.dp,
    suffixIconSize: Dp = 18.dp,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(cornerRadius)

    Row(
        modifier = modifier.run {
            fillMaxWidth()
                .height(height)
                .clip(shape)
                .background(backgroundColor)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = shape,
                )
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                )
                .padding(horizontal = 19.dp)
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            painter = painterResource(id = prefixIcon),
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            colorFilter = ColorFilter.tint(contentColor),
        )

        Spacer(
            modifier = Modifier.width(12.dp),
        )

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = contentColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = TrendingNowTypography.Inter,
        )

        Image(
            painter = painterResource(id = suffixIcon),
            contentDescription = null,
            modifier = Modifier.size(suffixIconSize),
            colorFilter = ColorFilter.tint(contentColor),
        )
    }
}
