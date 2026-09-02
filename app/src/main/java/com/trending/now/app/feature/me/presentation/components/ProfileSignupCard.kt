package com.trending.now.app.feature.me.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.common.components.GradientAccentButton
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.auth.domain.model.AuthProfile
import com.trending.now.app.feature.auth.domain.model.AuthState

@Composable
fun ProfileSignupCard(
    authState: AuthState,
    onSignUpClick: () -> Unit,
) {
    when (authState) {
        AuthState.Guest,
        AuthState.LoggedOut,
        -> SignupCard(onSignUpClick = onSignUpClick)

        is AuthState.NewUser -> UserProfileCard(
            profile = authState.profile,
            onEditProfileClick = onSignUpClick,
        )

        is AuthState.ExistingUser -> UserProfileCard(
            profile = authState.profile,
            onEditProfileClick = onSignUpClick,
        )
    }
}

@Composable
private fun SignupCard(
    onSignUpClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(TrendingNowColors.CardSurface)
            .border(1.dp, TrendingNowColors.CardBorder.copy(0.15f), RoundedCornerShape(12.dp))
            .padding(22.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(19.dp),
    ) {
        ProfilePlaceholder()
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "Create your profile to personalize your Trending Now experience.",
                color = TrendingNowColors.CardTitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = TrendingNowTypography.Inter,
            )
            GradientAccentButton(
                text = "Sign Up",
                onClick = onSignUpClick
            )
        }
    }
}

@Composable
private fun UserProfileCard(
    profile: AuthProfile,
    onEditProfileClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(TrendingNowColors.CardSurface)
            .border(1.dp, TrendingNowColors.CardBorder.copy(0.15f), RoundedCornerShape(10.dp))
            .padding(vertical = 11.dp)
            .padding(start = 21.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(19.dp),
    ) {
        InitialsAvatar(initials = profile.initials())
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = profile.displayName(),
                color = TrendingNowColors.CardTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = TrendingNowTypography.Inter,
            )
            GradientAccentButton(
                text = "Edit Profile",
                onClick = onEditProfileClick
            )
        }
    }
}

@Composable
private fun InitialsAvatar(
    initials: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(Brush.verticalGradient(TrendingNowColors.UserProfileStrokeGradient))
            .padding(2.5.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(TrendingNowColors.Background)
                .padding(3.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF9426F0),
                                Color(0xFF5B12C9),
                            ),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = initials,
                    color = TrendingNowColors.CardTitle,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = TrendingNowTypography.Inter,
                )
            }
        }
    }
}

private fun AuthProfile.displayName(): String {
    return username
        ?: listOfNotNull(firstName, lastName)
            .joinToString(" ")
            .takeIf { it.isNotBlank() }
        ?: email
        ?: "User"
}

private fun AuthProfile.initials(): String {
    val names = listOfNotNull(firstName, lastName)
        .mapNotNull { name -> name.firstOrNull()?.uppercaseChar()?.toString() }
        .take(2)
        .joinToString("")

    return names.takeIf { it.length == 2 }
        ?: displayName()
            .filter { it.isLetterOrDigit() }
            .take(2)
            .uppercase()
            .ifBlank { "U" }
}


@Composable
fun ProfilePlaceholder(
    modifier: Modifier = Modifier,
    size: Dp = 76.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.verticalGradient(TrendingNowColors.UserProfileStrokeGradient))
            .padding(3.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(TrendingNowColors.Background)
                .padding(3.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(TrendingNowColors.CardContent),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_person),
                    contentDescription = "Profile",
                    modifier = Modifier.size(size * 0.36f),
                    tint = TrendingNowColors.ProfilePlaceholderContent,
                )
            }
        }
    }
}
