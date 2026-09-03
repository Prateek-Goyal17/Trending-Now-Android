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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun CreatorPostCard(
    modifier: Modifier = Modifier,
    tagLabel: String = "Instagram",
    tagIcon: Int = R.drawable.ic_instagram,
    postImageRes: Int = R.drawable.ic_samay_home,
    description: String = "\"Samay Raina: Still Alive\" is a deeply personal stand-up comedy special released on YouTube in April 2026, marking the comedian's triumphant return after stepping away from the spotlight.",
    content: @Composable BoxScope.() -> Unit = {}
) {

    val cardShape = RoundedCornerShape(17.65.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(Color(0xFF1F1115))
            .innerShadow(
                shape = cardShape,
                color = Color(0x8CFF2D88),
                blur = 1.dp,
                offsetX = 0.44.dp,
                offsetY = 0.44.dp
            )
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = cardShape
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .align(Alignment.TopCenter)
                .background(
                    brush = Brush.verticalGradient(
                        0.0f to Color(0xFF5C1949).copy(alpha = 0.65f),
                        0.25f to Color(0xFF401633).copy(alpha = 0.50f),
                        0.50f to Color(0xFF281326).copy(alpha = 0.30f),
                        0.75f to Color.Transparent,
                        1.0f to Color.Transparent
                    )
                )
        )

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 17.65.dp,
                            topEnd = 17.65.dp
                        )
                    )
            ) {

                Image(
                    painter = painterResource(
                        id = postImageRes
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Image(
                    painter = painterResource(
                        id = R.drawable.ic_play
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFFFF2D88),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
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

                Box(
                    modifier = Modifier
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_home_save),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(Color(0xFFFF2D88))
                    )
                }
            }

            Text(
                text = description,
                color = Color.White,
                fontSize = 13.sp,
                fontFamily = TrendingNowTypography.Inter,
                lineHeight = 18.sp,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Color(0x805A1833))
            )

            CommentsShown()

            CommentInput(
                modifier = Modifier
                    .padding(
                        start = 13.dp,
                        end = 13.dp,
                        bottom = 12.dp
                    )
            )
        }

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

        val shadowOutline = shape.createOutline(
            size = size,
            layoutDirection = layoutDirection,
            density = this
        )

        val path = Path().apply {
            addOutline(shadowOutline)
        }

        canvas.save()

        canvas.clipPath(path)

        canvas.translate(
            offsetX.toPx(),
            offsetY.toPx()
        )

        val strokePaint = Paint().apply {
            style = PaintingStyle.Stroke
            strokeWidth = blur.toPx()
            this.color = color
        }

        strokePaint
            .asFrameworkPaint()
            .maskFilter = BlurMaskFilter(
            blur.toPx(),
            BlurMaskFilter.Blur.NORMAL
        )

        canvas.drawOutline(
            shadowOutline,
            strokePaint
        )

        canvas.restore()
    }
}
