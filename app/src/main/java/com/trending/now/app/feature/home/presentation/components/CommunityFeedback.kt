package com.trending.now.app.feature.home.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun CommunityFeedbackCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(380.dp)
            .height(240.dp)
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
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Community Feedback Chip
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

            // Title
            Text(
                text = "\"Still Alive\" Is Worth All the Buzz.",
                color = Color.White,
                fontSize = 22.sp,
                fontFamily = TrendingNowTypography.Anton
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle
            Text(
                text = "What do you think? 👇",
                color = Color(0xFFB0B0B0),
                fontSize = 13.sp,
                fontFamily = TrendingNowTypography.Inter
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FeedbackButton(
                    text = "Support",
                    iconRes = R.drawable.ic_play, // Using a generic placeholder for thumb up
                    borderColor = Color(0xFF2F80ED),
                    textColor = Color.White
                )

                FeedbackButton(
                    text = "Oppose",
                    iconRes = R.drawable.ic_play, // Using a generic placeholder for thumb down
                    borderColor = Color(0xFFEB5757),
                    textColor = Color.White
                )
            }
        }
    }
}

@Composable
private fun FeedbackButton(
    text: String,
    iconRes: Int,
    borderColor: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .background(Color(0xFF171721), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
            Text(
                text = text,
                color = textColor,
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
    Box(modifier = Modifier.padding(16.dp)) {
        CommunityFeedbackCard()
    }
}
