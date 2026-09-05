package com.trending.now.app.feature.creator.presentation

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.creator.presentation.components.TrendingVideoSurface
import com.trending.now.app.feature.creator.presentation.components.rememberVideoPlayback
import com.trending.now.app.feature.home.presentation.components.PostCommentPreview

@Composable
fun CreatorVideoFeedScreen(
    initialIndex: Int,
    initialPostId: String,
    onBack: () -> Unit,
    viewModel: CreatorViewModel,
    modifier: Modifier = Modifier,
) {
    val window = LocalActivity.current?.window
    val view = LocalView.current
    DisposableEffect(window, view) {
        val controller = window?.let { WindowCompat.getInsetsController(it, view) }
        val previousLightStatusBars = controller?.isAppearanceLightStatusBars
        controller?.isAppearanceLightStatusBars = false
        onDispose {
            if (previousLightStatusBars != null) {
                controller.isAppearanceLightStatusBars = previousLightStatusBars
            }
        }
    }
    val uiState by viewModel.uiState.collectAsState()
    val videos = remember(uiState.videoFeed, uiState.creatorScreenFeed) {
        uiState.videoFeed ?: uiState.creatorScreenFeed?.trendingVideos().orEmpty()
    }
    CreatorVideoFeedContent(
        videos = videos,
        initialIndex = initialIndex,
        initialPostId = initialPostId,
        muted = uiState.isVideoMuted,
        isLoading = uiState.isLoading || (uiState.creatorScreenFeed == null && uiState.errorMessage == null),
        errorMessage = uiState.errorMessage,
        onToggleSound = viewModel::toggleVideoSound,
        onRetry = viewModel::retry,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
internal fun CreatorVideoFeedContent(
    videos: List<TrendingVideo>,
    initialIndex: Int,
    initialPostId: String,
    muted: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onToggleSound: () -> Unit,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(72.dp).padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ReelControlButton(
                icon = ReelIcon.Back,
                label = "Back to creators",
                background = Color.White,
                onClick = onBack,
            )
            if (videos.isNotEmpty()) {
                ReelControlButton(
                    icon = if (muted) ReelIcon.Muted else ReelIcon.Sound,
                    label = if (muted) "Unmute video" else "Mute video",
                    background = Color.White.copy(alpha = 0.1f),
                    tint = Color.White,
                    onClick = onToggleSound,
                )
            }
        }

        if (videos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (isLoading) {
                    CircularProgressIndicator(color = ReelPink)
                } else {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = errorMessage ?: "No trending videos right now.",
                            color = Color.White,
                            fontFamily = TrendingNowTypography.Inter,
                        )
                        Button(onClick = onRetry) { Text("Try again") }
                    }
                }
            }
        } else {
            val pagerState = rememberPagerState(
                initialPage = initialVideoPage(videos, initialPostId, initialIndex),
                pageCount = { videos.size },
            )
            VerticalPager(
                state = pagerState,
                modifier = Modifier.weight(1f).fillMaxWidth(),
                key = { videos[it].id },
            ) { page ->
                CreatorVideoPage(
                    video = videos[page],
                    active = page == pagerState.settledPage && !pagerState.isScrollInProgress,
                    muted = muted,
                )
            }
        }
    }
}

@Composable
private fun CreatorVideoPage(video: TrendingVideo, active: Boolean, muted: Boolean) {
    val playback = rememberVideoPlayback(video.videoUrl, enabled = active, muted = muted)
    Box(Modifier.fillMaxSize().background(Color.Black)) {
        TrendingVideoSurface(
            playback = playback,
            posterUrl = video.posterUrl,
            modifier = Modifier.fillMaxSize(),
        )
        Box(
            Modifier.fillMaxSize().clickable(
                role = Role.Button,
                onClickLabel = if (playback.isPlaying) "Pause video" else "Play video",
                enabled = active && !playback.hasError,
                onClick = playback::togglePlayback,
            ).semantics { contentDescription = video.title },
        )
        when {
            playback.hasError && active -> Column(
                modifier = Modifier.align(Alignment.Center)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.8f)).padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Couldn't play this video.", color = Color.White)
                Button(onClick = playback::retry) { Text("Retry video") }
            }
            playback.isBuffering && active -> CircularProgressIndicator(
                color = ReelPink,
                modifier = Modifier.align(Alignment.Center).size(42.dp),
            )
            !playback.isPlaying -> ReelControlButton(
                icon = ReelIcon.Play,
                label = "Play video",
                background = Color(0xB36B1439),
                onClick = playback::togglePlayback,
                modifier = Modifier.align(Alignment.Center).size(56.dp),
            )
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))),
                )
                .padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 44.dp),
        ) {
            Text(
                text = video.title,
                color = Color.White,
                fontFamily = TrendingNowTypography.Anton,
                fontSize = 21.sp,
                lineHeight = 26.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (video.description.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = video.description,
                    color = Color(0xFFC5BEC1),
                    fontFamily = TrendingNowTypography.Inter,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.height(26.dp))
            // Uses the existing sample comment rotation until real comments are connected.
            PostCommentPreview(
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF29161C))
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(ReelPink, Color(0xFFFF9055))),
                        RoundedCornerShape(10.dp),
                    )
                    .padding(horizontal = 16.dp, vertical = 9.dp),
            )
        }
    }
}

private enum class ReelIcon { Back, Play, Sound, Muted }

@Composable
private fun ReelControlButton(
    icon: ReelIcon,
    label: String,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = ReelPink,
) {
    Box(
        modifier = modifier.size(48.dp)
            .semantics { contentDescription = label }
            .clip(CircleShape).clickable(role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier.fillMaxSize().padding(4.dp).clip(CircleShape).background(background),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(Modifier.size(24.dp)) {
                val scale = size.width / 24f
                fun point(x: Float, y: Float) = Offset(x * scale, y * scale)
                fun line(x1: Float, y1: Float, x2: Float, y2: Float) =
                    drawLine(tint, point(x1, y1), point(x2, y2), 2f * scale, StrokeCap.Round)
                when (icon) {
                    ReelIcon.Back -> {
                        line(14f, 5f, 7f, 12f)
                        line(7f, 12f, 14f, 19f)
                    }
                    ReelIcon.Play -> drawPath(
                        Path().apply {
                            moveTo(6 * scale, 3 * scale)
                            lineTo(21 * scale, 12 * scale)
                            lineTo(6 * scale, 21 * scale)
                            close()
                        },
                        tint,
                    )
                    ReelIcon.Sound, ReelIcon.Muted -> {
                        drawPath(
                            Path().apply {
                                moveTo(3 * scale, 9 * scale)
                                lineTo(7 * scale, 9 * scale)
                                lineTo(12 * scale, 5 * scale)
                                lineTo(12 * scale, 19 * scale)
                                lineTo(7 * scale, 15 * scale)
                                lineTo(3 * scale, 15 * scale)
                                close()
                            },
                            tint,
                        )
                        if (icon == ReelIcon.Muted) {
                            line(16f, 9f, 22f, 15f)
                            line(16f, 15f, 22f, 9f)
                        } else {
                            line(16f, 8f, 18f, 12f)
                            line(18f, 12f, 16f, 16f)
                            line(20f, 5f, 23f, 12f)
                            line(23f, 12f, 20f, 19f)
                        }
                    }
                }
            }
        }
    }
}

private val ReelPink = Color(0xFFFF2D88)
