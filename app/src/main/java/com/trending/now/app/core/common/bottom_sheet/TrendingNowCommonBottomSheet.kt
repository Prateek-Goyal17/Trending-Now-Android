@file:OptIn(ExperimentalMaterial3Api::class)

package com.trending.now.app.core.common.bottom_sheet

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography

data class AppBottomSheetConfig(
    val iconRes: Int,
    val title: String,
    val description: String,
    val primaryButtonText: String = "Sign Up",
)

@Composable
fun TrendingNowCommonBottomSheet(
    config: AppBottomSheetConfig,
    onDismiss: () -> Unit,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.Transparent,
        scrimColor = Color.Black.copy(alpha = 0.55f),
        modifier = modifier,
    ) {
        BottomSheetCard(
            config = config,
            onPrimaryClick = onPrimaryClick,
        )
    }
}

@Composable
private fun BottomSheetCard(
    config: AppBottomSheetConfig,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(TrendingNowColors.Background)
            .border(
                width = 1.dp,
                color = TrendingNowColors.TodayFollowingBorder.copy(alpha = 0.9f),
                shape = shape,
            )
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // top handle
        Box(
            modifier = Modifier
                .fillMaxWidth(0.18f)
                .height(3.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(Color.White.copy(alpha = 0.88f)),
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = config.iconRes),
            contentDescription = null,
            modifier = Modifier.size(68.dp),
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = config.title,
            color = TrendingNowColors.CardTitle,
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = TrendingNowTypography.Anton,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp,
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = config.description,
            color = TrendingNowColors.CardTitle,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = TrendingNowTypography.Inter,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.fillMaxWidth(0.88f),
        )

        Spacer(modifier = Modifier.height(24.dp))

        BottomSheetPrimaryButton(
            text = config.primaryButtonText,
            onClick = onPrimaryClick,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
private fun BottomSheetPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(10.dp)

    Box(
        modifier = modifier
            .height(48.dp),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 3.dp)
                .clip(shape)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFF2D88),
                            Color(0xFFFF8A5B),
                        ),
                    ),
                ),
        )

        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF3F3F3),
                contentColor = TrendingNowColors.RisingCreatorTag,
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
            ),
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = TrendingNowTypography.Inter,
            )
        }
    }
}