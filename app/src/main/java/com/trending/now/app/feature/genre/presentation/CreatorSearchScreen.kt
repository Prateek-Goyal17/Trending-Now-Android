package com.trending.now.app.feature.genre.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.trending.now.app.R
import com.trending.now.app.core.common.components.TrendingNowTextField
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.genre.domain.model.Genre
import com.trending.now.app.feature.genre.domain.model.GenreCreator
import java.util.Locale
import androidx.core.graphics.toColorInt

@Composable
fun CreatorSearchRoute(
    onGenreClick: (String) -> Unit,
    onCreatorClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GenreSearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    CreatorSearchScreen(
        uiState = uiState,
        onQueryChange = viewModel::updateQuery,
        onGenreClick = onGenreClick,
        onCreatorClick = onCreatorClick,
        onRetry = viewModel::loadGenres,
        modifier = modifier,
    )
}

@Composable
fun CreatorSearchScreen(
    uiState: GenreSearchUiState,
    onQueryChange: (String) -> Unit,
    onGenreClick: (String) -> Unit,
    onCreatorClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = androidx.compose.runtime.remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding(),
    ) {
        TrendingNowTextField(
            value = uiState.query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 18.dp, end = 20.dp, bottom = 0.dp)
                .focusRequester(focusRequester),
            placeholder = "Find your favorite ",
            highlightedPlaceholders = listOf("Roaster", "Gamer", "Comedian"),
            leadingIcon = R.drawable.ic_search,
        )

        when {
            uiState.isLoading && uiState.genres.isEmpty() -> SearchLoadingState()
            uiState.errorMessage != null && uiState.genres.isEmpty() -> SearchMessageState(
                message = uiState.errorMessage,
                actionLabel = "Retry",
                onAction = onRetry,
            )
            uiState.query.isBlank() -> GenreGrid(
                genres = uiState.genres,
                onGenreClick = onGenreClick,
            )
            uiState.searchResults.isEmpty() -> SearchMessageState(
                message = "No creators found for “${uiState.query.trim()}”.",
            )
            else -> CreatorResultsGrid(
                creators = uiState.searchResults,
                onCreatorClick = onCreatorClick,
            )
        }
    }
}

@Composable
private fun GenreGrid(
    genres: List<Genre>,
    onGenreClick: (String) -> Unit,
) {
    if (genres.isEmpty()) {
        SearchMessageState(message = "No genres are available yet.")
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Explore by Genre",
            color = Color(0xFFFFE3F0),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = TrendingNowTypography.Inter,
            lineHeight = 20.sp,
            letterSpacing = 0.04.em,
            modifier = Modifier.padding(start = 20.dp, top = 35.dp, bottom = 20.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 112.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(items = genres, key = Genre::id) { genre ->
                GenreCard(
                    genre = genre,
                    onClick = { onGenreClick(genre.id) },
                )
            }
        }
    }
}

@Composable
private fun GenreCard(
    genre: Genre,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(12.dp)
    val genreColor = genre.color.toComposeColor() ?: TrendingNowColors.RisingCreatorTag

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape)
            .background(genreColor.copy(alpha = 0.35f))
            .border(1.dp, genreColor.copy(alpha = 0.9f), shape)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = genre.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.48f to genreColor.copy(alpha = 0.24f),
                        1f to genreColor.copy(alpha = 0.96f),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp),
        ) {
            AsyncImage(
                model = genre.logoUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = genre.name.uppercase(Locale.getDefault()),
                color = Color.White,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.08.em,
                fontWeight = FontWeight.Normal,
                fontFamily = TrendingNowTypography.Anton,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = "${genre.creators.size} ${if (genre.creators.size == 1) "Creator" else "Creators"}",
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                letterSpacing = 0.04.em,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter,
            )
        }
    }
}

@Composable
private fun CreatorResultsGrid(
    creators: List<GenreCreator>,
    onCreatorClick: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Creators",
            color = Color(0xFFFFE3F0),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = TrendingNowTypography.Inter,
            lineHeight = 20.sp,
            letterSpacing = 0.04.em,
            modifier = Modifier.padding(start = 20.dp, top = 35.dp, bottom = 20.dp),
        )

        CreatorGrid(
            creators = creators,
            onCreatorClick = onCreatorClick,
            bottomPadding = 112.dp,
        )
    }
}

@Composable
fun CreatorGrid(
    creators: List<GenreCreator>,
    onCreatorClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    bottomPadding: androidx.compose.ui.unit.Dp = 24.dp,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = bottomPadding),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        items(items = creators, key = GenreCreator::id) { creator ->
            CreatorGridCard(
                creator = creator,
                onClick = { onCreatorClick(creator.name) },
            )
        }
    }
}

@Composable
private fun CreatorGridCard(
    creator: GenreCreator,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(9.dp)
    val displayName = creator.name.replace('_', ' ')

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.64f)
            .clip(shape)
            .background(TrendingNowColors.CardSurface)
            .border(1.dp, TrendingNowColors.RisingCreatorTag.copy(alpha = 0.8f), shape)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = creator.cardImageUrl ?: creator.imageUrl,
            contentDescription = displayName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.45f to Color.Transparent,
                        0.72f to Color(0x99F22472),
                        1f to Color(0xFFFF527B),
                    ),
                ),
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 11.dp, end = 10.dp)
                .size(30.dp)
        ) {
            // Drop Shadow (Y: 1.5, Blur: 0)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = 1.5.dp)
                    .background(TrendingNowColors.RisingCreatorTag, CircleShape)
            )

            // Button Surface
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, CircleShape)
                    .clip(CircleShape)
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_right_arrow),
                    contentDescription = "Open $displayName",
                    tint = TrendingNowColors.RisingCreatorTag,
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 11.dp, vertical = 12.dp),
        ) {
            Text(
                text = displayName,
                color = Color.White,
                fontSize = 15.sp,
                lineHeight = 16.5.sp,
                letterSpacing = 0.04.em,
                fontWeight = FontWeight.Normal,
                fontFamily = TrendingNowTypography.Anton,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = creator.role,
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                letterSpacing = 0.04.em,
                fontWeight = FontWeight.SemiBold,
                fontFamily = TrendingNowTypography.Inter,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(5.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                SocialCount(
                    icon = R.drawable.ic_instagram,
                    value = creator.instagramFollowers.formatCompactCount(),
                )

                Spacer(Modifier.width(8.dp))

                SocialCount(
                    icon = R.drawable.ic_youtube,
                    value = creator.youtubeFollowers?.uppercase(Locale.US) ?: "--",
                    iconSize = 20.dp
                )
            }
        }
    }
}

@Composable
private fun SocialCount(
    icon: Int,
    value: String,
    iconSize: androidx.compose.ui.unit.Dp = 14.dp
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(iconSize),
        )

        Spacer(Modifier.width(3.dp))

        Text(
            text = value,
            color = Color.White,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = TrendingNowTypography.Inter,
            maxLines = 1,
        )
    }
}

@Composable
private fun SearchLoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            color = TrendingNowColors.RisingCreatorTag,
            strokeWidth = 3.dp,
        )
    }
}

@Composable
fun SearchMessageState(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            color = TrendingNowColors.CardContent,
            fontSize = 15.sp,
            fontFamily = TrendingNowTypography.Inter,
        )

        if (actionLabel != null && onAction != null) {
            TextButton(onClick = onAction) {
                Text(
                    text = actionLabel,
                    color = TrendingNowColors.RisingCreatorTag,
                    fontFamily = TrendingNowTypography.Inter,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

private fun String?.toComposeColor(): Color? {
    if (isNullOrBlank()) return null

    return runCatching { Color(this.toColorInt()) }.getOrNull()
}

private fun Long?.formatCompactCount(): String {
    val count = this ?: return "--"
    return when {
        count >= 1_000_000_000 -> compactNumber(count, 1_000_000_000, "B")
        count >= 1_000_000 -> compactNumber(count, 1_000_000, "M")
        count >= 1_000 -> compactNumber(count, 1_000, "K")
        else -> count.toString()
    }
}

private fun compactNumber(value: Long, divisor: Long, suffix: String): String {
    val scaled = value.toDouble() / divisor
    val pattern = if (scaled >= 10 || scaled % 1.0 == 0.0) "%.0f%s" else "%.1f%s"
    return String.format(Locale.US, pattern, scaled, suffix)
}
