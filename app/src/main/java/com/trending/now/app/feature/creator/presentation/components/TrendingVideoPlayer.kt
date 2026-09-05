package com.trending.now.app.feature.creator.presentation.components

import android.view.LayoutInflater
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.trending.now.app.R
import kotlinx.coroutines.delay

@Stable
class VideoPlaybackState internal constructor() {
    internal var player by mutableStateOf<ExoPlayer?>(null)
    internal var positionMs = 0L
    internal var userPaused = false
    var isPlaying by mutableStateOf(false)
        internal set
    var isBuffering by mutableStateOf(false)
        internal set
    var hasRenderedFrame by mutableStateOf(false)
        internal set
    var hasError by mutableStateOf(false)
        internal set
    var progress by mutableFloatStateOf(0f)
        internal set

    fun togglePlayback() {
        val currentPlayer = player ?: return
        userPaused = currentPlayer.playWhenReady
        currentPlayer.playWhenReady = !userPaused
    }

    fun retry() {
        hasError = false
        userPaused = false
        player?.apply {
            prepare()
            play()
        }
    }
}

/** Owns a decoder only while this video is visible and its destination is resumed. */
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun rememberVideoPlayback(
    videoUrl: String?,
    enabled: Boolean,
    muted: Boolean = true,
): VideoPlaybackState {
    val context = LocalContext.current.applicationContext
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state = remember(videoUrl) { VideoPlaybackState() }
    var resumed by remember(lifecycle) {
        mutableStateOf(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
    }
    val latestMuted by rememberUpdatedState(muted)

    DisposableEffect(lifecycle, state) {
        val observer = LifecycleEventObserver { _, _ ->
            resumed = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
            // Stop immediately, before recomposition disposes the player.
            if (!resumed) state.player?.pause()
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    DisposableEffect(videoUrl, enabled, resumed) {
        val player = if (enabled && resumed && !videoUrl.isNullOrBlank()) {
            ExoPlayer.Builder(
                context,
                DefaultRenderersFactory(context).setEnableDecoderFallback(true),
            ).build().apply {
                repeatMode = Player.REPEAT_MODE_ONE
                volume = if (latestMuted) 0f else 1f
                setAudioAttributes(AudioAttributes.DEFAULT, !latestMuted)
                setHandleAudioBecomingNoisy(true)
            }
        } else null
        state.player = player
        state.hasRenderedFrame = false
        state.isBuffering = player != null
        val listener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                state.isPlaying = player.isPlaying
                state.hasError = player.playerError != null
                state.isBuffering = player.playbackState == Player.STATE_BUFFERING
            }

            override fun onRenderedFirstFrame() {
                state.hasRenderedFrame = true
            }
        }
        player?.apply {
            addListener(listener)
            setMediaItem(MediaItem.fromUri(videoUrl!!), state.positionMs)
            playWhenReady = !state.userPaused
            prepare()
        }
        onDispose {
            if (player != null) {
                state.positionMs = player.currentPosition.coerceAtLeast(0L)
                player.removeListener(listener)
                player.release()
            }
            state.player = null
            state.isPlaying = false
            state.isBuffering = false
            state.hasRenderedFrame = false
        }
    }

    LaunchedEffect(state.player, muted) {
        state.player?.apply {
            volume = if (muted) 0f else 1f
            setAudioAttributes(AudioAttributes.DEFAULT, !muted)
        }
    }
    LaunchedEffect(state.player) {
        val player = state.player ?: return@LaunchedEffect
        while (true) {
            val duration = player.duration
            state.progress = if (duration != C.TIME_UNSET && duration > 0) {
                (player.currentPosition.toFloat() / duration).coerceIn(0f, 1f)
            } else 0f
            delay(250)
        }
    }
    return state
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun TrendingVideoSurface(
    playback: VideoPlaybackState,
    posterUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(modifier.background(Color.Black)) {
        if (playback.player != null) {
            AndroidView(
                factory = { context ->
                    (LayoutInflater.from(context).inflate(R.layout.trending_video_player, null) as PlayerView)
                        .apply { importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO }
                },
                update = {
                    it.player = playback.player
                    it.keepScreenOn = playback.isPlaying
                },
                onRelease = { it.player = null },
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (!playback.hasRenderedFrame || playback.hasError) {
            AsyncImage(
                model = posterUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
