package com.trending.now.app.feature.creator.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.trending.now.app.R
import com.trending.now.app.core.common.components.GradientAccentButton
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.creator.data.remote.CreatorSuggestionResponse
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

data class CreatorSuggestionCardUiModel(
    val creatorSlug: String,
    val creatorName: String,
    val badgeType: CreatorSuggestionBadgeType,
    val role: String,
    val suggestionLine: AnnotatedString,
    val suggestionImageUrl: String?,
)

enum class CreatorSuggestionBadgeType {
    TopCreator,
    RisingCreator,
    Unknown,
}

@Composable
fun CreatorSuggestionCard(
    creatorSuggestions: List<CreatorSuggestionCardUiModel>,
    modifier: Modifier = Modifier,
    onExploreClick: (CreatorSuggestionCardUiModel) -> Unit = {},
) {
    val shape = RoundedCornerShape(20.dp)
    var activeIndex by remember { mutableIntStateOf(0) }
    val activeSuggestion = creatorSuggestions.getOrNull(activeIndex) ?: return

    val exitDuration = 1200
    val enterDuration = 1250

    LaunchedEffect(creatorSuggestions.size) {
        if (creatorSuggestions.size <= 1) {
            return@LaunchedEffect
        }

        while (true) {
            delay(5_000.milliseconds)

            activeIndex = (activeIndex + 1) % creatorSuggestions.size
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(shape)
                .background(color = TrendingNowColors.CreatorSuggestionCardAndTagBackground)
                .border(
                    width = 1.dp,
                    color = Color(0xFF442B33),
                    shape = shape,
                ),
        ) {
            Image(
                painter = painterResource(R.drawable.pink_gradient_bg),
                contentDescription = "Background",
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .padding(top = 17.dp, bottom = 18.dp, start = 24.dp),
            ) {
                AnimatedContent(
                    targetState = activeSuggestion.badgeType,
                    transitionSpec = {
                        fadeIn(
                            animationSpec = tween(durationMillis = 300),
                        ) togetherWith fadeOut(
                            animationSpec = tween(durationMillis = 220),
                        )
                    },
                    label = "creatorSuggestionBadgeTransition",
                ) { badgeType ->
                    CreatorSuggestionBadge(badgeType = badgeType)
                }

                Spacer(Modifier.height(10.dp))

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
                    label = "creatorSuggestionNameTransition",
                ) { index ->
                    val suggestion = creatorSuggestions.getOrNull(index)
                        ?: return@AnimatedContent

                    Text(
                        text = suggestion.creatorName,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = TrendingNowTypography.Anton,
                            color = Color.White,
                            letterSpacing = 0.4.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Text(
                    text = activeSuggestion.role,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = TrendingNowTypography.Inter,
                        color = Color.White,
                        letterSpacing = 0.4.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = activeSuggestion.suggestionLine,
                    modifier = Modifier.fillMaxWidth(0.35f),
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = TrendingNowTypography.Inter,
                        color = Color(0xFFB6B6B6),
                        letterSpacing = 0.4.sp,
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.weight(1f))

                GradientAccentButton(
                    text = "Explore",
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    height = 25.dp,
                    textFontSize = 12.0,
                    textFontWeight = FontWeight.SemiBold,
                    suffixIcon = R.drawable.ic_right_arrow,
                    suffixIconSize = 10.dp,
                    suffixIconLeftPadding = 4.dp,
                    onClick = {
                        onExploreClick(activeSuggestion)
                    },
                )

                Spacer(Modifier.weight(1f))

                SocialIconRow()

            }

            AnimatedContent(
                targetState = activeIndex,
                modifier = Modifier.align(Alignment.BottomEnd),
                contentAlignment = Alignment.BottomEnd,
                transitionSpec = {
                    val enterTransition =
                        scaleIn(
                            initialScale = 0.22f,
                            transformOrigin = TransformOrigin(1f, 1f),
                            animationSpec = tween(
                                durationMillis = enterDuration,
                                easing = FastOutSlowInEasing,
                            ),
                        ) + fadeIn(
                            animationSpec = tween(
                                durationMillis = enterDuration,
                            ),
                        )

                    val exitTransition =
                        scaleOut(
                            targetScale = 0.22f,
                            transformOrigin = TransformOrigin(1f, 1f),
                            animationSpec = tween(
                                durationMillis = exitDuration,
                                easing = FastOutSlowInEasing,
                            ),
                        ) + fadeOut(
                            animationSpec = tween(
                                durationMillis = exitDuration,
                            ),
                        )

                    enterTransition togetherWith exitTransition
                },
                label = "creatorSuggestionImageTransition",
            ) { index ->
                val suggestion = creatorSuggestions.getOrNull(index)
                    ?: return@AnimatedContent

                AsyncImage(
                    model = suggestion.suggestionImageUrl,
                    contentDescription = null,
                    modifier = Modifier.height(240.dp),
                    contentScale = ContentScale.FillHeight,
                )
            }
        }

        CreatorSuggestionPagerIndicator(
            activeIndex = activeIndex,
            count = creatorSuggestions.size,
        )
    }
}

@Composable
private fun CreatorSuggestionPagerIndicator(
    activeIndex: Int,
    count: Int,
) {
    if (count <= 1) {
        return
    }

    val dotWidth = 9.dp
    val activeWidth = 17.dp
    val indicatorHeight = 8.dp
    val spacing = 5.dp
    val activeExtraWidth = activeWidth - dotWidth
    val totalWidth = dotWidth * count + spacing * (count - 1) + activeExtraWidth
    val activeOffset by animateDpAsState(
        targetValue = indicatorOffset(
            index = activeIndex,
            activeIndex = activeIndex,
            dotWidth = dotWidth,
            spacing = spacing,
            activeExtraWidth = activeExtraWidth,
        ),
        animationSpec = tween(
            durationMillis = 1200,
            easing = FastOutSlowInEasing,
        ),
        label = "creatorSuggestionActiveIndicatorOffset",
    )

    Box(
        modifier = Modifier.padding(top = 10.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        repeat(count) { index ->
            if (index == activeIndex) {
                return@repeat
            }

            val inactiveOffset by animateDpAsState(
                targetValue = indicatorOffset(
                    index = index,
                    activeIndex = activeIndex,
                    dotWidth = dotWidth,
                    spacing = spacing,
                    activeExtraWidth = activeExtraWidth,
                ),
                animationSpec = tween(
                    durationMillis = 1200,
                    easing = FastOutSlowInEasing,
                ),
                label = "creatorSuggestionInactiveIndicatorOffset",
            )

            Box(
                modifier = Modifier
                    .offset(x = inactiveOffset)
                    .width(dotWidth)
                    .height(indicatorHeight)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(Color(0xFF747474))
            )
        }

        Box(
            modifier = Modifier
                .width(totalWidth)
                .height(indicatorHeight),
        )

        Box(
            modifier = Modifier
                .offset(x = activeOffset)
                .width(activeWidth)
                .height(indicatorHeight)
                .clip(RoundedCornerShape(percent = 50))
                .background(TrendingNowColors.RisingCreatorTag),
        )
    }
}

private fun indicatorOffset(
    index: Int,
    activeIndex: Int,
    dotWidth: Dp,
    spacing: Dp,
    activeExtraWidth: Dp,
): Dp {
    val baseOffset = (dotWidth + spacing) * index
    return if (index > activeIndex) {
        baseOffset + activeExtraWidth
    } else {
        baseOffset
    }
}

fun CreatorSuggestionResponse.toCreatorSuggestionCardUiModel(): CreatorSuggestionCardUiModel {
    return CreatorSuggestionCardUiModel(
        creatorSlug = creatorName.orEmpty(),
        creatorName = creatorName.toDisplayCreatorName(),
        badgeType = badge.toCreatorSuggestionBadgeType(),
        role = role.orEmpty(),
        suggestionLine = suggestionLine.toSuggestionLine(),
        suggestionImageUrl = suggestionImage,
    )
}

@Composable
private fun CreatorSuggestionBadge(
    badgeType: CreatorSuggestionBadgeType,
) {
    val badgeRes = when (badgeType) {
        CreatorSuggestionBadgeType.TopCreator -> R.drawable.badge_top_creator
        CreatorSuggestionBadgeType.RisingCreator -> R.drawable.badge_rising_creator
        CreatorSuggestionBadgeType.Unknown -> null
    }

    badgeRes?.let { badge ->
        Image(
            painter = painterResource(badge),
            contentDescription = when (badgeType) {
                CreatorSuggestionBadgeType.TopCreator -> "Top Creator"
                CreatorSuggestionBadgeType.RisingCreator -> "Rising Creator"
                CreatorSuggestionBadgeType.Unknown -> null
            },
            modifier = Modifier.width(104.dp),
        )
    }
}

@Composable
private fun SocialIconRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
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
        modifier = Modifier
            .size(32.dp)
    )
}

private fun String?.toDisplayCreatorName(): String {
    return orEmpty()
        .replace("_", " ")
        .trim()
}

private fun String?.toCreatorSuggestionBadgeType(): CreatorSuggestionBadgeType {
    return when (orEmpty().trim().lowercase()) {
        "top creator" -> CreatorSuggestionBadgeType.TopCreator
        "rising creator" -> CreatorSuggestionBadgeType.RisingCreator
        else -> CreatorSuggestionBadgeType.Unknown
    }
}

private fun String?.toSuggestionLine(): AnnotatedString {
    val displayText = toDisplayCreatorName()
    val metricMatch = suggestionMetricRegex.find(displayText)

    return buildAnnotatedString {
        if (metricMatch == null) {
            append(displayText)
            return@buildAnnotatedString
        }

        append(displayText.substring(0, metricMatch.range.first))
        withStyle(
            SpanStyle(color = TrendingNowColors.RisingCreatorTag),
        ) {
            append(metricMatch.value)
        }
        append(displayText.substring(metricMatch.range.last + 1))
    }
}

private val suggestionMetricRegex = Regex(
    pattern = """\+?\d+(?:\.\d+)?\s*[kKmM]?\s*fans""",
)
