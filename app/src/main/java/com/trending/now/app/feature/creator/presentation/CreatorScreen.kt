package com.trending.now.app.feature.creator.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.trending.now.app.feature.auth.domain.model.AuthState
import com.trending.now.app.feature.creator.data.remote.CreatorPostMediaResponse
import com.trending.now.app.feature.creator.data.remote.CreatorScreenResponse
import com.trending.now.app.feature.creator.data.remote.CreatorTrendingPostResponse
import com.trending.now.app.feature.creator.data.remote.ExistingFavoriteCreatorResponse
import com.trending.now.app.feature.creator.data.remote.buzzingCards
import com.trending.now.app.feature.creator.data.remote.creatorSuggestions
import com.trending.now.app.feature.creator.data.remote.existingFavoriteCreators
import com.trending.now.app.feature.creator.data.remote.favoriteCreatorCards
import com.trending.now.app.feature.creator.data.remote.trendingNowPosts
import com.trending.now.app.feature.creator.presentation.components.CreatorBuzzCard
import com.trending.now.app.feature.creator.presentation.components.CreatorIntroCard
import com.trending.now.app.feature.creator.presentation.components.CreatorIntroCardItem
import com.trending.now.app.feature.creator.presentation.components.CreatorSuggestionCard
import com.trending.now.app.feature.creator.presentation.components.ExistingFavoriteCreatorsSection
import com.trending.now.app.feature.creator.presentation.components.TrendingVideoCard
import com.trending.now.app.feature.creator.presentation.components.toExistingFavoriteCreatorUiModel
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
    val introSection = creatorScreenFeed.toCreatorIntroSection(uiState.authState)
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

    if (uiState.isLoading && creatorScreenFeed == null) {
        CreatorScreenLoader(modifier = modifier)
        return
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
            highlightedPlaceholders = listOf(
                "Roaster",
                "Gamer",
                "Comedian",
            ),
            leadingIcon = R.drawable.ic_search,
        )

        Spacer(Modifier.height(35.dp))

        when (introSection) {
            is CreatorIntroSectionUiState.Guest -> {
                if (introSection.cards.isNotEmpty()) {
                    CreatorIntroCard(
                        cards = introSection.cards,
                        onPersonalizeClick = onPersonalizeFeedClick,
                    )

                    Spacer(Modifier.height(35.dp))
                }
            }

            is CreatorIntroSectionUiState.ExistingUser -> {
                ExistingFavoriteCreatorsSection(
                    creators = introSection.creators.map { creator ->
                        creator.toExistingFavoriteCreatorUiModel()
                    },
                    onCreatorClick = { creator ->
                        onCreatorClick(creator.slug)
                    },
                )

                Spacer(Modifier.height(35.dp))
            }

            CreatorIntroSectionUiState.Hidden,
            -> Unit
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

@Composable
private fun CreatorScreenLoader(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = TrendingNowColors.RisingCreatorTag,
        )
    }
}

private fun CreatorTrendingPostResponse.displayImageUrl(): String? {
    return media.orEmpty().firstDisplayImageUrl()
}

private fun CreatorTrendingPostResponse.displayTitle(): String {
    return caption ?: text ?: normalizedText.orEmpty()
}

private fun List<CreatorPostMediaResponse>.firstDisplayImageUrl(): String? {
    return firstNotNullOfOrNull { mediaItem ->
        mediaItem.poster ?: mediaItem.thumbnail
    }
}

private sealed interface CreatorIntroSectionUiState {
    data class Guest(
        val cards: List<CreatorIntroCardItem>,
    ) : CreatorIntroSectionUiState

    data class ExistingUser(
        val creators: List<ExistingFavoriteCreatorResponse>,
    ) : CreatorIntroSectionUiState

    data object Hidden : CreatorIntroSectionUiState
}

private fun CreatorScreenResponse?.toCreatorIntroSection(
    authState: AuthState,
): CreatorIntroSectionUiState {
    if (this == null) {
        return CreatorIntroSectionUiState.Hidden
    }

    return when (authState) {
        AuthState.Guest,
        AuthState.LoggedOut,
        is AuthState.NewUser,
        -> CreatorIntroSectionUiState.Guest(
            cards = favoriteCreatorCards().map { card ->
                CreatorIntroCardItem(
                    title = card.title.orEmpty(),
                    description = card.description.orEmpty(),
                    imageUrl = card.image.orEmpty(),
                )
            },
        )

        is AuthState.ExistingUser -> CreatorIntroSectionUiState.ExistingUser(
            creators = existingFavoriteCreators(),
        )
    }
}
