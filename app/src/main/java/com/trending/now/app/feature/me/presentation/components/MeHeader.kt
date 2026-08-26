package com.trending.now.app.feature.me.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun MeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TrendingNowColors.CardTitle)) {
                        append("Trending")
                    }
                    append("\n")
                    withStyle(SpanStyle(color = TrendingNowColors.RisingCreatorTag)) {
                        append("Now")
                    }
                },
                fontSize = 22.sp,
                lineHeight = 18.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.W900,
                fontFamily = TrendingNowTypography.Inter,
            )
            Image(
                painter = painterResource(id = R.drawable.ic_electric),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(start = 82.dp)
                    .size(50.dp)
                    .align(Alignment.CenterEnd),
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = "Notifications",
            modifier = Modifier.size(25.dp),
            tint = TrendingNowColors.CardTitle,
        )
    }
}
