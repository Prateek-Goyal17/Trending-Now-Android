package com.trending.now.app.feature.home.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.home.presentation.components.TopCreator
import kotlinx.coroutines.delay
import java.util.Collections

private data class TrendingCreatorItem(
    val id: Int, // Unique ID for animation key
    val rank: Int,
    val name: String,
    val subtitle: String,
    val imageRes: Int,
    val igCount: String,
    val ytCount: String
)

private val initialTrendingList = listOf(
    TrendingCreatorItem(1, 1, "Bhuvan Bam", "Indian comedian", R.drawable.ic_bhuvam_home, "20M", "20M"),
    TrendingCreatorItem(2, 2, "Harsh Beniwal", "Indian comedian", R.drawable.ic_carry_home, "15M", "12M"),
    TrendingCreatorItem(3, 3, "Sorabh Joshi", "Indian creator", R.drawable.ic_samay_home, "10M", "8M"),
    TrendingCreatorItem(4, 4, "Bhuvan Bam", "Indian comedian", R.drawable.ic_bhuvam_home, "20M", "20M"),
    TrendingCreatorItem(5, 5, "Bhuvan Bam", "Indian comedian", R.drawable.ic_bhuvam_home, "20M", "20M"),
    TrendingCreatorItem(6, 6, "Bhuvan Bam", "Indian comedian", R.drawable.ic_bhuvam_home, "20M", "20M")
)

private val RibbonShape = GenericShape { size, _ ->
    val notch = size.height * 0.28f
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, size.height)
    lineTo(size.width / 2f, size.height - notch)
    lineTo(0f, size.height)
    close()
}

@Composable
fun TrendingCreators(
    onBackClick: () -> Unit = {}
) {
    // Create a mutable list that Compose observes
    val items = remember { mutableStateListOf<TrendingCreatorItem>().apply { addAll(initialTrendingList) } }

    // Track which item is currently being "plucked" and moved
    var movingItemId by remember { mutableStateOf<Int?>(null) }

    // Logic to periodically shift items to simulate real-time trending changes
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Longer interval between moves
            if (items.size > 1) {
                val fromIndex = (0 until items.size).random()
                val toIndex = (0 until items.size).random()
                
                if (fromIndex != toIndex) {
                    val item = items[fromIndex]
                    
                    // 1. "Pluck out" - Slower lifting
                    movingItemId = item.id
                    delay(1200)
                    
                    // 2. "Slide" - Change position in list
                    val itemToMove = items.removeAt(fromIndex)
                    items.add(toIndex, itemToMove)
                    
                    // 3. "Drop" - Wait for slow slide to finish then scale back
                    delay(2500) 
                    movingItemId = null
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.1f))
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_right_arrow),
                        contentDescription = "Back",
                        modifier = Modifier.size(20.dp).graphicsLayer { rotationZ = 180f },
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Trending Creators",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = TrendingNowTypography.Inter,
                        lineHeight = 20.sp,
                        letterSpacing = 0.8.sp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color.White, Color(0xFFFF2D88))
                        )
                    )
                )

                Spacer(modifier = Modifier.weight(1.2f))
            }
        }

        item {
            TopCreator(showViewAll = false)
        }

        itemsIndexed(
            items = items,
            key = { _, item -> item.id } 
        ) { index, item ->
            val isMoving = item.id == movingItemId
            // Slower scale animation for a "heavy" drag feel
            val scale by animateFloatAsState(
                targetValue = if (isMoving) 1.08f else 1f,
                animationSpec = spring(dampingRatio = 0.8f, stiffness = 150f)
            )
            val zIndex = if (isMoving) 1f else 0f

            CreatorListItem(
                item = item,
                rank = index + 1,
                modifier = Modifier
                    .animateItem(
                        placementSpec = spring(
                            dampingRatio = 0.85f,
                            stiffness = 100f // Slower sliding movement
                        )
                    ) 
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.shadowElevation = if (isMoving) 16.dp.toPx() else 0f
                        this.translationY = if (isMoving) (-8).dp.toPx() else 0f
                    }
                    .zIndex(zIndex)
            )
        }
    }
}

@Composable
private fun CreatorListItem(
    item: TrendingCreatorItem,
    rank: Int,
    modifier: Modifier = Modifier
) {
    // Determine border color based on position for a "dynamic" feel during shifts
    val highlightColor = when (rank) {
        1 -> Color(0xFFFF2D88)
        2 -> Color(0xFF2F80ED)
        else -> Color.White.copy(alpha = 0.1f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(84.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF171721))
            .border(
                width = 1.5.dp,
                color = highlightColor.copy(alpha = if (rank <= 2) 0.8f else 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Badge
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .width(24.dp)
                    .height(32.dp)
                    .clip(RibbonShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFFFE07A), Color(0xFFFFB800))
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "★",
                    color = Color(0xFF7A4B00),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = rank.toString(),
                    color = Color(0xFF3D2600),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TrendingNowTypography.Inter
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Profile Image
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Name and Subtitle
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TrendingNowTypography.Inter
                )
                Text(
                    text = item.subtitle,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = TrendingNowTypography.Inter
                )
            }

            // Social Badges
            Column(
                modifier = Modifier.padding(end = 12.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                MiniSocialBadge(iconRes = R.drawable.ic_instagram, count = item.igCount)
                MiniSocialBadge(iconRes = R.drawable.ic_youtube, count = item.ytCount)
            }
        }
    }
}

@Composable
private fun MiniSocialBadge(iconRes: Int, count: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White.copy(alpha = 0.95f))
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .width(48.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(10.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = count,
            color = Color.Black,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter
        )
    }
}
