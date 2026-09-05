package com.trending.now.app.feature.me.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.trending.now.app.R
import com.trending.now.app.core.common.components.GradientAccentButton
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.me.presentation.components.TopbarInfo

@Composable
fun ReportProblemScreen(
    onBack: () -> Unit = {},
    viewModel: ReportProblemViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val maxChar = 150

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.onImageSelected(uri)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            android.widget.Toast.makeText(context, "Problem reported successfully!", android.widget.Toast.LENGTH_SHORT).show()
            viewModel.clearSuccess()
            onBack()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            android.widget.Toast.makeText(context, it, android.widget.Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding()
            .padding(horizontal = 20.dp)
    ) {
        // 1. Header
        TopbarInfo(
            title = "Report a problem",
            onBackClick = onBack
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Description Field (Takes remaining space)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF171721), RoundedCornerShape(5.dp))
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(16.dp)
        ) {
            BasicTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                modifier = Modifier.fillMaxSize(),
                enabled = !uiState.isLoading,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = TrendingNowTypography.Inter
                ),
                cursorBrush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFF168B), Color(0xFFFF8A3D))
                ),
                decorationBox = { innerTextField ->
                    if (uiState.description.isEmpty()) {
                        Text(
                            text = "Describe the issue you're facing...",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontFamily = TrendingNowTypography.Inter
                        )
                    }
                    innerTextField()
                }
            )

            Text(
                text = "${uiState.description.length}/$maxChar",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 4.dp, end = 4.dp),
                fontFamily = TrendingNowTypography.Inter
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. Bottom Section (Screenshot, Info, Button)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Attach Screenshot Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(Color(0xFF171721), RoundedCornerShape(5.dp))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .clickable(enabled = !uiState.isLoading) {
                        imagePicker.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                if (uiState.imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(uiState.imageUri),
                        contentDescription = "Selected screenshot",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(5.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_gallery_icon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(37.5.dp)
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Attach Screenshot",
                            color = Color(0xFFFF5375),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = TrendingNowTypography.Inter
                        )
                    }
                }
            }

            // Info Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = null,
                    tint = Color(0xFFFF5375),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "The more details you share, the easier it is for us to help.",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = TrendingNowTypography.Inter,
                    lineHeight = 20.sp,
                    letterSpacing = 0.04.em
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Send Button
            GradientAccentButton(
                text = if (uiState.isLoading) "Sending..." else "Send",
                modifier = Modifier.fillMaxWidth().height(50.dp),
                height = 50.dp,
                onClick = {
                    if (!uiState.isLoading) {
                        viewModel.submitReport(context)
                    }
                }
            )
        }
    }
}
