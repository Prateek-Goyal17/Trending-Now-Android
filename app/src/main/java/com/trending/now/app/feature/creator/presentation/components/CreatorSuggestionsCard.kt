package com.trending.now.app.feature.creator.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.trending.now.app.R
import com.trending.now.app.core.common.components.GradientAccentButton
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.creator.data.remote.CreatorSuggestionResponse

data class CreatorSuggestionCardUiModel(
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
fun CreatorSuggestionsCard(
    creatorSuggestion: CreatorSuggestionCardUiModel,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    onExploreClick: () -> Unit = {},
    onInstagramClick: () -> Unit = {},
    onYoutubeClick: () -> Unit = {},
    onTwitterClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(shape)
            .background(color = TrendingNowColors.CreatorSuggestionCardAndTagBackground)
            .clickable(onClick = onCardClick)
            .border(
                width = 1.dp,
                color = Color(0xFF442B33),
                shape = shape,
            ),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 17.dp, bottom = 18.dp, start = 24.dp),
        ) {

            CreatorSuggestionBadge(badgeType = creatorSuggestion.badgeType)

            Spacer(Modifier.height(10.dp))

            Text(
                text = creatorSuggestion.creatorName,
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

            Text(
                text = creatorSuggestion.role,
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
                text = creatorSuggestion.suggestionLine,
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
                onClick = onExploreClick,
            )

            Spacer(Modifier.weight(1f))

            SocialIconRow(
                onInstagramClick = onInstagramClick,
                onYoutubeClick = onYoutubeClick,
                onTwitterClick = onTwitterClick,
            )

        }

        AsyncImage(
            model = creatorSuggestion.suggestionImageUrl,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .height(275.dp),
            contentScale = ContentScale.FillHeight,
        )
    }
}

fun CreatorSuggestionResponse.toCreatorSuggestionCardUiModel(): CreatorSuggestionCardUiModel {
    return CreatorSuggestionCardUiModel(
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
private fun SocialIconRow(
    onInstagramClick: () -> Unit = {},
    onYoutubeClick: () -> Unit = {},
    onTwitterClick: () -> Unit = {},
) {
    Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        CreatorSocialIcon(
            icon = R.drawable.ic_creator_instagram,
            contentDescription = "Instagram",
            onClick = onInstagramClick,
        )
        CreatorSocialIcon(
            icon = R.drawable.ic_creator_youtube,
            contentDescription = "YouTube",
            onClick = onYoutubeClick,
        )
        CreatorSocialIcon(
            icon = R.drawable.ic_creator_twitter,
            contentDescription = "Twitter",
            onClick = onTwitterClick,
        )
    }
}

@Composable
private fun CreatorSocialIcon(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Image(
        painter = painterResource(icon),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(32.dp)
            .clickable(onClick = onClick),
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
