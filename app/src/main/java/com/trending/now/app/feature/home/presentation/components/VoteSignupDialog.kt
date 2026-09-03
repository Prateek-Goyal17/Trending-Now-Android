package com.trending.now.app.feature.home.presentation.components

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowTypography

import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.DialogProperties
import com.trending.now.app.core.common.components.GradientAccentButton

@Composable
fun VoteSignupDialog(
    onDismissRequest: () -> Unit,
    onSignUpClick: () -> Unit
) {
    val dialogShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .dropShadow(
                        shape = dialogShape,
                        shadow = Shadow(
                            radius = 4.dp,
                            spread = 0.dp,
                            offset = DpOffset(x = 0.dp, y = (-2).dp),
                            color = Color(0xFFFF2D88).copy(alpha = 0.5f)
                        )
                    )
                    .clip(dialogShape)
                    .background(Color(0xFF0C091A))
                    .padding(start = 24.dp, end = 24.dp, top = 15.dp, bottom = 32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(2.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(Color(0xFFD9D9D9))
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    Image(
                        painter = painterResource(id = R.drawable.ic_lock_icon_home),
                        contentDescription = null,
                        modifier = Modifier.size(58.dp),
                        colorFilter = ColorFilter.tint(Color(0xFFFF2D88))
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = "Make Your Vote Count",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = TrendingNowTypography.Anton,
                        lineHeight = 24.sp,
                        letterSpacing = 0.96.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Create an account to save posts and access them anytime.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 18.sp,
                        letterSpacing = 0.72.sp,
                        fontFamily = TrendingNowTypography.Inter,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    GradientAccentButton(
                        text = "Sign Up",
                        modifier = Modifier.fillMaxWidth(),
                        height = 50.dp,
                        textFontSize = 16.0,
                        onClick = onSignUpClick
                    )
                }
            }
        }
    }
}
