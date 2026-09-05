package com.trending.now.app.feature.genre.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.genre.domain.model.Genre

@Composable
fun GenreCreatorsRoute(
    genreId: String,
    onBack: () -> Unit,
    onCreatorClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GenreSearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val genre = uiState.genres.firstOrNull { it.id == genreId }

    GenreCreatorsScreen(
        genre = genre,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onBack = onBack,
        onCreatorClick = onCreatorClick,
        onRetry = viewModel::loadGenres,
        modifier = modifier,
    )
}

@Composable
fun GenreCreatorsScreen(
    genre: Genre?,
    isLoading: Boolean,
    errorMessage: String?,
    onBack: () -> Unit,
    onCreatorClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding(),
    ) {
        when {
            genre != null -> {
                GenreCreatorsHeader(
                    genre = genre,
                    onBack = onBack,
                )

                Spacer(Modifier.height(24.dp))

                if (genre.creators.isEmpty()) {
                    SearchMessageState(
                        message = "No creators are available in this genre yet.",
                    )
                } else {
                    CreatorGrid(
                        creators = genre.creators,
                        onCreatorClick = onCreatorClick,
                        bottomPadding = 28.dp,
                    )
                }
            }

            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = TrendingNowColors.RisingCreatorTag,
                        strokeWidth = 3.dp,
                    )
                }
            }

            else -> {
                GenreCreatorsHeader(
                    genre = null,
                    onBack = onBack,
                )

                SearchMessageState(
                    message = errorMessage ?: "This genre is no longer available.",
                    actionLabel = errorMessage?.let { "Retry" },
                    onAction = errorMessage?.let { onRetry },
                )
            }
        }
    }
}

@Composable
private fun GenreCreatorsHeader(
    genre: Genre?,
    onBack: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(TrendingNowColors.NavigationCenterSurface)
                .border(1.dp, Color(0xFF432130), CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBack,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_right_arrow),
                contentDescription = "Back",
                modifier = Modifier
                    .size(20.dp)
                    .rotate(180f),
                tint = TrendingNowColors.CardTitle,
            )
        }

        Spacer(Modifier.width(26.dp))

        if (genre?.logoUrl != null) {
            AsyncImage(
                model = genre.logoUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp),
            )

            Spacer(Modifier.width(10.dp))
        }

        Text(
            text = genre?.let { "${it.name} Creators" } ?: "Creators",
            color = Color(0xFFFFD9EB),
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
