package com.trending.now.app.feature.me.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.common.components.GradientAccentButton
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.auth.domain.model.AuthState

@Composable
fun CreatorConnectionCard(
    authState: AuthState,
    onSignUpClick: () -> Unit,
    onFindFavouritesClick: () -> Unit,
) {
    Column {
        Text(
            text = "Creator Connection",
            color = TrendingNowColors.CardTitle,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter,
        )
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            TrendingNowColors.CreatorSuggestionCardAndTagBackground,
                            TrendingNowColors.CreatorConnectionGradientMiddle,
                            TrendingNowColors.RisingCreatorTag.copy(alpha = 0.55f),
                        ),
                    ),
                )
                .border(1.dp, Color(0xFF442B33), RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 26.dp, top = 26.dp, bottom = 22.dp),
            ) {
                Text(
                    text = "Save What Stays\nWith You",
                    color = TrendingNowColors.CardTitle,
                    fontSize = 20.sp,
                    lineHeight = 25.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = TrendingNowTypography.Anton,
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    text = "• Save the creator\n   moments you love.\n• Every save helps us\n   understand who you\n   enjoy the most.",
                    color = TrendingNowColors.CardContent,
                    lineHeight = 16.sp,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = TrendingNowTypography.Inter,
                )
                Spacer(Modifier.height(16.dp))
                GradientAccentButton(
                    contentPadding = PaddingValues(
                        horizontal = when (authState) {
                            is AuthState.NewUser -> 25.dp
                            AuthState.Guest,
                            AuthState.LoggedOut,
                                -> 33.dp
                            is AuthState.OldUser -> 33.dp
                        }
                    ),
                    text = when (authState) {
                        is AuthState.NewUser -> "Find Favourites"
                        AuthState.Guest,
                        AuthState.LoggedOut,
                        -> "Sign Up"
                        is AuthState.OldUser -> "Visit Profile"
                    },
                    onClick = when (authState) {
                        is AuthState.NewUser -> onFindFavouritesClick
                        AuthState.Guest,
                        AuthState.LoggedOut,
                        -> onSignUpClick
                        is AuthState.OldUser -> onFindFavouritesClick
                    },
                )
            }

            Image(
                painter = painterResource(R.drawable.profile_creator_connection),
                contentDescription = "Profile Creator",
                modifier = Modifier.align(Alignment.BottomEnd).height(240.dp).padding(end = 10.dp)
            )
        }
    }
}
