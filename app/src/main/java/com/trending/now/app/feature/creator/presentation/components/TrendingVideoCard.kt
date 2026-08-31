package com.trending.now.app.feature.creator.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun TrendingVideoCard(
    imageUrl: String?,
    username: String,
    title: String,
    modifier: Modifier = Modifier,
    progress: Float = 0.12f,
    onCardClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onPlatformClick: () -> Unit = {},
) {
    val cardShape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .height(225.dp)
            .width(171.dp)
            .clip(cardShape)
            .background(Color.Transparent)
            .clickable(onClick = onCardClick),
    ) {
        // Backend image
        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        // Bottom gradient for text readability
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

        // Username
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
                .clickable(onClick = onProfileClick)
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

        // Platform icon
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    top = 8.dp,
                    end = 7.dp,
                )
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(onClick = onPlatformClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.ic_instagram,
                ),
                contentDescription = "Instagram",
                modifier = Modifier.size(14.dp),
                tint = Color(0xFFFF4D67),
            )
        }

        // Play button
        Box(
            modifier = Modifier
                .align(Alignment.Center),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_play),
                contentDescription = "Play",
                modifier = Modifier.size(30.dp),
            )
        }

        // Bottom content
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

            VideoProgressBar(
                progress = progress,
            )
        }
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