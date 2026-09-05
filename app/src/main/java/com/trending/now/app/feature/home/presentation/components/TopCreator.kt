package com.trending.now.app.feature.home.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowTypography
import kotlinx.coroutines.delay
import kotlin.math.abs

private data class KingItem(
    val rank: Int,
    val name: String,
    val subtitle: String,
    val igCount: String,
    val ytCount: String
)

private val kingsList = listOf(
    KingItem(rank = 2, name = "Samay Raina", subtitle = "Comedian & Gamer", igCount = "15M", ytCount = "12M"),
    KingItem(rank = 1, name = "Bhuvan Bam", subtitle = "Indian comedian", igCount = "20M", ytCount = "20M"),
    KingItem(rank = 3, name = "Pranit More", subtitle = "Indian creator", igCount = "8M", ytCount = "6M")
)


@Composable
fun TopCreator(
    onViewAllClick: () -> Unit = {},
    showViewAll: Boolean = true
) {
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
            .padding(top = 24.dp, bottom = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(388.dp)
                .height(262.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Creator Kings",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = TrendingNowTypography.Inter,
                            brush = Brush.linearGradient(
                                colors = listOf(Color.White, Color(0xFFFF2D88))
                            )
                        )
                    )
                    if (showViewAll) {
                        TextButton(
                            onClick = onViewAllClick,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text(
                                text = "View all",
                                color = Color(0xFFFF2D88),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = TrendingNowTypography.Inter
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CrownPlaceholder()
                    CreatorCards(
                        listState = listState,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                CreatorLoader(
                    activeIndex = activeIndex,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun BoxScope.CrownPlaceholder() {
    Image(
        painter = painterResource(id = R.drawable.ic_crown),
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.Center)
            .width(388.dp)
            .offset(y = (-90).dp)
            .graphicsLayer {
                alpha = 0.95f
                scaleX = 3.0f
                scaleY = 3.0f
            },
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun CreatorCards(
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            start = 94.dp,
            end = 94.dp,
            top = 10.dp,
            bottom = 10.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        items(kingsList.size) { index ->
            CarouselItem(
                index = index,
                listState = listState,
                cardWidth = 200.dp,
                item = kingsList[index]
            )
        }
    }
}

@Composable
private fun CarouselItem(
    index: Int,
    listState: LazyListState,
    cardWidth: Dp,
    item: KingItem
) {
    val layoutInfo = listState.layoutInfo
    val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == index }
    val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2f

    val fraction = if (itemInfo != null) {
        val itemCenter = itemInfo.offset + itemInfo.size / 2f
        val distance = abs(itemCenter - viewportCenter)
        val maxDistance = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
        (distance / maxDistance).coerceIn(0f, 1f)
    } else {
        1f
    }

    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 350f),
        label = "cardAnimation"
    )

    val cardScale = 1.12f - (animatedFraction * 0.17f)
    val cardAlpha = 1f - (animatedFraction * 0.45f)
    val cardBlur = (animatedFraction * 5f).dp

    val imageScale by animateFloatAsState(
        targetValue = 1f + ((1f - animatedFraction) * 0.18f),
        animationSpec = spring(dampingRatio = 0.72f, stiffness = 300f),
        label = "bhuvamImagePop"
    )

    Box(
        modifier = Modifier
            .width(cardWidth)
            .height(220.dp)
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
                alpha = cardAlpha
                transformOrigin = TransformOrigin(0.5f, 1f)
            }
            .blur(cardBlur)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(cardWidth)
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_home_card),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SocialBadge(iconRes = R.drawable.ic_instagram, count = item.igCount)
                SocialBadge(iconRes = R.drawable.ic_youtube, count = item.ytCount)
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_bhuvam_home),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(160.dp)
                .height(185.dp)
                .graphicsLayer {
                    scaleX = imageScale
                    scaleY = imageScale
                    transformOrigin = TransformOrigin(0.5f, 1f)
                },
            contentScale = ContentScale.Fit,
            alignment = Alignment.BottomCenter
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = TrendingNowTypography.Anton,
                lineHeight = 18.sp,
                letterSpacing = 0.72.sp
            )
            Text(
                text = item.subtitle,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = TrendingNowTypography.Inter
            )
        }
    }
}

@Composable
private fun SocialBadge(iconRes: Int, count: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.92f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = count,
            color = Color(0xFF1A1A1A),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter
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
                targetValue = if (isActive) 20.dp else 7.dp,
                animationSpec = spring(dampingRatio = 0.8f, stiffness = 500f),
                label = "loaderWidth"
            )
            Box(
                modifier = Modifier
                    .width(width)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (isActive) Color(0xFFFF2D88) else Color(0xFF716A73))
            )
        }
    }
}

