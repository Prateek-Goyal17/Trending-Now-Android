package com.trending.now.app.feature.creator.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.creator.data.remote.CreatorBuzzingCardResponse

data class CreatorBuzzCardUiModel(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val creator: String,
    val topic: String,
)

@Composable
fun CreatorBuzzCard(
    buzzCard: CreatorBuzzCardUiModel,
    modifier: Modifier = Modifier,
    onCardClick: (CreatorBuzzCardUiModel) -> Unit = {},
) {
    val cardShape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .width(170.dp)
            .clip(cardShape)
            .background(TrendingNowColors.CardTitle)
            .clickable { onCardClick(buzzCard) },
    ) {
        AsyncImage(
            model = buzzCard.imageUrl,
            contentDescription = buzzCard.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0x33FF2D88),
                            Color(0xDDFF4D67),
                        ),
                    ),
                ),
        )

        Text(
            text = buzzCard.title,
            color = Color.White,
            fontSize = 15.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = TrendingNowTypography.Anton,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = 14.dp,
                    end = 14.dp,
                    bottom = 11.dp,
                ),
        )
    }
}

fun CreatorBuzzingCardResponse.toCreatorBuzzCardUiModel(): CreatorBuzzCardUiModel? {
    val firstPost = posts.orEmpty().firstOrNull()
    val title = firstPost?.text?.trim().orEmpty()

    if (title.isBlank()) {
        return null
    }

    val imageUrl = firstPost
        ?.media
        .orEmpty()
        .firstOrNull { mediaItem ->
            mediaItem.type.equals("image", ignoreCase = true)
        }
        ?.let { mediaItem ->
            mediaItem.imageUrl ?: mediaItem.url
        }

    return CreatorBuzzCardUiModel(
        id = id ?: title,
        title = title,
        imageUrl = imageUrl,
        creator = creator?.replace('_', ' ').orEmpty(),
        topic = topic.orEmpty(),
    )
}
