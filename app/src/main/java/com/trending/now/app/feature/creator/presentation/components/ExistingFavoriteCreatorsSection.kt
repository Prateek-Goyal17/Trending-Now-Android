package com.trending.now.app.feature.creator.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.creator.data.remote.ExistingFavoriteCreatorResponse

data class ExistingFavoriteCreatorUiModel(
    val id: String,
    val slug: String,
    val name: String,
    val imageUrl: String?,
    val newFetchCount: Int,
)

@Composable
fun ExistingFavoriteCreatorsSection(
    creators: List<ExistingFavoriteCreatorUiModel>,
    modifier: Modifier = Modifier,
    onCreatorClick: (ExistingFavoriteCreatorUiModel) -> Unit = {},
) {
    if (creators.isEmpty()) return

    Column(modifier = modifier) {
        Text(
            text = "Favorites",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = TrendingNowTypography.Inter,
                color = TrendingNowColors.CardTitle,
            ),
        )

        LazyRow(
            contentPadding = PaddingValues(top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            items(items = creators, key = { creator -> creator.id }) { creator ->
                ExistingFavoriteCreatorItem(
                    creator = creator,
                    onClick = {
                        onCreatorClick(creator)
                    },
                )
            }
        }
    }
}

@Composable
private fun ExistingFavoriteCreatorItem(
    creator: ExistingFavoriteCreatorUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .width(89.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(89.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                        brush = Brush.sweepGradient(
                            colors = if (creator.newFetchCount > 0) {
                                listOf(
                                    Color(0xFFFF9055),
                                    TrendingNowColors.RisingCreatorTag,
                                    TrendingNowColors.RisingCreatorTag,
                                    Color(0xFFFF9055),
                                )
                            } else {
                                listOf(
                                    Color(0x33FF9055),
                                    TrendingNowColors.RisingCreatorTag.copy(alpha = 0.2f),
                                    TrendingNowColors.RisingCreatorTag.copy(alpha = 0.2f),
                                    Color(0x33FF9055),
                                )
                            },
                        ),
                    )
                    .padding(4.dp),
            ) {
                AsyncImage(
                    model = creator.imageUrl,
                    contentDescription = creator.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(
                            width = 3.dp,
                            color = TrendingNowColors.Background,
                            shape = CircleShape,
                        ),
                )
            }

            if (creator.newFetchCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(TrendingNowColors.RisingCreatorTag)
                        .padding()
                        .border(
                            width = 4.dp,
                            color = TrendingNowColors.Background,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = creator.newFetchCount.toString(),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = TrendingNowTypography.Inter,
                    )
                }
            }
        }

        Text(
            text = creator.name,
            color = TrendingNowColors.CardTitle,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = TrendingNowTypography.Inter,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 8.dp)
        )
    }
}

fun ExistingFavoriteCreatorResponse.toExistingFavoriteCreatorUiModel(): ExistingFavoriteCreatorUiModel {
    val creatorSlug = name.orEmpty()

    return ExistingFavoriteCreatorUiModel(
        id = id ?: creatorSlug,
        slug = creatorSlug,
        name = creatorSlug.replace('_', ' '),
        imageUrl = image,
        newFetchCount = newFetchCount ?: 0,
    )
}
