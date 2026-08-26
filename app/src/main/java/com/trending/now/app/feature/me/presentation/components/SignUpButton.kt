package com.trending.now.app.feature.me.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun SignUpButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(6.dp)

    Box(
        modifier = modifier.height(33.dp),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 3.dp)
                .clip(shape)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            TrendingNowColors.RisingCreatorTag,
                            TrendingNowColors.SignUpButtonGradientEnd,
                        ),
                    ),
                ),
        )

        Button(
            onClick = onClick,
            modifier = Modifier.height(33.dp),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = TrendingNowColors.CardTitle,
                contentColor = TrendingNowColors.SignUpButtonContent,
            ),
            contentPadding = PaddingValues(
                horizontal = 33.dp,
                vertical = 0.dp,
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
            ),
        ) {
            Text(
                text = "Sign Up",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter,
            )
        }
    }
}
