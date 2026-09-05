package com.trending.now.app.feature.home.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.home.presentation.innerShadow
import kotlinx.coroutines.delay

enum class PostCardType {
    Instagram,
    X,
    Shorts,
    News;

    companion object {
        fun from(value: String?): PostCardType {
            val normalized = value.orEmpty().trim().lowercase()
            return when {
                normalized == "x" || "twitter" in normalized || "tweet" in normalized -> X
                "youtube" in normalized || "short" in normalized -> Shorts
                "news" in normalized || "article" in normalized -> News
                else -> Instagram
            }
        }
    }
}

data class PostCardComment(
    val userName: String,
    val text: String,
    val initials: String,
)

@Composable
fun CreatorPostCard(
    modifier: Modifier = Modifier,
    tagLabel: String? = null,
    @DrawableRes tagIcon: Int? = null,
    @DrawableRes postImageRes: Int = R.drawable.ic_samay_home,
    description: String = "\"Samay Raina: Still Alive\" is a deeply personal stand-up comedy special released on YouTube in April 2026, marking the comedian's triumphant return after stepping away from the spotlight.",
    postType: PostCardType = PostCardType.from(tagLabel),
    postImageUrl: String? = null,
    headline: String? = null,
    publishedLabel: String = "2 days ago",
    showPlayButton: Boolean = true,
    isBookmarked: Boolean = false,
    comment: PostCardComment? = PostCardComment(
        userName = "@kapil_r",
        text = "Well deserved 🤩",
        initials = "KR",
    ),
    showCommentInput: Boolean = true,
    onPlayClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {},
) {
    val cardShape = RoundedCornerShape(18.dp)
    val presentation = postType.presentation(tagLabel, tagIcon)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(Color(0xFF1F1115))
            .innerShadow(
                shape = cardShape,
                color = Color(0xFFFF2D88).copy(alpha = 0.55f),
                blur = 3.53.dp,
                offsetX = 0.44.dp,
                offsetY = 0.44.dp,
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            PostMedia(
                imageUrl = postImageUrl,
                imageRes = postImageRes,
                showPlayButton = showPlayButton,
                onPlayClick = onPlayClick,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 13.dp),
            ) {
                PostMetadata(
                    presentation = presentation,
                    publishedLabel = publishedLabel,
                    isBookmarked = isBookmarked,
                    onBookmarkClick = onBookmarkClick,
                )

                if (!headline.isNullOrBlank()) {
                    Text(
                        text = headline,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = TrendingNowTypography.Inter,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }

                Text(
                    text = description,
                    color = BodyText,
                    fontSize = 14.sp,
                    fontFamily = TrendingNowTypography.Inter,
                    fontWeight = FontWeight.Medium
                )

                if (comment != null || showCommentInput) {
                    Spacer(modifier = Modifier.height(15.dp))

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .fillMaxWidth()
                            .height(0.88.dp)
                            .background(Color(0xFF5A1833).copy(0.5f)),
                    )
                }

                comment?.let {
                    PostCommentPreview(
                        comment = it,
                        modifier = Modifier.padding(vertical = 14.dp),
                    )
                }

                if (showCommentInput) {
                    CommentInput(
                        modifier = Modifier.padding(
                            top = if (comment == null) 14.dp else 0.dp,
                            bottom = 14.dp,
                        ),
                    )
                }
            }
        }

        content()
    }
}

@Composable
private fun PostMedia(
    imageUrl: String?,
    @DrawableRes imageRes: Int,
    showPlayButton: Boolean,
    onPlayClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        AsyncImage(
            model = imageUrl?.takeIf(String::isNotBlank) ?: imageRes,
            contentDescription = "Post media",
            placeholder = painterResource(imageRes),
            error = painterResource(imageRes),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        if (showPlayButton) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PlayBackground)
                    .border(0.5.dp, PlayBorder, CircleShape)
                    .clickable(onClick = onPlayClick),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_play),
                    contentDescription = "Play post",
                    modifier = Modifier.size(25.dp),
                )
            }
        }
    }
}

@Composable
private fun PostMetadata(
    presentation: PostCardPresentation,
    publishedLabel: String,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = presentation.accent,
                    shape = RoundedCornerShape(9.dp),
                )
                .padding(horizontal = 8.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            presentation.iconRes?.let { iconRes ->
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                )
            }

            Text(
                text = presentation.label,
                color = presentation.accent,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = TrendingNowTypography.Inter,
            )
        }

        Spacer(modifier = Modifier.width(9.dp))

        Text(
            text = "•",
            color = Color(0xFFA5A5A5),
            fontSize = 10.sp,
            fontFamily = TrendingNowTypography.Inter,
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = publishedLabel,
            color = Color(0xFFA5A5A5),
            fontSize = 10.sp,
            fontFamily = TrendingNowTypography.Inter,
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(R.drawable.ic_save),
            contentDescription = if (isBookmarked) "Remove bookmark" else "Bookmark post",
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(
                if (isBookmarked) BookmarkSelected else BookmarkColor,
            ),
        )
    }
}

@Composable
internal fun PostCommentPreview(
    comment: PostCardComment = DefaultAnimatedComments.first(),
    modifier: Modifier = Modifier,
) {
    val comments = remember(comment) {
        listOf(comment) + DefaultAnimatedComments.filterNot {
            it.userName == comment.userName && it.text == comment.text
        }
    }
    val initialIndex = remember(comments) {
        ((System.currentTimeMillis() / CommentRotationDelayMillis) % comments.size).toInt()
    }
    var currentIndex by remember(comments) { mutableIntStateOf(initialIndex) }

    LaunchedEffect(comments) {
        while (comments.size > 1) {
            val timeUntilNextComment =
                CommentRotationDelayMillis - (System.currentTimeMillis() % CommentRotationDelayMillis)
            delay(timeUntilNextComment)
            currentIndex = (currentIndex + 1) % comments.size
        }
    }

    AnimatedContent(
        targetState = comments[currentIndex],
        modifier = modifier.fillMaxWidth(),
        transitionSpec = {
            (slideInVertically { height -> height } + fadeIn()) togetherWith
                (slideOutVertically { height -> -height } + fadeOut())
        },
        label = "postCommentAnimation",
    ) { animatedComment ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(CommentAvatar),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = animatedComment.initials,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = TrendingNowTypography.Inter,
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = animatedComment.userName,
                    color = MetadataText,
                    fontSize = 11.sp,
                    fontFamily = TrendingNowTypography.Inter,
                    lineHeight = 14.sp,
                )

                Text(
                    text = animatedComment.text,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = TrendingNowTypography.Inter,
                    lineHeight = 17.sp,
                )
            }
        }
    }
}

private data class PostCardPresentation(
    val label: String,
    @DrawableRes val iconRes: Int?,
    val accent: Color,
)

private fun PostCardType.presentation(
    requestedLabel: String?,
    @DrawableRes requestedIcon: Int?,
): PostCardPresentation = when (this) {
    PostCardType.Instagram -> PostCardPresentation(
        label = requestedLabel?.takeIf(String::isNotBlank) ?: "Instagram",
        iconRes = requestedIcon ?: R.drawable.ic_instagram,
        accent = InstagramAccent,
    )

    PostCardType.X -> PostCardPresentation(
        label = if (requestedLabel.equals("Twitter", ignoreCase = true)) {
            "X"
        } else {
            requestedLabel?.takeIf(String::isNotBlank) ?: "X"
        },
        iconRes = requestedIcon ?: R.drawable.ic_twitter,
        accent = XAccent,
    )

    PostCardType.Shorts -> PostCardPresentation(
        label = requestedLabel?.takeIf(String::isNotBlank) ?: "Shorts",
        iconRes = requestedIcon ?: R.drawable.ic_youtube,
        accent = ShortsAccent,
    )

    PostCardType.News -> PostCardPresentation(
        label = requestedLabel?.takeIf(String::isNotBlank) ?: "News",
        iconRes = requestedIcon,
        accent = NewsAccent,
    )
}

private const val MediaAspectRatio = 1.47f
private const val CommentRotationDelayMillis = 3_000L

private val DefaultAnimatedComments = listOf(
    PostCardComment("@kapil_r", "Well deserved 😃", "KR"),
    PostCardComment("@shruti_v", "This is amazing! 🔥", "SV"),
    PostCardComment("@aman_x", "Keep it up bro 🙌", "AX"),
    PostCardComment("@neha_j", "Dhamaka selection 💣", "NJ"),
    PostCardComment("@rahul_s", "Waiting for the special!", "RS"),
)

private val CardBackground = Color(0xFF211216)
private val BorderColor = Color(0xFF59152E)
private val BorderGlow = Color(0x8CFF2D88)
private val DividerColor = Color(0x805A1833)
private val BodyText = Color(0xFFC5BEC1)
private val MetadataText = Color(0xFFAAA3A6)
private val BookmarkColor = Color(0xFFFF2D88)
private val BookmarkSelected = Color(0xFFFF9055)
private val CommentAvatar = Color(0xFFFF4D8D)
private val PlayBackground = Color(0xB36B1439)
private val PlayBorder = Color(0x80FF7BAF)
private val InstagramAccent = Color(0xFFFF2D88)
private val XAccent = Color(0xFFFF5A94)
private val ShortsAccent = Color(0xFFFF1744)
private val NewsAccent = Color(0xFFFFC400)
