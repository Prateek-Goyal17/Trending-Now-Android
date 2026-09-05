package com.trending.now.app.feature.creator.presentation

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.core.data.remote.SocialPostMediaResponse
import com.trending.now.app.core.data.remote.SocialPostResponse
import com.trending.now.app.feature.creator.data.remote.CreatorDetailDataResponse
import com.trending.now.app.feature.creator.data.remote.CreatorDetailSectionsResponse
import java.time.Duration
import java.time.Instant


@Composable
fun CreatorDetailScreen(
    creatorSlug: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreatorDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(creatorSlug) {
        viewModel.loadCreatorDetail(creatorSlug)
    }

    when {
        uiState.isLoading && uiState.creatorDetail == null -> {
            CreatorDetailLoader(modifier)
        }

        uiState.errorMessage != null && uiState.creatorDetail == null -> {
            CreatorDetailError(
                message = uiState.errorMessage,
                onRetry = {
                    viewModel.loadCreatorDetail(creatorSlug)
                },
                modifier = modifier,
            )
        }

        else -> {
            CreatorDetailContent(
                creatorSlug = creatorSlug,
                data = uiState.creatorDetail?.data,
                selectedTab = uiState.selectedTab,
                onTabSelected = viewModel::selectTab,
                onBack = onBack,
                modifier = modifier,
            )
        }
    }
}


@Composable
private fun CreatorDetailContent(
    creatorSlug: String,
    data: CreatorDetailDataResponse?,
    selectedTab: CreatorDetailTab,
    onTabSelected: (CreatorDetailTab) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val creatorName = data?.creatorName
        ?.toDisplayName()
        .takeUnless { it.isNullOrBlank() }
        ?: creatorSlug.toDisplayName()

    val posts = data
        .postsForTab(selectedTab)
        .map { post ->
            post.toCreatorDetailPostUiModel()
        }

    val lazyListState = rememberLazyListState()

    var isUpNextExpanded by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (lazyListState.isScrollInProgress) {
            isUpNextExpanded = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding(),
    ) {

        // ---------------------------------------------------------
        // MAIN SCROLLABLE CONTENT
        // Wrapped inside one large pink bordered/glowing container.
        // All existing LazyColumn content and functionality stays the same.
        // ---------------------------------------------------------
        val containerShape = RoundedCornerShape(18.dp)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .dropShadow(
                    shape = containerShape,
                    shadow = Shadow(
                        radius = 15.dp,
                        spread = 0.dp,
                        offset = DpOffset(x = 0.98.dp, y = 0.98.dp),
                        color = TrendingNowColors.RisingCreatorTag.copy(alpha = 0.60f),
                    ),
                )
                .innerShadow(
                    shape = containerShape,
                    shadow = Shadow(
                        radius = 3.93.dp,
                        spread = 0.dp,
                        offset = DpOffset(x = 0.dp, y = 0.98.dp),
                        color = TrendingNowColors.RisingCreatorTag,
                    ),
                )
                .border(
                    width = 1.5.dp,
                    color = TrendingNowColors.RisingCreatorTag.copy(alpha = 0.5f),
                    shape = containerShape,
                )
                .background(
                    color = TrendingNowColors.Background,
                    shape = containerShape,
                )
                .clip(containerShape),
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = 112.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(22.dp),
            ) {

                item {
                    CreatorDetailHero(
                        creatorName = creatorName,
                        role = data?.role ?: "--",
                        imageUrl = data?.bannerImage ?: "",
                        instagramFollowers = data
                            ?.socialFollows
                            ?.instaFollowers
                            .formatFollowerCount(),
                        youtubeSubscribers = data
                            ?.socialFollows
                            ?.youtubeSubs
                            .orEmpty()
                            .ifBlank { "--" },
                        onBack = onBack,
                    )
                }

                item {
                    Box(modifier = Modifier.padding(horizontal = 12.35.dp)) {
                        CreatorDetailTabs(
                            selectedTab = selectedTab,
                            onTabSelected = onTabSelected,
                        )
                    }
                }

                if (posts.isEmpty()) {
                    item {
                        Text(
                            text = "No posts yet.",
                            color = TrendingNowColors.CardContent,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = TrendingNowTypography.Inter,
                            modifier = Modifier.padding(
                                start = 12.35.dp,
                                end = 12.35.dp,
                                top = 12.dp,
                            ),
                        )
                    }
                } else {
                    items(
                        items = posts,
                        key = { post ->
                            post.id
                        },
                    ) { post ->
                        Box(modifier = Modifier.padding(horizontal = 12.35.dp)) {
                            CreatorDetailFeedCard(
                                post = post,
                            )
                        }
                    }
                }
            }
        }

        // Up Next Pill - Moved outside the glowing container to be "intact" with screen edge
        HardcodedUpNextPill(
            isExpanded = isUpNextExpanded,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 11.dp), // Height from top of screen
            onClick = {
                isUpNextExpanded = !isUpNextExpanded
            },
        )

        // FLOATING + BUTTON
        // ---------------------------------------------------------
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 24.dp,
                    bottom = 140.dp,
                )
                .size(58.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(
                    width = 2.dp,
                    color = TrendingNowColors.RisingCreatorTag,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "+",
                color = TrendingNowColors.RisingCreatorTag,
                fontSize = 42.sp,
                lineHeight = 42.sp,
                fontWeight = FontWeight.Light,
            )
        }
    }
}


@Composable
private fun CreatorDetailHero(
    creatorName: String,
    role: String,
    imageUrl: String,
    instagramFollowers: String,
    youtubeSubscribers: String,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(379.dp)
            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
            .background(TrendingNowColors.CardSurface),
    ) {

        AsyncImage(
            model = imageUrl,
            contentDescription = creatorName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x661F1115),
                            Color(0xDD391020),
                        ),
                    ),
                ),
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = 30.dp,
                    top = 30.dp,
                )
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF231539))
                .border(
                    width = 1.dp,
                    color = Color(0xFF432130),
                    shape = CircleShape,
                )
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_right_arrow),
                contentDescription = "Back",
                modifier = Modifier
                    .size(18.dp)
                    .rotate(180f),
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    horizontal = 24.dp,
                    vertical = 24.dp,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = creatorName,
                color = Color.White,
                fontSize = 22.94.sp,
                lineHeight = 22.94.sp,
                letterSpacing = 0.04.em,
                fontWeight = FontWeight.Normal,
                fontFamily = TrendingNowTypography.Anton,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = role,
                color = Color.White,
                fontSize = 12.35.sp,
                lineHeight = 12.35.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = TrendingNowTypography.Inter,
            )

            Spacer(
                modifier = Modifier.height(14.dp),
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                CreatorSocialPill(
                    icon = R.drawable.ic_instagram,
                    value = instagramFollowers,
                )

                CreatorSocialPill(
                    icon = R.drawable.ic_youtube,
                    value = youtubeSubscribers,
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp),
            )

            Button(
                onClick = {},
                shape = RoundedCornerShape(7.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = TrendingNowColors.RisingCreatorTag,
                ),
                contentPadding = PaddingValues(
                    horizontal = 20.dp,
                ),
                modifier = Modifier.height(35.dp),
            ) {
                Text(
                    text = "+ Your Creators",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TrendingNowTypography.Inter,
                )
            }
        }
    }
}


@Composable
private fun HardcodedUpNextPill(
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val offsetX by animateDpAsState(
        targetValue = if (isExpanded) {
            0.dp
        } else {
            18.dp
        },
        label = "upNextOffsetX",
    )

    val pillWidth by animateDpAsState(
        targetValue = if (isExpanded) {
            176.dp
        } else {
            58.dp
        },
        label = "upNextWidth",
    )

    val pillPaddingEnd by animateDpAsState(
        targetValue = if (isExpanded) {
            12.dp
        } else {
            5.dp
        },
        label = "upNextPaddingEnd",
    )

    Row(
        modifier = modifier
            .zIndex(100f)
            .offset(
                x = offsetX,
                y = 60.dp,
            )
            .width(pillWidth)
            .height(58.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 30.dp,
                    bottomStart = 30.dp,
                ),
            )
            .background(Color(0xEE120A19))
            .border(
                width = 1.dp,
                color = TrendingNowColors.RisingCreatorTag.copy(alpha = 0.8f),
                shape = RoundedCornerShape(
                    topStart = 30.dp,
                    bottomStart = 30.dp,
                ),
            )
            .clickable(onClick = onClick)
            .padding(
                start = 5.dp,
                end = pillPaddingEnd,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .border(
                    width = 1.5.dp,
                    color = TrendingNowColors.RisingCreatorTag,
                    shape = CircleShape,
                )
                .background(Color(0xFF3A1B3C)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_carry_home),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        if (isExpanded) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "Up Next »",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = TrendingNowTypography.Inter,
                    maxLines = 1,
                )

                Text(
                    text = "CarryMinati",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TrendingNowTypography.Inter,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}


@Composable
private fun CreatorSocialPill(
    @DrawableRes icon: Int,
    value: String,
) {
    val pillShape = RoundedCornerShape(20.dp)
    val circleShape = CircleShape

    Box(
        modifier = Modifier.height(48.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // The background pill (rounded rectangle)
        Box(
            modifier = Modifier
                .padding(start = 24.dp)
                .height(34.dp)
                .background(Color.White, pillShape)
                .padding(start = 28.dp, end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                color = TrendingNowColors.RisingCreatorTag,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter,
            )
        }

        // The circular icon container
        Box(
            modifier = Modifier
                .size(48.dp)
                .dropShadow(
                    shape = circleShape,
                    shadow = Shadow(
                        radius = 8.dp,
                        spread = 0.dp,
                        offset = DpOffset(x = 1.dp, y = 1.dp),
                        color = TrendingNowColors.RisingCreatorTag.copy(alpha = 0.45f),
                    ),
                )
                .background(Color.White, circleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
            )
        }
    }
}


@Composable
private fun CreatorDetailTabs(
    selectedTab: CreatorDetailTab,
    onTabSelected: (CreatorDetailTab) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        CreatorDetailTab.entries.forEach { tab ->
            Column(
                modifier = Modifier
                    .width(105.dp)
                    .clickable {
                        onTabSelected(tab)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = tab.label,
                    color = if (tab == selectedTab) {
                        TrendingNowColors.RisingCreatorTag
                    } else {
                        TrendingNowColors.MutedText
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = TrendingNowTypography.Inter,
                )

                Spacer(
                    modifier = Modifier.height(12.dp),
                )

                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth(0.76f)
                        .background(
                            if (tab == selectedTab) {
                                TrendingNowColors.RisingCreatorTag
                            } else {
                                Color.Transparent
                            },
                        ),
                )
            }
        }
    }
}


@Composable
private fun CreatorDetailFeedCard(
    post: CreatorDetailPostUiModel,
) {
    val cardShape = RoundedCornerShape(14.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .dropShadow(
                shape = cardShape,
                shadow = Shadow(
                    radius = 15.dp,
                    spread = 0.dp,
                    offset = DpOffset(x = 0.98.dp, y = 0.98.dp),
                    color = TrendingNowColors.RisingCreatorTag.copy(alpha = 0.60f),
                ),
            )
            .innerShadow(
                shape = cardShape,
                shadow = Shadow(
                    radius = 3.93.dp,
                    spread = 0.dp,
                    offset = DpOffset(x = 0.dp, y = 0.98.dp),
                    color = TrendingNowColors.RisingCreatorTag,
                ),
            )
            .border(
                width = 1.5.dp,
                color = TrendingNowColors.RisingCreatorTag.copy(alpha = 0.5f),
                shape = cardShape,
            )
            .background(
                color = Color(0xFF231016),
                shape = cardShape,
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(cardShape),
        ) {
            // Image stays edge-to-edge inside the outer card.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(338.dp)
                    .background(TrendingNowColors.CardSurface),
            ) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = post.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )

                if (post.hasPlayableMedia) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(57.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        TrendingNowColors.RisingCreatorTag,
                                        TrendingNowColors.SignUpButtonGradientEnd,
                                    ),
                                ),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_play),
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                        )
                    }
                }
            }

            // Only the post content gets the requested horizontal padding.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.35.dp,
                        vertical = 18.dp,
                    ),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    PlatformChip(
                        platform = post.platform,
                    )

                    Text(
                        text = "- ${post.timeAgo}",
                        color = TrendingNowColors.MutedText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = TrendingNowTypography.Inter,
                    )
                }

                Spacer(
                    modifier = Modifier.height(17.dp),
                )

                Text(
                    text = post.title,
                    color = Color(0xFFD4CDD3),
                    fontSize = 14.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = TrendingNowTypography.Inter,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(
                    modifier = Modifier.height(24.dp),
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0x44FF2D88)),
                )

                Spacer(
                    modifier = Modifier.height(18.dp),
                )

                HardcodedComment()
            }
        }
    }
}

@Composable
private fun PlatformChip(
    platform: CreatorDetailPlatform,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = TrendingNowColors.RisingCreatorTag,
                shape = RoundedCornerShape(10.dp),
            )
            .padding(
                horizontal = 8.dp,
                vertical = 5.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Image(
            painter = painterResource(platform.icon),
            contentDescription = platform.label,
            modifier = Modifier.size(14.dp),
        )

        Text(
            text = platform.label,
            color = TrendingNowColors.RisingCreatorTag,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = TrendingNowTypography.Inter,
        )
    }
}


@Composable
private fun HardcodedComment() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(47.dp)
                .clip(CircleShape)
                .background(TrendingNowColors.RisingCreatorTag),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "KR",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter,
            )
        }

        Column {
            Text(
                text = "@kapil_r",
                color = TrendingNowColors.MutedText,
                fontSize = 12.sp,
                fontFamily = TrendingNowTypography.Inter,
            )

            Text(
                text = "Well deserved",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = TrendingNowTypography.Inter,
            )
        }
    }
}


@Composable
private fun CreatorDetailLoader(
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


@Composable
private fun CreatorDetailError(
    message: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message.orEmpty(),
            color = TrendingNowColors.CardTitle,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = TrendingNowTypography.Inter,
        )

        Spacer(
            modifier = Modifier.height(16.dp),
        )

        Button(
            onClick = onRetry,
        ) {
            Text("Retry")
        }
    }
}


private data class CreatorDetailPostUiModel(
    val id: String,
    val platform: CreatorDetailPlatform,
    val title: String,
    val imageUrl: String?,
    val timeAgo: String,
    val hasPlayableMedia: Boolean,
)


private enum class CreatorDetailPlatform(
    val label: String,
    @DrawableRes val icon: Int,
) {
    Instagram(
        "Instagram",
        R.drawable.ic_instagram,
    ),

    Twitter(
        "X",
        R.drawable.ic_twitter,
    ),

    Youtube(
        "YouTube",
        R.drawable.ic_youtube,
    ),

    News(
        "News",
        R.drawable.ic_creator_news,
    ),
}


private val CreatorDetailTab.label: String
    get() = when (this) {
        CreatorDetailTab.AllFeed -> "All Feed"
        CreatorDetailTab.News -> "News"
        CreatorDetailTab.Lifestyle -> "Lifestyle"
    }


private fun CreatorDetailDataResponse?.postsForTab(
    tab: CreatorDetailTab,
): List<SocialPostResponse> {
    if (this == null) {
        return emptyList()
    }

    return when (tab) {
        CreatorDetailTab.AllFeed -> sections.allPosts()
        CreatorDetailTab.News -> categorized?.news.orEmpty()
        CreatorDetailTab.Lifestyle -> categorized?.lifestyle.orEmpty()
    }
}


private fun CreatorDetailSectionsResponse?.allPosts():
        List<SocialPostResponse> {
    if (this == null) {
        return emptyList()
    }

    return instagram.orEmpty() +
            youtubeShorts.orEmpty() +
            twitter.orEmpty() +
            news.orEmpty()
}


private fun SocialPostResponse.toCreatorDetailPostUiModel():
        CreatorDetailPostUiModel {

    val platform = platform.toCreatorDetailPlatform()

    val imageUrl = when (platform) {
        CreatorDetailPlatform.News -> urlToImage

        CreatorDetailPlatform.Youtube -> thumbnailUrl

        CreatorDetailPlatform.Instagram,
        CreatorDetailPlatform.Twitter ->
            media.orEmpty()
                .firstDisplayImageUrl()
                ?: thumbnail
    }

    return CreatorDetailPostUiModel(
        id = id
            ?: shortId
            ?: tweetId
            ?: articleId
            ?: url.orEmpty(),

        platform = platform,

        title = listOfNotNull(
            title,
            description,
            caption,
            text,
            normalizedText,
        ).firstOrNull { value ->
            value.isNotBlank()
        }.orEmpty(),

        imageUrl = imageUrl,

        timeAgo = publishedAt.toTimeAgo(),

        hasPlayableMedia = media.orEmpty().any { mediaItem ->
            mediaItem.videoUrl?.isNotBlank() == true
        } || platform == CreatorDetailPlatform.Youtube,
    )
}


private fun String?.toCreatorDetailPlatform():
        CreatorDetailPlatform {

    return when (orEmpty().lowercase()) {
        "instagram",
        "ig" -> CreatorDetailPlatform.Instagram

        "twitter",
        "x",
        "tw" -> CreatorDetailPlatform.Twitter

        "youtube",
        "youtube_shorts",
        "shorts",
        "short" -> CreatorDetailPlatform.Youtube

        else -> CreatorDetailPlatform.News
    }
}


private fun List<SocialPostMediaResponse>.firstDisplayImageUrl():
        String? {
    return firstNotNullOfOrNull { mediaItem ->
        mediaItem.posterUrl
            ?: mediaItem.poster
            ?: mediaItem.imageUrl
            ?: mediaItem.url
    }
}


private fun String?.toDisplayName(): String {
    return orEmpty().replace('_', ' ')
}


private fun Long?.formatFollowerCount(): String {
    val count = this ?: return "--"

    return when {
        count >= 1_000_000 -> {
            "${trimTrailingZero(count / 1_000_000.0)}M"
        }

        count >= 1_000 -> {
            "${trimTrailingZero(count / 1_000.0)}K"
        }

        else -> {
            count.toString()
        }
    }
}


private fun trimTrailingZero(
    value: Double,
): String {
    return "%.1f"
        .format(value)
        .removeSuffix(".0")
}


private fun String?.toTimeAgo(): String {
    val publishedAt = this ?: return "Recently"

    return runCatching {
        val duration = Duration.between(
            Instant.parse(publishedAt),
            Instant.now(),
        )

        when {
            duration.toDays() > 0 -> {
                "${duration.toDays()} days ago"
            }

            duration.toHours() > 0 -> {
                "${duration.toHours()} hours ago"
            }

            duration.toMinutes() > 0 -> {
                "${duration.toMinutes()} min ago"
            }

            else -> {
                "Just now"
            }
        }
    }.getOrDefault("Recently")
}
