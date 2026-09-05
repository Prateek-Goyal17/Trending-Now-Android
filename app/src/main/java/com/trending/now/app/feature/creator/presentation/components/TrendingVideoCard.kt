package com.trending.now.app.feature.creator.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun TrendingVideoCard(
    imageUrl: String?,
    username: String,
    title: String,
    platform: String,
    modifier: Modifier = Modifier,
    videoUrl: String? = null,
    autoPlay: Boolean = false,
    onCardClick: () -> Unit = {},
) {
    val cardShape = RoundedCornerShape(8.dp)
    val platformIcon = platformIconFor(platform)
    val platformLabel = platformLabelFor(platform)
    val playback = rememberVideoPlayback(videoUrl = videoUrl, enabled = autoPlay)

    Box(
        modifier = modifier
            .height(225.dp)
            .width(171.dp)
            .clip(cardShape)
            .background(Color.Transparent)
            .clickable(onClick = onCardClick),
    ) {
        TrendingVideoSurface(
            playback = playback,
            posterUrl = imageUrl,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0x33FF2D88),
                            Color(0xCCFF4D67),
                        ),
                    ),
                ),
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = 10.dp,
                    top = 9.dp,
                )
                .clip(RoundedCornerShape(10.dp))
                .innerShadow(
                    shape = RoundedCornerShape(10.dp),
                    shadow = Shadow(
                        radius = 7.2.dp,
                        spread = 1.dp,
                        color = Color(0xFFFF2D88).copy(alpha = 0.55f),
                        offset = DpOffset(
                            x = 0.dp,
                            y = 4.dp,
                        ),
                    ),
                )
                .border(
                    width = 0.5.dp,
                    color = Color(0xFFFF2D88),
                    shape = RoundedCornerShape(10.dp),
                )
                .padding(
                    horizontal = 10.dp,
                    vertical = 6.dp,
                ),
        ) {
            Text(
                text = "@$username",
                color = Color(0xFFFF2D88),
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = TrendingNowTypography.Inter,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 8.dp,
                    end = 7.dp,
                )
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(platformIcon),
                contentDescription = platformLabel,
                modifier = Modifier.size(14.dp),
            )
        }

        if (!playback.isPlaying) Box(
            modifier = Modifier
                .align(Alignment.Center),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_play),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 8.dp,
                ),
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = TrendingNowTypography.Anton,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(
                modifier = Modifier.height(13.dp),
            )

            if (videoUrl != null) {
                VideoProgressBar(progress = playback.progress)
            }
        }
    }
}

@DrawableRes
private fun platformIconFor(platform: String): Int {
    return when (platform.lowercase()) {
        "instagram", "ig" -> R.drawable.ic_instagram
        "x", "twitter", "tw" -> R.drawable.ic_twitter
        "youtube", "shorts", "short" -> R.drawable.ic_youtube
        "news" -> R.drawable.ic_creator_news
        else -> R.drawable.ic_creator_news
    }
}

private fun platformLabelFor(platform: String): String {
    return when (platform.lowercase()) {
        "instagram", "ig" -> "Instagram"
        "x", "twitter", "tw" -> "X"
        "youtube", "shorts", "short" -> "YouTube"
        "news" -> "News"
        else -> "Platform"
    }
}

@Composable
private fun VideoProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp)
            .clip(CircleShape)
            .background(Color(0xFFC9C9C9)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(
                    fraction = progress.coerceIn(0f, 1f),
                )
                .fillMaxHeight()
                .clip(CircleShape)
                .background(Color(0xFFFF1D20)),
        )
    }
}
