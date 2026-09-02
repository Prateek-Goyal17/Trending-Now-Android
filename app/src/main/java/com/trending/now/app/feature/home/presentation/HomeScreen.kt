@file:Suppress("DEPRECATION")

package com.trending.now.app.feature.home.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.Paint
import android.graphics.BlurMaskFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.home.presentation.components.CommunityFeedbackCard
import com.trending.now.app.feature.home.presentation.components.CreatorPostCard
import com.trending.now.app.feature.home.presentation.components.CreatorReactionCard
import com.trending.now.app.feature.me.presentation.components.MeHeader
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding()
            .padding(contentPadding)
    ) {
        item {
            Box(
                modifier = Modifier.padding(
                    start = 15.dp,
                    end = 15.dp,
                    top = 18.dp
                )
            ) {
                MeHeader()
            }
        }

        item {
            CreatorKingsCarousel()
        }

        item {
            CreatorInfoCard(
                modifier = Modifier.padding(
                    start = 19.dp,
                    end = 19.dp,
                    top = 8.dp
                )
            )
        }

        item {
            CreatorReactionCard(
                modifier = Modifier.padding(
                    start = 17.dp,
                    end = 17.dp,
                    top = 6.dp,
                    bottom = 6.dp
                )
            )
        }

        item {
            CreatorPostCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 6.dp,
                    bottom = 6.dp
                ),
                tagLabel = "Instagram",
                tagIcon = R.drawable.ic_instagram
            )
        }

        item {
            CreatorPostCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 6.dp,
                    bottom = 12.dp
                ),
                tagLabel = "Shorts",
                tagIcon = R.drawable.ic_youtube
            )
        }

        item {
            CommunityFeedbackCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 6.dp,
                    bottom = 24.dp
                )
            )
        }
    }
}

@Composable
private fun CreatorKingsCarousel() {

    val listState = rememberLazyListState()

    val activeIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2f
            layoutInfo.visibleItemsInfo
                .minByOrNull { abs((it.offset + it.size / 2f) - viewportCenter) }
                ?.index ?: 1
        }
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(1)

        val sequence = listOf(2, 1, 0, 1)
        var sequenceIdx = 0

        while (true) {
            delay(3000)

            // Only auto-scroll if the user isn't currently touching/scrolling
            if (!listState.isScrollInProgress) {
                val nextIndex = sequence[sequenceIdx % sequence.size]
                listState.animateScrollToItem(nextIndex)
                sequenceIdx++
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 24.dp,
                bottom = 24.dp
            ),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .width(388.dp)
                .height(262.dp)
        ) {

            // Outer box
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .innerShadow(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0x80FF2D88),
                        blur = 4.dp,
                        offsetX = 0.5.dp,
                        offsetY = 0.dp
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1E042A))
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF802F61),
                                Color(0xFF311542)
                            )
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 18.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Creator Kings",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TrendingNowTypography.Inter
                )

                Text(
                    text = "View all",
                    color = Color(0xFFFF2D88),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TrendingNowTypography.Inter,
                    modifier = Modifier.clickable {}
                )
            }

            CrownPlaceholder()

            CreatorCards(
                listState = listState
            )

            CreatorLoader(
                activeIndex = activeIndex,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 9.dp)
            )
        }
    }
}

@Composable
private fun BoxScope.CrownPlaceholder() {
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 52.dp)
            .width(240.dp)
            .height(75.dp)
    )
}

@Composable
private fun CreatorCards(
    listState: LazyListState
) {
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 38.dp),
        contentPadding = PaddingValues(
            start = 94.dp,
            end = 94.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(3) { index ->
            CarouselItem(
                index = index,
                listState = listState,
                cardWidth = 200.dp
            )
        }
    }
}

@Composable
private fun CarouselItem(
    index: Int,
    listState: LazyListState,
    cardWidth: Dp
) {
    val layoutInfo = listState.layoutInfo

    val itemInfo =
        layoutInfo.visibleItemsInfo
            .find { it.index == index }

    val viewportCenter =
        (
                layoutInfo.viewportStartOffset +
                        layoutInfo.viewportEndOffset
                ) / 2f

    val fraction =
        if (itemInfo != null) {

            val itemCenter =
                itemInfo.offset + itemInfo.size / 2f

            val distance =
                abs(itemCenter - viewportCenter)

            val maxDistance =
                layoutInfo.viewportEndOffset -
                        layoutInfo.viewportStartOffset

            (distance / maxDistance)
                .coerceIn(0f, 1f)

        } else {
            1f
        }

    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(
            dampingRatio = 0.75f,
            stiffness = 350f
        ),
        label = "cardAnimation"
    )

    val cardScale =
        1.12f - (animatedFraction * 0.17f)

    val cardAlpha =
        1f - (animatedFraction * 0.45f)

    val cardBlur =
        (animatedFraction * 5f).dp

    val imageScale by animateFloatAsState(
        targetValue = 1f + (
                (1f - animatedFraction) * 0.18f
                ),
        animationSpec = spring(
            dampingRatio = 0.72f,
            stiffness = 300f
        ),
        label = "bhuvamImagePop"
    )

    Box(
        modifier = Modifier
            .width(cardWidth)
            .height(180.dp)
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
                alpha = cardAlpha
            }
            .blur(cardBlur)
    ) {

        // Small card
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(cardWidth)
                .height(150.dp)
                .clip(RoundedCornerShape(5.dp))
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_home_card
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Character
        Image(
            painter = painterResource(
                id = R.drawable.ic_bhuvam_home
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(180.dp)
                .height(154.dp)
                .graphicsLayer {
                    scaleX = imageScale
                    scaleY = imageScale
                },
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun CreatorLoader(
    activeIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->

            val isActive = index == activeIndex

            val width by animateDpAsState(
                targetValue = if (isActive) {
                    20.dp
                } else {
                    7.dp
                },
                animationSpec = spring(
                    dampingRatio = 0.8f,
                    stiffness = 500f
                ),
                label = "loaderWidth"
            )

            Box(
                modifier = Modifier
                    .width(width)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (isActive) {
                            Color(0xFFFF2D88)
                        } else {
                            Color(0xFF716A73)
                        }
                    )
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0C091A
)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
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
