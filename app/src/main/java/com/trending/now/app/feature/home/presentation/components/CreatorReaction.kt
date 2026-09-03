package com.trending.now.app.feature.home.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun CreatorReactionCard(
    modifier: Modifier = Modifier,
    text: String = "CarryMinati reacts on samay raina and amitabh bachan KBC."
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = Color(0xFF171721)
            )
            .border(
                width = 1.dp,
                color = Color(0xFF322F42),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp),

        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Reaction icon
        Text(
            text = "✦",
            color = Color(0xFFFF2D88),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        // Reaction text
        Text(
            text = text,
            color = Color.White,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = TrendingNowTypography.Inter
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0C091A
)
@Composable
private fun CreatorReactionCardPreview() {
    CreatorReactionCard()
}