package com.trending.now.app.feature.creator.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.trending.now.app.R
import com.trending.now.app.core.common.components.GradientAccentButton
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import kotlinx.coroutines.delay

data class CreatorIntroCardItem(
    val title: String,
    val description: String,
    val imageUrl: String,
)

@Composable
fun CreatorIntroCard(
    cards: List<CreatorIntroCardItem>,
    modifier: Modifier = Modifier,
    onPersonalizeClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(20.dp)

    var activeIndex by remember { mutableIntStateOf(0) }
    var animationType by remember { mutableIntStateOf(0) }
    var transitionCount by remember { mutableIntStateOf(0) }
    var isVisible by remember { mutableStateOf(true) }

    val activeCard = cards.getOrNull(activeIndex) ?: return

    val exitDuration = 600
    val enterDuration = 650
    val overlapDuration = 180

    val enterDelay = exitDuration - overlapDuration

    LaunchedEffect(cards.size) {
        if (cards.size <= 1) {
            return@LaunchedEffect
        }

        while (true) {
            delay(2_700)

            isVisible = false
            delay(300)

            animationType = transitionCount % 3

            activeIndex = (activeIndex + 1) % cards.size

            transitionCount++

            isVisible = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(275.dp)
            .clip(shape)

    ) {
        Image(
            painter = painterResource(R.drawable.pink_gradient_bg),
            contentDescription = "Background",
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(275.dp)
                .clip(shape)
                .innerShadow(
                    shape = shape,
                    shadow = Shadow(
                        radius = 8.dp,
                        spread = 0.dp,
                        color = TrendingNowColors.RisingCreatorTag,
                        offset = DpOffset(
                            x = 0.5.dp,
                            y = 0.5.dp,
                        ),
                    ),
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = shape,
                ),
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 27.dp, bottom = 33.dp, start = 24.dp),
            ) {
                AnimatedContent(
                    targetState = activeIndex,
                    modifier = Modifier.zIndex(1f),
                    transitionSpec = {
                        (
                                (
                                        slideInVertically(
                                            animationSpec = tween(durationMillis = 1000),
                                            initialOffsetY = { height ->
                                                height
                                            },
                                        ) +
                                                fadeIn(
                                                    animationSpec = tween(durationMillis = 700),
                                                )
                                        ) togetherWith
                                        (
                                                slideOutVertically(
                                                    animationSpec = tween(durationMillis = 1000),
                                                    targetOffsetY = { height ->
                                                        -height
                                                    },
                                                ) +
                                                        fadeOut(
                                                            animationSpec = tween(durationMillis = 600),
                                                        )
                                                )
                                ).using(
                                SizeTransform(
                                    clip = false,
                                ),
                            )
                    },
                    label = "creatorTextTransition",
                ) { index ->

                    val card = cards.getOrNull(index)
                        ?: return@AnimatedContent

                    Text(
                        text = card.title,
                        modifier = Modifier.fillMaxWidth(0.6f),
                        style = TextStyle(
                            fontSize = 22.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = TrendingNowTypography.Anton,
                            color = TrendingNowColors.CardTitle,
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(Modifier.height(13.dp))

                Text(
                    text = activeCard.description,
                    modifier = Modifier
                        .fillMaxWidth(0.47f)
                        .zIndex(0f),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = TrendingNowTypography.Inter,
                        color = Color(0xFFB6B6B6),
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(30.dp))

                SocialIconRow()

                Spacer(Modifier.height(12.dp))

                GradientAccentButton(
                    text = "Personalize Feed",
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    onClick = onPersonalizeClick,
                )
            }

            AnimatedContent(
                targetState = activeIndex,
                modifier = Modifier.align(Alignment.BottomEnd),
                transitionSpec = {
                    when (animationType) {

                        // --------------------------------------------------
                        // 1.
                        // OLD: EXIT DOWN
                        // NEW: RIGHT -> LEFT
                        // --------------------------------------------------
                        0 -> {
                            (
                                    slideInHorizontally(
                                        animationSpec = tween(
                                            durationMillis = enterDuration,
                                            delayMillis = enterDelay,
                                            easing = FastOutSlowInEasing,
                                        ),
                                        initialOffsetX = { width ->
                                            width
                                        },
                                    ) togetherWith
                                            slideOutVertically(
                                                animationSpec = tween(
                                                    durationMillis = exitDuration,
                                                    easing = FastOutSlowInEasing,
                                                ),
                                                targetOffsetY = { height ->
                                                    height
                                                },
                                            )
                                    ).using(
                                    SizeTransform(
                                        clip = false,
                                    ),
                                )
                        }

                        // --------------------------------------------------
                        // 2.
                        // OLD: EXIT LEFT -> RIGHT
                        // NEW: TOP -> BOTTOM
                        // --------------------------------------------------
                        1 -> {
                            (
                                    slideInVertically(
                                        animationSpec = tween(
                                            durationMillis = enterDuration,
                                            delayMillis = enterDelay,
                                            easing = FastOutSlowInEasing,
                                        ),
                                        initialOffsetY = { height ->
                                            -height
                                        },
                                    ) togetherWith
                                            slideOutHorizontally(
                                                animationSpec = tween(
                                                    durationMillis = exitDuration,
                                                    easing = FastOutSlowInEasing,
                                                ),
                                                targetOffsetX = { width ->
                                                    width
                                                },
                                            )
                                    ).using(
                                    SizeTransform(
                                        clip = false,
                                    ),
                                )
                        }

                        // --------------------------------------------------
                        // 3.
                        // OLD: EXIT BOTTOM -> TOP
                        // NEW: BOTTOM -> TOP
                        // --------------------------------------------------
                        else -> {
                            (
                                    slideInVertically(
                                        animationSpec = tween(
                                            durationMillis = enterDuration,
                                            delayMillis = enterDelay,
                                            easing = FastOutSlowInEasing,
                                        ),
                                        initialOffsetY = { height ->
                                            height
                                        },
                                    ) togetherWith
                                            slideOutVertically(
                                                animationSpec = tween(
                                                    durationMillis = exitDuration,
                                                    easing = FastOutSlowInEasing,
                                                ),
                                                targetOffsetY = { height ->
                                                    -height
                                                },
                                            )
                                    ).using(
                                    SizeTransform(
                                        clip = false,
                                    ),
                                )
                        }
                    }
                },
                label = "creatorImageTransition",
            ) { index ->

                val card = cards.getOrNull(index)
                    ?: return@AnimatedContent

                AsyncImage(
                    model = card.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .height(250.dp),
                )
            }
        }
    }
}

@Composable
private fun SocialIconRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CreatorSocialIcon(
            icon = R.drawable.ic_creator_instagram,
            contentDescription = "Instagram",
        )
        CreatorSocialIcon(
            icon = R.drawable.ic_creator_youtube,
            contentDescription = "YouTube",
        )
        CreatorSocialIcon(
            icon = R.drawable.ic_creator_twitter,
            contentDescription = "Twitter",
        )
        CreatorSocialIcon(
            icon = R.drawable.ic_creator_news,
            contentDescription = "News",
        )
    }
}

@Composable
private fun CreatorSocialIcon(
    @DrawableRes icon: Int,
    contentDescription: String,
) {
    Image(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        modifier = Modifier.size(28.dp),
    )
}
