package com.trending.now.app.feature.home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.home.presentation.innerShadow

@Composable
fun CommunityFeedbackCard(
    modifier: Modifier = Modifier,
    onVoteAttempt: () -> Unit = {}
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .innerShadow(
                shape = RoundedCornerShape(17.96.dp),
                color = Color(0xFFFF2D88).copy(alpha = 0.55f),
                blur = 4.49.dp,
                offsetX = 0.45.dp,
                offsetY = 0.45.dp
            )
            .clip(RoundedCornerShape(17.96.dp))
            .background(Color(0xFF1F1115))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF8A2BE2), Color(0xFF4B0082))
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .background(
                            color = Color(0x338A2BE2),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_community),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        colorFilter = ColorFilter.tint(Color(0xFFB57EDC))
                    )
                    Text(
                        text = "Community Feedback",
                        color = Color(0xFFB57EDC),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = TrendingNowTypography.Inter
                    )
                }

                Text(
                    text = "2.4K fans voted",
                    color = Color(0xFF9B51E0),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TrendingNowTypography.Inter
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "\"Still Alive\" Is Worth All the Buzz.",
                color = Color.White,
                fontSize = 22.sp,
                fontFamily = TrendingNowTypography.Anton
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "What do you think? 👇",
                color = Color(0xFFB0B0B0),
                fontSize = 13.sp,
                fontFamily = TrendingNowTypography.Inter
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FeedbackButton(
                    text = "Support",
                    iconRes = R.drawable.ic_play,
                    color = Color(0xFF2F80ED),
                    isSelected = selectedOption == "Support",
                    percentage = if (selectedOption != null) 0.65f else 0f,
                    onClick = {
                        onVoteAttempt()
                        selectedOption = "Support"
                    }
                )

                FeedbackButton(
                    text = "Oppose",
                    iconRes = R.drawable.ic_play,
                    color = Color(0xFFEB5757),
                    isSelected = selectedOption == "Oppose",
                    percentage = if (selectedOption != null) 0.35f else 0f,
                    onClick = {
                        onVoteAttempt()
                        selectedOption = "Oppose"
                    }
                )
            }
        }
    }
}

@Composable
private fun FeedbackButton(
    text: String,
    iconRes: Int,
    color: Color,
    isSelected: Boolean,
    percentage: Float,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) color else color.copy(alpha = 0.3f)
    val borderWidth = if (isSelected) 1.5.dp else 1.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF171721))
            .border(borderWidth, borderColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        if (percentage > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percentage)
                    .background(color)
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter
            )
        }
    }
}

@Preview
@Composable
fun CommunityFeedbackCardPreview() {
    Box(modifier = Modifier.padding(16.dp)
        ) {
        CommunityFeedbackCard()
    }
}
