package com.trending.now.app.core.common.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun TrendingNowTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    highlightedPlaceholder: String? = null,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    height: androidx.compose.ui.unit.Dp = 56.dp,
    cornerRadius: androidx.compose.ui.unit.Dp = 12.dp,
    backgroundColor: Color = TrendingNowColors.NavigationBackground,
    textColor: Color = Color.White,
    placeholderColor: Color = Color(0xFFE8E3FF),
    highlightedPlaceholderColor: Color = Color(0xFFFF2F87),
    iconTint: Color = Color(0xFFE8E3FF),
) {
    val shape = RoundedCornerShape(cornerRadius)

    val interactionSource = remember {
        MutableInteractionSource()
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(height)
            .background(
                color = backgroundColor,
                shape = shape,
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF168B),
                        Color(0xFFFF8A3D),
                    ),
                ),
                shape = shape,
            ),
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = TextStyle(
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = TrendingNowTypography.Inter,
        ),
        cursorBrush = Brush.verticalGradient(
            listOf(
                Color(0xFFFF168B),
                Color(0xFFFF8A3D),
            ),
        ),
        decorationBox = { innerTextField ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                if (leadingIcon != null) {
                    Icon(
                        painter = painterResource(id = leadingIcon),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp),
                    )

                    Spacer(
                        modifier = Modifier.width(16.dp),
                    )
                }

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = buildAnnotatedString {
                                append(placeholder)

                                if (!highlightedPlaceholder.isNullOrBlank()) {
                                    withStyle(
                                        style = SpanStyle(
                                            color = highlightedPlaceholderColor,
                                        ),
                                    ) {
                                        append(highlightedPlaceholder)
                                    }
                                }
                            },
                            color = placeholderColor,
                            fontFamily = TrendingNowTypography.Inter,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                        )
                    }

                    innerTextField()
                }

                if (trailingIcon != null) {
                    Spacer(
                        modifier = Modifier.width(12.dp),
                    )

                    Icon(
                        painter = painterResource(id = trailingIcon),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        },
    )
}