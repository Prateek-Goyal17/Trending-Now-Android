package com.trending.now.app.feature.home.presentation.components

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun CreatorPostCard(
    modifier: Modifier = Modifier,
    tagLabel: String = "Instagram",
    tagIcon: Int = R.drawable.ic_instagram,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .width(380.dp)
            .height(540.dp)
            .innerShadow(
                shape = RoundedCornerShape(17.65.dp),
                color = Color(0x8CFF2D88), // 55% opacity
                blur = 3.53.dp,
                offsetX = 0.44.dp,
                offsetY = 0.44.dp
            )
            .clip(RoundedCornerShape(17.65.dp))
            .background(Color(0xFF1F1115))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Main Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(topStart = 17.65.dp, topEnd = 17.65.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_samay_home),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Play Button Overlay
                Image(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }

            // Info Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Instagram Tag
                Row(
                    modifier = Modifier
                        .border(1.dp, Color(0xFFFF2D88), RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Image(
                        painter = painterResource(id = tagIcon),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = tagLabel,
                        color = Color(0xFFFF2D88),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = TrendingNowTypography.Inter
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "• 2 days ago",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontFamily = TrendingNowTypography.Inter
                )

                Spacer(modifier = Modifier.weight(1f))

                // Bookmark Icon
                Image(
                    painter = painterResource(id = R.drawable.ic_profile_saved),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(Color(0xFFFF2D88))
                )
            }

            // Description
            Text(
                text = "\"Samay Raina: Still Alive\" is a deeply personal stand-up comedy special released on YouTube in April 2026, marking the comedian's triumphant return after stepping away from the spotlight.",
                color = Color.White,
                fontSize = 13.sp,
                fontFamily = TrendingNowTypography.Inter,
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Color.DarkGray)
            )

            // Comment Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFF2D88)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "KR",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "@kapil_r",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontFamily = TrendingNowTypography.Inter
                    )
                    Text(
                        text = "Well deserved 😃",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = TrendingNowTypography.Inter
                    )
                }
            }
        }

        CommentInput(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    start = 13.dp,
                    end = 13.dp,
                    bottom = 12.dp
                )
        )

        content()
    }
}

fun Modifier.innerShadow(
    shape: Shape,
    color: Color,
    blur: Dp,
    offsetX: Dp,
    offsetY: Dp
) = drawWithContent {
    drawContent()
    drawIntoCanvas { canvas ->
        val shadowOutline = shape.createOutline(size, layoutDirection, this)
        val path = Path().apply {
            addOutline(shadowOutline)
        }

        canvas.save()
        canvas.clipPath(path)

        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        paint.color = color
        frameworkPaint.maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)

        canvas.translate(offsetX.toPx(), offsetY.toPx())

        val strokePaint = Paint().apply {
            this.style = PaintingStyle.Stroke
            this.strokeWidth = blur.toPx()
            this.color = color
        }
        strokePaint.asFrameworkPaint().maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        canvas.drawOutline(shadowOutline, strokePaint)

        canvas.restore()
    }
}
