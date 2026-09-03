package com.trending.now.app.feature.home.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.core.constants.TrendingNowTypography
import kotlinx.coroutines.delay

data class CommentModel(
    val userName: String,
    val commentText: String,
    val userInitials: String
)

val dummyComments = listOf(
    CommentModel("@kapil_r", "Well deserved 😃", "KR"),
    CommentModel("@shruti_v", "This is amazing! 🔥", "SV"),
    CommentModel("@aman_x", "Keep it up bro 🙌", "AX"),
    CommentModel("@neha_j", "Dhhamaka selection 💣", "NJ"),
    CommentModel("@rahul_s", "Waiting for the special!", "RS")
)

@Composable
fun CommentsShown(
    modifier: Modifier = Modifier
) {
    val rotationDelay = 3000L
    val initialIndex = remember { 
        ((System.currentTimeMillis() / rotationDelay) % dummyComments.size).toInt() 
    }
    var currentIndex by remember { mutableIntStateOf(initialIndex) }

    LaunchedEffect(Unit) {
        while (true) {
            val timeToNextTick = rotationDelay - (System.currentTimeMillis() % rotationDelay)
            delay(timeToNextTick)
            currentIndex = (currentIndex + 1) % dummyComments.size
        }
    }

    AnimatedContent(
        targetState = dummyComments[currentIndex],
        transitionSpec = {
            slideInVertically { height -> height } + fadeIn() togetherWith
                    slideOutVertically { height -> -height } + fadeOut()
        },
        label = "commentAnimation"
    ) { comment ->
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF2D88)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = comment.userInitials,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = comment.userName,
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontFamily = TrendingNowTypography.Inter
                )

                Text(
                    text = comment.commentText,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = TrendingNowTypography.Inter
                )
            }
        }
    }
}
