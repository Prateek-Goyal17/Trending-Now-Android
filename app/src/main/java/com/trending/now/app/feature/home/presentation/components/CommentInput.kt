package com.trending.now.app.feature.home.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CommentInput(
    modifier: Modifier = Modifier
) {
    var commentText by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = Color(0xFF27151A),
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 0.5.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF2D88),
                        Color(0xFFFF9055)
                    )
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                horizontal = 13.dp
            )
    ) {
        BasicTextField(
            value = commentText,
            onValueChange = { commentText = it },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
            ),
            cursorBrush = SolidColor(Color(0xFFFF2D88)),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (commentText.isEmpty()) {
                        Text(
                            text = "Add a comment",
                            color = Color(0xFF6F6669),
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
