package com.trending.now.app.feature.creator.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.trending.now.app.R
import com.trending.now.app.core.common.components.TrendingNowTextField
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.creator.data.remote.CreatorPostMediaResponse
import com.trending.now.app.feature.creator.data.remote.CreatorTrendingPostResponse
import com.trending.now.app.feature.creator.data.remote.buzzingCards
import com.trending.now.app.feature.creator.data.remote.creatorSuggestions
import com.trending.now.app.feature.creator.data.remote.favoriteCreatorCards
import com.trending.now.app.feature.creator.data.remote.trendingNowPosts
import com.trending.now.app.feature.creator.presentation.components.CreatorBuzzCard
import com.trending.now.app.feature.creator.presentation.components.CreatorIntroCard
import com.trending.now.app.feature.creator.presentation.components.CreatorIntroCardItem
import com.trending.now.app.feature.creator.presentation.components.CreatorSuggestionCard
import com.trending.now.app.feature.creator.presentation.components.TrendingVideoCard
import com.trending.now.app.feature.creator.presentation.components.toCreatorBuzzCardUiModel
import com.trending.now.app.feature.creator.presentation.components.toCreatorSuggestionCardUiModel

@Composable
fun CreatorScreen(
    onPersonalizeFeedClick: () -> Unit,
    onTrendingVideoClick: () -> Unit,
    onCreatorClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreatorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val creatorScreenFeed = uiState.creatorScreenFeed
    val introCards = creatorScreenFeed
        ?.favoriteCreatorCards()
        .orEmpty()
        .map { card ->
            CreatorIntroCardItem(
                title = card.title.orEmpty(),
                description = card.description.orEmpty(),
                imageUrl = card.image.orEmpty(),
            )
        }
    val trendingNowPosts = creatorScreenFeed
        ?.trendingNowPosts()
        .orEmpty()

    val creatorSuggestions = creatorScreenFeed?.creatorSuggestions().orEmpty()
    val buzzingCards = creatorScreenFeed
        ?.buzzingCards()
        .orEmpty()
        .mapNotNull { buzzCard ->
            buzzCard.toCreatorBuzzCardUiModel()
        }

    var searchText by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 15.dp, vertical = 18.dp),
    ) {
        TrendingNowTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = "Find your favorite ",
            highlightedPlaceholder = "Comedian",
            leadingIcon = R.drawable.ic_search,
        )

        Spacer(Modifier.height(35.dp))

        if (introCards.isNotEmpty()) {
            CreatorIntroCard(
                cards = introCards,
                onPersonalizeClick = onPersonalizeFeedClick,
            )

            Spacer(Modifier.height(35.dp))
        }

        if (trendingNowPosts.isNotEmpty()) {
            Text(
                text = "Trending Now",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = TrendingNowTypography.Inter,
                    color = TrendingNowColors.CardTitle,
                ),
            )

            Spacer(Modifier.height(20.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(items = trendingNowPosts) { post ->
                    TrendingVideoCard(
                        username = post.account.orEmpty(),
                        title = post.displayTitle(),
                        imageUrl = post.displayImageUrl(),
                        platform = post.platform.orEmpty(),
                        onCardClick = onTrendingVideoClick,
                    )
                }
            }

            Spacer(Modifier.height(35.dp))
        }

        if (creatorSuggestions.isNotEmpty()) {
            Text(
                text = "Creator Suggestions",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = TrendingNowTypography.Inter,
                    color = TrendingNowColors.CardTitle,
                ),
            )

            Spacer(Modifier.height(20.dp))

            CreatorSuggestionCard(
                creatorSuggestions = creatorSuggestions.map { suggestion ->
                    suggestion.toCreatorSuggestionCardUiModel()
                },
                onExploreClick = { suggestion ->
                    onCreatorClick(suggestion.creatorSlug)
                },
            )

            Spacer(Modifier.height(35.dp))
        }

        if (buzzingCards.isNotEmpty()) {
            Text(
                text = "What's Buzzing This Week",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = TrendingNowTypography.Inter,
                    color = TrendingNowColors.CardTitle,
                ),
            )

            Spacer(Modifier.height(20.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(items = buzzingCards, key = { buzzCard -> buzzCard.id }) { buzzCard ->
                    CreatorBuzzCard(
                        buzzCard = buzzCard,
                        modifier = Modifier.height(225.dp),
                    )
                }
            }
        }

        Spacer(Modifier.height(90.dp))
    }
}

private fun CreatorTrendingPostResponse.displayImageUrl(): String? {
    return thumbnail
        ?: media.orEmpty().firstDisplayImageUrl()
}

private fun CreatorTrendingPostResponse.displayTitle(): String {
    return caption ?: text ?: normalizedText.orEmpty()
}

private fun List<CreatorPostMediaResponse>.firstDisplayImageUrl(): String? {
    return firstNotNullOfOrNull { mediaItem ->
        mediaItem.poster ?: mediaItem.thumbnail
    }
}
