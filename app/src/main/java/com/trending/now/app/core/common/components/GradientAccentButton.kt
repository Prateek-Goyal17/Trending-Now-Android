package com.trending.now.app.core.common.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun GradientAccentButton(
    text: String,
    modifier: Modifier = Modifier,
    @DrawableRes suffixIcon: Int? = null,
    height: Dp = 33.dp,
    suffixIconSize: Dp = 16.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 33.dp),
    onClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(6.dp)

    val gradientColors = listOf(
        TrendingNowColors.RisingCreatorTag,
        TrendingNowColors.SignUpButtonGradientEnd,
    )

    Box(
        modifier = modifier.height(height),
    ) {

        // Bottom gradient accent
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 2.dp)
                .clip(shape)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = gradientColors,
                    ),
                ),
        )

        Button(
            modifier = modifier,
            onClick = onClick,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = TrendingNowColors.CardTitle,
            ),
            contentPadding = contentPadding,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
            ),
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {

                // Gradient text: top -> bottom
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                brush = Brush.verticalGradient(
                                    colors = gradientColors,
                                ),
                            ),
                        ) {
                            append(text)
                        }
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = TrendingNowTypography.Inter,
                    letterSpacing = 0.4.sp
                )

                suffixIcon?.let { icon ->
                    Spacer(
                        modifier = Modifier.width(8.dp),
                    )

                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.size(suffixIconSize),
                    )
                }
            }
        }
    }
}