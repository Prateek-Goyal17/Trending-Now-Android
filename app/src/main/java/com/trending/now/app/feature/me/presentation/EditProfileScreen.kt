package com.trending.now.app.feature.me.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.trending.now.app.R
import com.trending.now.app.core.common.components.GradientAccentButton
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.me.presentation.components.TopbarInfo

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    viewModel: EditProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            android.widget.Toast.makeText(context, "Profile updated successfully!", android.widget.Toast.LENGTH_SHORT).show()
            viewModel.clearSuccess()
            onBack()
        }
    }

    Scaffold(
        containerColor = TrendingNowColors.Background,
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        topBar = {
            TopbarInfo(
                title = "My Profile",
                onBackClick = onBack,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --------------------------------------------------
            // PROFILE AVATAR
            // --------------------------------------------------

            ProfileAvatarSection(
                initials = getInitials(uiState.firstName, uiState.lastName)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Member since August 2026",
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = TrendingNowTypography.Inter
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            // --------------------------------------------------
            // PROFILE FORM
            // --------------------------------------------------

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                ProfileInputField(
                    label = "First Name",
                    value = uiState.firstName,
                    onValueChange = viewModel::onFirstNameChange
                )

                ProfileInputField(
                    label = "Last Name",
                    value = uiState.lastName,
                    onValueChange = viewModel::onLastNameChange
                )

                ProfileInputField(
                    label = "User Name",
                    value = uiState.username,
                    onValueChange = viewModel::onUsernameChange
                )

                ProfileInputField(
                    label = "Email",
                    value = uiState.email,
                    onValueChange = {},
                    trailingIcon = R.drawable.ic_verfied,
                    enabled = false,
                    trailingIconSize = 24.dp,
                    borderColor = Color.White
                )
            }

            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            // --------------------------------------------------
            // SAVE BUTTON
            // --------------------------------------------------

            val isButtonActive = uiState.hasChanges && !uiState.isLoading

            GradientAccentButton(
                text = if (uiState.isLoading) "Saving..." else "Save",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .graphicsLayer {
                        alpha = if (isButtonActive) 1f else 0.5f
                    },
                height = 50.dp,
                onClick = {
                    if (isButtonActive) {
                        viewModel.updateProfile()
                    }
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

private fun getInitials(firstName: String, lastName: String): String {
    val f = firstName.trim().firstOrNull()?.uppercase() ?: ""
    val l = lastName.trim().firstOrNull()?.uppercase() ?: ""
    return if (f.isEmpty() && l.isEmpty()) "U" else "$f$l"
}

@Composable
private fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: Int? = null,
    enabled: Boolean = true,
    trailingIconSize: androidx.compose.ui.unit.Dp = 24.dp,
    borderColor: Color = Color.White.copy(alpha = 0.1f),
    onIconClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    color = Color(0xFF171721),
                    shape = RoundedCornerShape(5.dp)
                )
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    enabled = enabled,
                    textStyle = TextStyle(
                        color = if (enabled) Color.White else Color.White.copy(alpha = 0.5f),
                        fontSize = 16.sp,
                        fontFamily = TrendingNowTypography.Inter
                    ),
                    cursorBrush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFF168B),
                            Color(0xFFFF8A3D)
                        )
                    )
                )

                trailingIcon?.let { icon ->

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(trailingIconSize)
                            .clickable { onIconClick() }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileAvatarSection(
    initials: String
) {
    Box(
        modifier = Modifier
            .size(110.dp)
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    TrendingNowColors.UserProfileStrokeGradient
                )
            )
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(TrendingNowColors.Background)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF9426F0),
                                Color(0xFF5B12C9)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = initials,
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = TrendingNowTypography.Inter
                )
            }
        }
    }
}

