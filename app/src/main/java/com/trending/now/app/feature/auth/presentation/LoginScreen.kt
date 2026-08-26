package com.trending.now.app.feature.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun LoginScreen(
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit,
    onGuestClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding(),
    ) {
        AuthBackgroundPlaceholders()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TrendingNowLoginTitle()
            Spacer(Modifier.height(26.dp))
            Text(
                text = "Your scape, All in one place.",
                color = TrendingNowColors.CardTitle,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = TrendingNowTypography.Inter,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(30.dp))
            SocialIconRow()
            Spacer(Modifier.height(74.dp))
            LoginButton(
                text = "Continue with Google",
                leading = { GoogleMark() },
                onClick = onGoogleClick,
            )
            Spacer(Modifier.height(34.dp))
            LoginButton(
                text = "Continue with Apple",
                leading = {
                    Text(
                        text = "A",
                        color = TrendingNowColors.AuthButtonText,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = TrendingNowTypography.Inter,
                    )
                },
                onClick = onAppleClick,
            )
            Spacer(Modifier.height(42.dp))
            OrDivider()
            Spacer(Modifier.height(42.dp))
            GuestButton(onClick = onGuestClick)
        }
    }
}

@Composable
private fun TrendingNowLoginTitle() {
    Text(
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            TrendingNowColors.AuthTitleGradientStart,
                            TrendingNowColors.AuthTitleGradientEnd,
                        ),
                    ),
                ),
            ) {
                append("Trending\nNow")
            }
            withStyle(SpanStyle(color = TrendingNowColors.AuthTitleGradientStart)) {
                append(" z")
            }
        },
        fontSize = 44.sp,
        lineHeight = 44.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.W900,
        fontFamily = TrendingNowTypography.Inter,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun LoginButton(
    text: String,
    leading: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        TrendingNowColors.AuthButtonGlow,
                        TrendingNowColors.AuthGuestBorderEnd,
                    ),
                ),
                shape = RoundedCornerShape(12.dp),
            )
            .padding(1.dp)
            .clip(RoundedCornerShape(11.dp))
            .background(TrendingNowColors.CardTitle)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 46.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) {
            leading()
        }
        Text(
            text = text,
            color = TrendingNowColors.AuthButtonText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter,
        )
    }
}

@Composable
private fun GuestButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        TrendingNowColors.AuthTitleGradientStart,
                        TrendingNowColors.AuthGuestBorderEnd,
                    ),
                ),
                shape = RoundedCornerShape(14.dp),
            )
            .padding(2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(TrendingNowColors.Background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 54.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(TrendingNowColors.AuthTitleGradientStart),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(TrendingNowColors.Background),
            )
        }
        Text(
            text = "Continue as Guest",
            color = TrendingNowColors.SignUpButtonContent,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter,
        )
    }
}

@Composable
private fun SocialIconRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        listOf("IG", "YT", "X", "TN").forEach { label ->
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(TrendingNowColors.AuthTitleGradientStart.copy(alpha = 0.22f))
                    .border(1.dp, TrendingNowColors.CardContent.copy(alpha = 0.48f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    color = TrendingNowColors.SignUpButtonContent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TrendingNowTypography.Inter,
                )
            }
        }
    }
}

@Composable
private fun OrDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(TrendingNowColors.AuthDivider),
        )
        Text(
            text = "or",
            modifier = Modifier.padding(horizontal = 18.dp),
            color = TrendingNowColors.CardTitle,
            fontSize = 16.sp,
            fontFamily = TrendingNowTypography.Inter,
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(TrendingNowColors.AuthDivider),
        )
    }
}

@Composable
private fun GoogleMark() {
    Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
        Text("G", color = TrendingNowColors.AuthGoogleBlue, fontSize = 25.sp, fontWeight = FontWeight.Black)
        Text("o", color = TrendingNowColors.AuthGoogleRed, fontSize = 20.sp, fontWeight = FontWeight.Black)
        Text("o", color = TrendingNowColors.AuthGoogleYellow, fontSize = 20.sp, fontWeight = FontWeight.Black)
        Text("g", color = TrendingNowColors.AuthGoogleBlue, fontSize = 20.sp, fontWeight = FontWeight.Black)
        Text("l", color = TrendingNowColors.AuthGoogleGreen, fontSize = 20.sp, fontWeight = FontWeight.Black)
        Text("e", color = TrendingNowColors.AuthGoogleRed, fontSize = 20.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun BoxScope.AuthBackgroundPlaceholders() {
    BackgroundCard(
        label = "Comedy",
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = (-24).dp, y = 142.dp)
            .rotate(-12f),
        color = TrendingNowColors.AuthHeroCard,
    )
    BackgroundCard(
        label = "Gaming",
        modifier = Modifier
            .align(Alignment.TopEnd)
            .offset(x = 34.dp, y = 274.dp)
            .rotate(12f),
        color = TrendingNowColors.AuthHeroCard,
    )
    BackgroundCard(
        label = "Beauty",
        modifier = Modifier
            .align(Alignment.BottomStart)
            .offset(x = (-10).dp, y = (-164).dp)
            .rotate(-8f),
        color = TrendingNowColors.AuthHeroCard,
    )
    BackgroundCard(
        label = "Travel",
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(x = 28.dp, y = (-56).dp)
            .rotate(8f),
        color = TrendingNowColors.AuthHeroCardAlt,
    )
    HashtagPill(
        text = "# Comedy",
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = 40.dp, y = 96.dp)
            .rotate(-10f),
        borderColor = TrendingNowColors.AuthPillPurple,
    )
    HashtagPill(
        text = "# Explore",
        modifier = Modifier
            .align(Alignment.CenterStart)
            .offset(x = 24.dp, y = (-104).dp)
            .rotate(-12f),
        borderColor = TrendingNowColors.AuthPillBlue,
    )
    HashtagPill(
        text = "# Gaming",
        modifier = Modifier
            .align(Alignment.TopEnd)
            .offset(x = 14.dp, y = 196.dp)
            .rotate(18f),
        borderColor = TrendingNowColors.AuthPillOrange,
    )
    HashtagPill(
        text = "# Roast",
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .offset(x = 14.dp, y = 246.dp)
            .rotate(10f),
        borderColor = TrendingNowColors.Logout,
    )
    HashtagPill(
        text = "# Travel",
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(x = (-44).dp, y = (-244).dp)
            .rotate(10f),
        borderColor = TrendingNowColors.Genre.Travel,
    )
}

@Composable
private fun BackgroundCard(
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(160.dp)
            .height(124.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.42f))
            .border(1.dp, TrendingNowColors.AuthTitleGradientStart.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        contentAlignment = Alignment.BottomStart,
    ) {
        Text(
            text = label,
            color = TrendingNowColors.CardTitle.copy(alpha = 0.28f),
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            fontFamily = TrendingNowTypography.Anton,
        )
    }
}

@Composable
private fun HashtagPill(
    text: String,
    borderColor: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .border(1.dp, borderColor.copy(alpha = 0.72f), RoundedCornerShape(24.dp))
            .padding(horizontal = 18.dp, vertical = 8.dp),
        color = borderColor.copy(alpha = 0.9f),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = TrendingNowTypography.Inter,
    )
}
