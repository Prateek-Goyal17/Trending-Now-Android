package com.trending.now.app.feature.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun CreatorInfoCard(
    modifier: Modifier = Modifier,
    creatorName: String = "CarryMinati",
    imageRes: Int = R.drawable.ic_carry_home,
    badgeText: String = "Following",
    badgeIcon: Int? = null,
    showCheckmark: Boolean = true
) {
    Row(
        modifier = modifier
            .height(50.dp),

        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

       
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFF3F9A),
                            Color(0xFFFF9ECA),
                            Color(0xFFFF2D88),
                            Color(0xFFFFA575),
                            Color(0xFFFF9055)
                        )
                    ),
                    shape = CircleShape
                )
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Name + Badge
        Column(
            modifier = Modifier.height(50.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {

            // Creator Name
            Text(
                text = creatorName,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter
            )

            // Badge
            CreatorBadge(
                text = badgeText,
                icon = badgeIcon,
                showCheckmark = showCheckmark
            )
        }
    }
}


@Composable
private fun CreatorBadge(
    text: String,
    icon: Int?,
    showCheckmark: Boolean
) {
    Row(
        modifier = Modifier
            .height(22.dp)
            .clip(RoundedCornerShape(44.dp))
            .background(color = Color(0xFF22000F))
            .border(
                width = 0.88.dp,
                color = Color(0xFFFF2D88),
                shape = RoundedCornerShape(44.dp)
            )
            .padding(horizontal = 8.8.dp, vertical = 4.4.dp),

        horizontalArrangement = Arrangement.spacedBy(4.4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (showCheckmark) {
            // Check Circle
            Box(
                modifier = Modifier
                    .size(13.dp)
                    .background(
                        color = Color(0xFFFF2D88),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    color = Color.Black,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        } else if (icon != null) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(10.dp),
                colorFilter = ColorFilter.tint(Color(0xFFFF2D88))
            )
        }

        // Badge Text
        Text(
            text = text,
            color = Color(0xFFFF2D88),
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = TrendingNowTypography.Inter
        )
    }
}


@Preview(
    showBackground = true,
    backgroundColor = 0xFF0C091A
)
@Composable
private fun CreatorInfoCardPreview() {
    CreatorInfoCard()
}