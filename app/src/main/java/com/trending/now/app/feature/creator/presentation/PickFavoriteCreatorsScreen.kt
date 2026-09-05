package com.trending.now.app.feature.creator.presentation

import android.graphics.Color as AndroidColor
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.trending.now.app.R
import com.trending.now.app.core.common.components.BackNavigationHeader
import com.trending.now.app.core.common.components.GradientAccentButton
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.auth.domain.model.isGuestLike
import com.trending.now.app.feature.genre.domain.model.Genre
import com.trending.now.app.feature.genre.domain.model.GenreCreator

@Composable
fun PickFavoriteCreatorsRoute(
    onBack: () -> Unit,
    onSignUp: () -> Unit,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PickFavoriteCreatorsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                PickFavoriteCreatorsEvent.Completed -> onCompleted()
            }
        }
    }

    PickFavoriteCreatorsScreen(
        uiState = uiState,
        onBack = onBack,
        onSignUp = onSignUp,
        onGenreSelected = viewModel::selectGenre,
        onCreatorToggle = viewModel::toggleCreator,
        onContinue = viewModel::continueWithSelection,
        onRetry = viewModel::loadGenres,
        modifier = modifier,
    )
}

@Composable
fun PickFavoriteCreatorsScreen(
    uiState: PickFavoriteCreatorsUiState,
    onBack: () -> Unit,
    onSignUp: () -> Unit,
    onGenreSelected: (String) -> Unit,
    onCreatorToggle: (String) -> Unit,
    onContinue: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isGuestLocked = uiState.authState.isGuestLike

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background),
    ) {
        PickerContent(
            uiState = uiState,
            onBack = onBack,
            onGenreSelected = onGenreSelected,
            onCreatorToggle = onCreatorToggle,
            onContinue = onContinue,
            onRetry = onRetry,
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isGuestLocked) {
                        Modifier.blur(12.dp)
                    } else {
                        Modifier
                    },
                ),
        )

        if (isGuestLocked) {
            GuestPickerOverlay(onSignUp = onSignUp)
        }
    }
}

@Composable
private fun PickerContent(
    uiState: PickFavoriteCreatorsUiState,
    onBack: () -> Unit,
    onGenreSelected: (String) -> Unit,
    onCreatorToggle: (String) -> Unit,
    onContinue: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.safeDrawingPadding()) {
        BackNavigationHeader(
            title="Pick Your Favorite Creators",
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(35.dp))

        if (uiState.genres.isNotEmpty()) {
            GenrePicker(
                genres = uiState.genres,
                selectedGenreId = uiState.selectedGenreId,
                onGenreSelected = onGenreSelected,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            when {
                uiState.isLoading && uiState.genres.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(34.dp)
                            .align(Alignment.Center),
                        color = TrendingNowColors.RisingCreatorTag,
                        strokeWidth = 3.dp,
                    )
                }

                uiState.errorMessage != null && uiState.genres.isEmpty() -> {
                    PickerLoadError(
                        message = uiState.errorMessage,
                        onRetry = onRetry,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                uiState.visibleCreators.isEmpty() -> {
                    Text(
                        text = "No creators are available in this genre yet.",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 32.dp),
                        color = TrendingNowColors.CardContent,
                        fontSize = 15.sp,
                        fontFamily = TrendingNowTypography.Inter,
                        textAlign = TextAlign.Center,
                    )
                }

                else -> {
                    CreatorList(
                        creators = uiState.visibleCreators,
                        selectedCreatorIds = uiState.selectedCreatorIds,
                        selectionEnabled = uiState.canChangeSelection,
                        onCreatorToggle = onCreatorToggle,
                    )
                }
            }
        }

        PickerBottomBar(
            selectedCount = uiState.selectedCreatorIds.size,
            isSubmitting = uiState.isSubmitting,
            isRetry = uiState.submissionStarted && uiState.errorMessage != null,
            errorMessage = uiState.errorMessage?.takeIf { uiState.genres.isNotEmpty() },
            continueEnabled = uiState.canContinue,
            onContinue = onContinue,
        )
    }
}

@Composable
private fun GenrePicker(
    genres: List<Genre>,
    selectedGenreId: String?,
    onGenreSelected: (String) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = genres, key = Genre::id) { genre ->
            GenreChip(
                genre = genre,
                selected = genre.id == selectedGenreId,
                onClick = { onGenreSelected(genre.id) },
            )
        }
    }

    Spacer(Modifier.height(20.dp))
}

@Composable
private fun GenreChip(
    genre: Genre,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)
    val genreColor = genre.color.toComposeColor() ?: TrendingNowColors.RisingCreatorTag

    Row(
        modifier = Modifier
            .clip(shape)
            .then(
                if (selected) {
                    Modifier.background(
                        brush = Brush.horizontalGradient(listOf(
                            Color(0xFFFF2D88),
                            Color(0xFFFF9055),
                        )),
                    )
                } else {
                    Modifier
                        .background(TrendingNowColors.Background)
                        .border(1.dp, genreColor.copy(alpha = 0.85f), shape)
                },
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 15.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        AsyncImage(
            model = genre.logoUrl,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            contentScale = ContentScale.Fit,
        )

        Text(
            text = genre.name,
            color = if (selected) Color.White else TrendingNowColors.CardContent,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter,
            maxLines = 1,
        )
    }
}

@Composable
private fun CreatorList(
    creators: List<GenreCreator>,
    selectedCreatorIds: Set<String>,
    selectionEnabled: Boolean,
    onCreatorToggle: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = creators, key = GenreCreator::id) { creator ->
            CreatorPickerRow(
                creator = creator,
                selected = creator.id in selectedCreatorIds,
                selectionEnabled = selectionEnabled,
                onToggle = { onCreatorToggle(creator.id) },
            )
        }
    }
}

@Composable
private fun CreatorPickerRow(
    creator: GenreCreator,
    selected: Boolean,
    selectionEnabled: Boolean,
    onToggle: () -> Unit,
) {
    val shape = RoundedCornerShape(12.dp)
    val displayName = creator.name.toDisplayCreatorName()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .clip(shape)
            .background(TrendingNowColors.CardSurface)
            .border(
                width = 1.dp,
                color = if (selected) {
                    TrendingNowColors.RisingCreatorTag.copy(alpha = 0.75f)
                } else {
                    TrendingNowColors.CardBorder.copy(alpha = 0.38f)
                },
                shape = shape,
            )
            .padding(9.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = creator.imageUrl,
            contentDescription = displayName,
            modifier = Modifier
                .size(74.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(TrendingNowColors.ProfilePlaceholderBackground),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = displayName,
                color = TrendingNowColors.CardTitle,
                fontSize = 17.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = TrendingNowTypography.Anton,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = creator.role,
                color = TrendingNowColors.CardContent,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = TrendingNowTypography.Inter,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }

        CreatorSelectionButton(
            creatorName = displayName,
            selected = selected,
            enabled = selectionEnabled,
            onClick = onToggle,
        )
    }
}

@Composable
private fun CreatorSelectionButton(
    creatorName: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val animationDuration = 500

    val selectionProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(animationDuration),
        label = "selectionProgress"
    )

    val blurRadius by animateDpAsState(
        targetValue = if (selected) 6.dp else 0.dp,
        animationSpec = tween(animationDuration),
        label = "blurRadius"
    )

    val borderColor by animateColorAsState(
        targetValue = if (selected) TrendingNowColors.RisingCreatorTag else TrendingNowColors.CardContent,
        animationSpec = tween(animationDuration),
        label = "borderColor"
    )

    val borderBlurRadius by animateDpAsState(
        targetValue = if (selected) 2.dp else 0.dp,
        animationSpec = tween(animationDuration),
        label = "borderBlurRadius"
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .semantics {
                role = Role.Button
                contentDescription = if (selected) "Remove $creatorName" else "Add $creatorName"
            }
            .clip(CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        // Blurred Border Layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(borderBlurRadius)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = CircleShape,
                )
        )

        // Blurred Gradient Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(blurRadius)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFF2D88).copy(alpha = selectionProgress),
                            Color(0xFFFF9055).copy(alpha = selectionProgress)
                        )
                    )
                )
        )

        AnimatedContent(
            targetState = selected,
            transitionSpec = {
                val duration = animationDuration
                if (targetState) {
                    (slideInVertically(animationSpec = tween(duration)) { it } + fadeIn(animationSpec = tween(duration))) togetherWith
                            (slideOutVertically(animationSpec = tween(duration)) { -it } + fadeOut(animationSpec = tween(duration)))
                } else {
                    (slideInVertically(animationSpec = tween(duration)) { -it } + fadeIn(animationSpec = tween(duration))) togetherWith
                            (slideOutVertically(animationSpec = tween(duration)) { it } + fadeOut(animationSpec = tween(duration)))
                }
            },
            label = "SelectionAnimation"
        ) { isSelected ->
            Text(
                text = if (isSelected) "✓" else "+",
                color = if (isSelected) Color.White else TrendingNowColors.CardContent,
                fontSize = if (isSelected) 23.sp else 32.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = TrendingNowTypography.Inter,
            )
        }
    }
}

@Composable
private fun PickerBottomBar(
    selectedCount: Int,
    isSubmitting: Boolean,
    isRetry: Boolean,
    errorMessage: String?,
    continueEnabled: Boolean,
    onContinue: () -> Unit,
) {
    val shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(TrendingNowColors.CardSurface)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(TrendingNowColors.CardGradient),
                shape = shape,
            )
            .padding(horizontal = 24.dp, vertical = 16.dp),
    ) {
        errorMessage?.let { message ->
            Text(
                text = message,
                modifier = Modifier.padding(bottom = 10.dp),
                color = TrendingNowColors.Logout,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = TrendingNowTypography.Inter,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$selectedCount Selected",
                    color = TrendingNowColors.CardTitle,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = TrendingNowTypography.Inter,
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Your vibe. Your creators.\nYour feed.",
                    color = TrendingNowColors.CardContent,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = TrendingNowTypography.Inter,
                )
            }

            ContinueButton(
                text = if (isRetry) "Try Again" else "Continue",
                enabled = continueEnabled,
                isLoading = isSubmitting,
                onClick = onContinue,
            )
        }
    }
}

@Composable
private fun ContinueButton(
    text: String,
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .width(124.dp)
            .height(46.dp),
        shape = RoundedCornerShape(7.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = TrendingNowColors.RisingCreatorTag,
            disabledContainerColor = Color(0xFF5D5963),
            disabledContentColor = Color(0xFFBDB9C2),
        ),
        contentPadding = PaddingValues(horizontal = 12.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = TrendingNowColors.RisingCreatorTag,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = "$text  ›",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = TrendingNowTypography.Inter,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun PickerLoadError(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            color = TrendingNowColors.CardContent,
            fontSize = 15.sp,
            fontFamily = TrendingNowTypography.Inter,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = TrendingNowColors.RisingCreatorTag,
            ),
        ) {
            Text(
                text = "Try Again",
                fontFamily = TrendingNowTypography.Inter,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun GuestPickerOverlay(onSignUp: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background.copy(alpha = 0.72f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            )
            .safeDrawingPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_shield_guestuser),
                contentDescription = null,
                modifier = Modifier.size(72.dp),
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Build Your Personal Feed",
                color = TrendingNowColors.CardTitle,
                fontSize = 24.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.04.em,
                fontWeight = FontWeight.Normal,
                fontFamily = TrendingNowTypography.Anton,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Create an account to choose your favorite creators and get a feed tailored just for you.",
                color = TrendingNowColors.CardTitle,
                fontSize = 18.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.04.em,
                fontWeight = FontWeight.SemiBold,
                fontFamily = TrendingNowTypography.Inter,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(35.dp))

            GradientAccentButton(
                text = "Sign Up",
                modifier = Modifier.fillMaxWidth(),
                height = 44.dp,
                onClick = onSignUp,
            )
        }
    }
}

private fun String.toDisplayCreatorName(): String = replace("_", " ").trim()

private fun String?.toComposeColor(): Color? {
    if (isNullOrBlank()) return null

    return runCatching {
        Color(AndroidColor.parseColor(this))
    }.getOrNull()
}
