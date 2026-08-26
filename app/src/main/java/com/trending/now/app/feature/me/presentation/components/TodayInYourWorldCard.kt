package com.trending.now.app.feature.me.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import kotlin.io.path.Path


@Composable
fun TodayInYourWorld() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(TrendingNowColors.TodayPanelBackground)
            .todayPanelGradientBorder(
                width = 1.dp,
                cornerRadius = 10.dp,
            )
            .padding(horizontal = 22.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Today in Your World",
                color = TrendingNowColors.CardTitle,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter,
            )
            Text(
                text = "See what you've explored today.",
                color = TrendingNowColors.CardContent,
                fontSize = 14.sp,
                fontFamily = TrendingNowTypography.Inter,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(22.dp)) {
                StatCard(
                    title = "Following",
                    subtitle = "Creators you follow",
                    valueSuffix = null,
                    icon = R.drawable.ic_profile_following,
                    innerShadowColor = Color(0xFF3F132D),
                    gradient = TrendingNowColors.TodayFollowingGradient,
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    title = "Saved",
                    subtitle = "Posts saved",
                    valueSuffix = null,
                    icon = R.drawable.ic_profile_saved,
                    innerShadowColor = Color(0xFF2B1142),
                    gradient = TrendingNowColors.TodaySavedGradient,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(22.dp)) {
                StatCard(
                    title = "My Activity",
                    subtitle = "Today's interaction",
                    valueSuffix = null,
                    icon = R.drawable.ic_profile_activity,
                    innerShadowColor = Color(0xFF3D1F1E),
                    gradient = TrendingNowColors.TodayActivityGradient,
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    title = "Time spent",
                    subtitle = "Time in Trends",
                    valueSuffix = "hrs",
                    icon = R.drawable.ic_profile_time,
                    innerShadowColor = Color(0xFF25184C),
                    gradient = TrendingNowColors.TodayTimeSpentGradient,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    subtitle: String,
    valueSuffix: String?,
    icon: Int,
    innerShadowColor: Color,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .height(118.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF120E1B))
            .statCardInnerShadow(
                cornerRadius = 8.dp,
                color = innerShadowColor,
                blur = 60.dp,
                offsetX = 8.dp,
                offsetY = 4.dp,
            )
            .gradientCardBorder(
                colors = gradient,
                width = 1.dp,
                cornerRadius = 8.dp,
            )
            .padding(horizontal = 17.dp, vertical = 17.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
            )
            Text(
                text = title,
                color = TrendingNowColors.CardTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = TrendingNowTypography.Anton,
            )
        }
        Column {
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "--",
                    color = TrendingNowColors.CardTitle,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TrendingNowTypography.Inter,
                )
                valueSuffix?.let {
                    Text(
                        text = it,
                        color = TrendingNowColors.MutedText,
                        fontSize = 13.sp,
                        fontFamily = TrendingNowTypography.Inter,
                    )
                }
            }
            Spacer(Modifier.height(3.dp))
            Text(
                text = subtitle,
                color = TrendingNowColors.CardContent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = TrendingNowTypography.Inter,
            )
        }
    }
}


fun Modifier.gradientCardBorder(
    colors: List<Color>,
    width: Dp = 1.dp,
    cornerRadius: Dp = 8.dp,
): Modifier = drawWithCache {
    val strokeWidth = width.toPx()
    val radius = cornerRadius.toPx()

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = Offset(size.width, size.height),
    )

    onDrawWithContent {
        drawContent()

        drawRoundRect(
            brush = brush,
            topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
            size = Size(
                width = size.width - strokeWidth,
                height = size.height - strokeWidth,
            ),
            cornerRadius = CornerRadius(radius, radius),
            style = Stroke(width = strokeWidth),
        )
    }
}


fun Modifier.statCardInnerShadow(
    cornerRadius: Dp = 8.dp,
    color: Color = Color(0xFF3F132D),
    blur: Dp = 50.dp,
    offsetX: Dp = 8.dp,
    offsetY: Dp = 4.dp,
): Modifier = drawWithCache {
    val radiusPx = cornerRadius.toPx()
    val blurPx = blur.toPx()
    val dx = offsetX.toPx()
    val dy = offsetY.toPx()

    val clipPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(Offset.Zero, size),
                cornerRadius = CornerRadius(radiusPx, radiusPx),
            ),
        )
    }

    // Stronger on top/left, subtler on bottom/right
    val strong = color.copy(alpha = 0.28f)
    val medium = color.copy(alpha = 0.28f)
    val subtle = color.copy(alpha = 0.28f)

    val topBrush = Brush.verticalGradient(
        colors = listOf(strong, Color.Transparent),
        startY = 0f,
        endY = blurPx,
    )

    val leftBrush = Brush.horizontalGradient(
        colors = listOf(strong, Color.Transparent),
        startX = 0f,
        endX = blurPx,
    )

    val rightBrush = Brush.horizontalGradient(
        colors = listOf(Color.Transparent, subtle),
        startX = size.width - blurPx,
        endX = size.width,
    )

    val bottomBrush = Brush.verticalGradient(
        colors = listOf(Color.Transparent, subtle),
        startY = size.height - blurPx,
        endY = size.height,
    )

    val topLeftCorner = Brush.radialGradient(
        colors = listOf(medium, Color.Transparent),
        center = Offset(radiusPx + dx * 0.2f, radiusPx + dy * 0.2f),
        radius = blurPx,
    )

    onDrawWithContent {
        drawContent()

        clipPath(clipPath) {
            // top
            drawRect(
                brush = topBrush,
                topLeft = Offset(0f, 0f),
                size = androidx.compose.ui.geometry.Size(
                    width = size.width,
                    height = blurPx + dy,
                ),
            )

            // left
            drawRect(
                brush = leftBrush,
                topLeft = Offset(0f, 0f),
                size = androidx.compose.ui.geometry.Size(
                    width = blurPx + dx,
                    height = size.height,
                ),
            )

            // bottom
            drawRect(
                brush = bottomBrush,
                topLeft = Offset(0f, size.height - blurPx),
                size = androidx.compose.ui.geometry.Size(
                    width = size.width,
                    height = blurPx,
                ),
            )

            // right
            drawRect(
                brush = rightBrush,
                topLeft = Offset(size.width - blurPx, 0f),
                size = androidx.compose.ui.geometry.Size(
                    width = blurPx,
                    height = size.height,
                ),
            )

            // extra soft corner glow
            drawRect(
                brush = topLeftCorner,
                topLeft = Offset.Zero,
                size = size,
            )
        }
    }
}


fun Modifier.todayPanelGradientBorder(
    width: Dp = 1.dp,
    cornerRadius: Dp = 10.dp,
): Modifier = drawWithCache {

    val strokeWidth = width.toPx()
    val radius = cornerRadius.toPx()

    val borderBrush = Brush.linearGradient(
        colorStops = arrayOf(
            0.00f to Color(0xFF141729),
            0.53f to Color(0xFF141729),
            0.84f to Color(0xFF474550),
            1.00f to Color(0xFF474550),
        ),
        // Figma gradient direction:
        // bottom-left → top-right
        start = Offset(
            x = 0f,
            y = size.height,
        ),
        end = Offset(
            x = size.width,
            y = 0f,
        ),
    )

    onDrawWithContent {
        drawContent()

        drawRoundRect(
            brush = borderBrush,
            topLeft = Offset(
                x = strokeWidth / 2f,
                y = strokeWidth / 2f,
            ),
            size = Size(
                width = size.width - strokeWidth,
                height = size.height - strokeWidth,
            ),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                x = radius,
                y = radius,
            ),
            style = Stroke(width = strokeWidth),
        )
    }
}