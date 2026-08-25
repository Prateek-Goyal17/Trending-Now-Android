package com.trending.now.app.feature.me.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun MeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 15.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        MeHeader()
        ProfileSignupCard()
        TodayInYourWorld()
        CreatorConnectionCard()
        SupportAndPrivacy()
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun MeHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box{
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TrendingNowColors.CardTitle)) {
                        append("Trending")
                    }
                    append("\n")
                    withStyle(SpanStyle(color = TrendingNowColors.RisingCreatorTag)) {
                        append("Now")
                    }
                },
                fontSize = 22.sp,
                lineHeight = 18.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.W900,
                fontFamily = TrendingNowTypography.Inter
            )
            Image(
                painter = painterResource(id = R.drawable.ic_electric),
                contentDescription = "App Logo",
                modifier = Modifier.padding(start =75.dp).size(50.dp).align(Alignment.CenterEnd),
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = "Notifications",
            modifier = Modifier.size(25.dp),
            tint = TrendingNowColors.CardTitle
        )
    }
}

@Composable
private fun ProfileSignupCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(TrendingNowColors.CardSurface)
            .border(1.dp, TrendingNowColors.CardBorder, RoundedCornerShape(10.dp))
            .padding(22.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(19.dp),
    ) {
        ProfilePlaceholder()
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = "Create your profile to personalize your Trending Now experience.",
                color = TrendingNowColors.CardTitle,
                fontSize = 12.sp,
                fontFamily = TrendingNowTypography.Inter,
            )
            SmallSignUpButton()
        }
    }
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

        // Dark ring between gradient and profile background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(TrendingNowColors.Background)
                .padding(3.dp),
            contentAlignment = Alignment.Center,
        ) {

            // Grey profile background
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


@Composable
private fun SmallSignUpButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(6.dp)

    Box(
        modifier = modifier
            .height(33.dp),
    ) {

        // Pink → orange layer visible at the bottom
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 3.dp)
                .clip(shape)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            TrendingNowColors.RisingCreatorTag,
                            TrendingNowColors.SignUpButtonGradientEnd,
                        )
                    )
                )
        )

        // Main white button
        Button(
            onClick = onClick,
            modifier = Modifier
                .height(33.dp),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = TrendingNowColors.CardTitle,
                contentColor = TrendingNowColors.SignUpButtonContent,
            ),
            contentPadding = PaddingValues(
                horizontal = 33.dp,
                vertical = 0.dp,
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
            ),
        ) {
            Text(
                text = "Sign Up",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter
            )
        }
    }
}

@Composable
private fun TodayInYourWorld() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(5.dp, TrendingNowColors.ScreenBorder)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Today in Your World",
                color = TrendingNowColors.CardTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter,
            )
            Text(
                text = "See what you've explored today.",
                color = TrendingNowColors.MutedText,
                fontSize = 11.sp,
                fontFamily = TrendingNowTypography.Inter,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                StatCard("Following", "Creators you follow", TrendingNowColors.RisingCreatorTag, Modifier.weight(1f))
                StatCard("Saved", "Posts saved", TrendingNowColors.SavedStat, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                StatCard("My Activity", "Today's interaction", TrendingNowColors.Genre.Comedy, Modifier.weight(1f))
                StatCard("Time spent", "Time in Trends", TrendingNowColors.TimeSpentStat, Modifier.weight(1f), valueSuffix = "hrs")
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    subtitle: String,
    accent: Color,
    modifier: Modifier = Modifier,
    valueSuffix: String? = null,
) {
    Column(
        modifier = modifier
            .height(96.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(accent.copy(alpha = 0.16f))
            .border(1.dp, accent.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            CircleIcon(accent = accent)
            Text(
                text = title,
                color = TrendingNowColors.CardTitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = TrendingNowTypography.Inter,
            )
        }
        Column {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "--",
                    color = TrendingNowColors.CardTitle,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TrendingNowTypography.Inter,
                )
                valueSuffix?.let {
                    Text(
                        text = it,
                        color = TrendingNowColors.MutedText,
                        fontSize = 8.sp,
                        fontFamily = TrendingNowTypography.Inter,
                    )
                }
            }
            Text(
                text = subtitle,
                color = TrendingNowColors.MutedText,
                fontSize = 10.sp,
                fontFamily = TrendingNowTypography.Inter,
            )
        }
    }
}

@Composable
private fun CircleIcon(accent: Color) {
    Canvas(modifier = Modifier.size(20.dp)) {
        drawCircle(color = accent.copy(alpha = 0.18f))
        drawCircle(color = accent, style = Stroke(width = 1.5.dp.toPx()))
        drawCircle(color = accent, radius = size.minDimension * 0.13f)
    }
}

@Composable
private fun CreatorConnectionCard() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Creator Connection",
            color = TrendingNowColors.CardTitle,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            TrendingNowColors.CreatorSuggestionCardAndTagBackground,
                            TrendingNowColors.CreatorConnectionGradientMiddle,
                            TrendingNowColors.RisingCreatorTag.copy(alpha = 0.55f),
                        ),
                    ),
                )
                .border(1.dp, TrendingNowColors.CardBorder, RoundedCornerShape(18.dp))
                .height(198.dp),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "Save What Stays\nWith You",
                    color = TrendingNowColors.CardTitle,
                    fontSize = 18.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = TrendingNowTypography.Inter,
                )
                Text(
                    text = "- Save the creator\n  moments you love.\n- Every save helps us\n  understand who you\n  enjoy the most.",
                    color = TrendingNowColors.CardTitle,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    fontFamily = TrendingNowTypography.Inter,
                )
                SmallSignUpButton()
            }
            CreatorImagePlaceholder(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp)
                    .fillMaxWidth(0.53f)
                    .aspectRatio(0.78f),
            )
            FloatingHeart(Modifier.align(Alignment.TopEnd).padding(top = 24.dp, end = 22.dp))
            FloatingHeart(Modifier.align(Alignment.TopCenter).padding(start = 78.dp, top = 18.dp))
        }
    }
}

@Composable
private fun CreatorImagePlaceholder(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val strokeWidth = 5.dp.toPx()
        val body = Path().apply {
            moveTo(size.width * 0.25f, size.height * 0.98f)
            cubicTo(size.width * 0.20f, size.height * 0.62f, size.width * 0.42f, size.height * 0.42f, size.width * 0.56f, size.height * 0.58f)
            cubicTo(size.width * 0.78f, size.height * 0.46f, size.width * 0.96f, size.height * 0.66f, size.width * 0.88f, size.height * 0.98f)
            close()
        }
        drawPath(path = body, color = TrendingNowColors.CardTitle, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
        drawPath(path = body, color = TrendingNowColors.CreatorPlaceholderJacket)
        drawCircle(
            color = TrendingNowColors.CardTitle,
            radius = size.width * 0.16f,
            center = Offset(size.width * 0.56f, size.height * 0.36f),
            style = Stroke(width = strokeWidth),
        )
        drawCircle(
            color = TrendingNowColors.CreatorPlaceholderSkin,
            radius = size.width * 0.15f,
            center = Offset(size.width * 0.56f, size.height * 0.36f),
        )
        drawCircle(
            color = TrendingNowColors.CardTitle,
            radius = size.width * 0.13f,
            center = Offset(size.width * 0.28f, size.height * 0.50f),
            style = Stroke(width = strokeWidth),
        )
        drawCircle(
            color = TrendingNowColors.CreatorPlaceholderHair,
            radius = size.width * 0.12f,
            center = Offset(size.width * 0.28f, size.height * 0.50f),
        )
    }
}

@Composable
private fun FloatingHeart(modifier: Modifier = Modifier) {
    Text(
        text = "<3",
        modifier = modifier,
        color = TrendingNowColors.RisingCreatorTag,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = TrendingNowTypography.Inter,
    )
}

@Composable
private fun SupportAndPrivacy() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Support & Privacy",
            color = TrendingNowColors.CardTitle,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter,
        )
        SettingsRow(label = "Report a Problem", leading = "i")
        SettingsRow(label = "Privacy Policy", leading = "s")
        SettingsRow(label = "Community Guideline", leading = "g")
        SettingsRow(
            label = "Log out",
            leading = "<",
            contentColor = TrendingNowColors.Logout,
            borderColor = TrendingNowColors.Logout,
        )
    }
}

@Composable
private fun SettingsRow(
    label: String,
    leading: String,
    contentColor: Color = TrendingNowColors.CardTitle,
    borderColor: Color = TrendingNowColors.CardBorder,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(TrendingNowColors.CardSurface)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = leading,
                color = contentColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter,
            )
            Text(
                text = label,
                color = contentColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter,
            )
        }
        Text(
            text = ">",
            color = contentColor,
            fontSize = 18.sp,
            fontFamily = TrendingNowTypography.Inter,
        )
    }
}
