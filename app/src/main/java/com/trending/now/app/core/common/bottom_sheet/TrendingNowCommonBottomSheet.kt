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
import androidx.compose.foundation.layout.width
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
import com.trending.now.app.core.common.components.GradientAccentButton
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
//            .border(
//                width = 1.dp,
//                color = TrendingNowColors.TodayFollowingBorder.copy(alpha = 0.9f),
//                shape = shape,
//            )
            .navigationBarsPadding()
            .padding(horizontal = 42.dp, vertical = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // top handle
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(2.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(Color.White.copy(alpha = 0.88f)),
        )

        Spacer(modifier = Modifier.height(25.dp))

        Image(
            painter = painterResource(id = config.iconRes),
            contentDescription = null,
            modifier = Modifier.size(60.dp),
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = config.title,
            color = TrendingNowColors.CardTitle,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = TrendingNowTypography.Anton,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = config.description,
            color = TrendingNowColors.CardTitle,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = TrendingNowTypography.Inter,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(24.dp))

        GradientAccentButton(
            text = config.primaryButtonText,
            modifier = Modifier.fillMaxWidth().height(45.dp),
            height = 45.dp,
            onClick = onPrimaryClick
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}