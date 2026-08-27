package com.trending.now.app.feature.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.auth.domain.model.AuthUser
import androidx.compose.ui.res.stringResource

@Composable
fun LoginScreen(
    onGoogleLoginSuccess: (AuthUser) -> Unit,
    onGuestClick: () -> Unit,
    showSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val serverClientId = stringResource(R.string.default_web_client_id)

    LaunchedEffect(uiState.user) {
        uiState.user?.let { user ->
            onGoogleLoginSuccess(user)
            viewModel.consumeSignedInUser()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginUiEvent.ShowSnackbar -> showSnackbar(event.message)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding(),
    ) {
        Image(
            painter = painterResource(R.drawable.sign_up_bg),
            contentDescription = "Sign Up Background",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 43.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TrendingNowLoginTitle()
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Your scape, All in one place.",
                color = TrendingNowColors.CardTitle,
                fontSize = 20.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = TrendingNowTypography.Inter,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(20.dp))
            SocialIconRow()
            Spacer(Modifier.height(50.dp))
            AuthButton(
                text = "Continue with Google",
                leading = {
                    Image(
                        painter = painterResource(R.drawable.ic_google),
                        contentDescription = "Google Icon"
                    )
                },
                isLoading = uiState.isGoogleLoading,
                onClick = {
                    viewModel.signInWithGoogle(
                        context = context,
                        serverClientId = serverClientId,
                    )
                },
            )
            Spacer(Modifier.height(40.dp))
            OrDivider()
            Spacer(Modifier.height(42.dp))
            AuthButton(
                text = "Continue as Guest",
                bgColor = Color(0xFF0C091A),
                textColor = Color.White,
                showShadow = false,
                leading = {
                    Image(
                        painter = painterResource(R.drawable.ic_login_guest),
                        contentDescription = "Guest Icon"
                    )
                },
                onClick = {
                    viewModel.continueAsGuest()
                    onGuestClick()
                },
            )
        }
    }
}

@Composable
private fun TrendingNowLoginTitle() {
    Text(
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            TrendingNowColors.AuthTitleGradientStart,
                            TrendingNowColors.AuthTitleGradientEnd,
                        ),
                    ),
                ),
            ) {
                append("Trending\nNow")
            }
        },
        fontSize = 36.sp,
        lineHeight = 38.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.W900,
        fontFamily = TrendingNowTypography.Inter,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun AuthButton(
    text: String,
    bgColor: Color = Color.White,
    textColor: Color = Color.Black,
    showShadow: Boolean = true,
    leading: @Composable () -> Unit,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(10.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .then(
                if (showShadow) {
                    Modifier.dropShadow(
                        shape = shape,
                        shadow = Shadow(
                            radius = 15.dp,
                            spread = 0.dp,
                            offset = DpOffset(
                                x = 0.5.dp,
                                y = 0.5.dp,
                            ),
                            color = Color(0xFFFF2D88).copy(
                                alpha = 0.60f,
                            ),
                        ),
                    )
                } else {
                    Modifier
                },
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFF2D88),
                        Color(0xFFFF9055),
                    ),
                ),
                shape = shape,
            )
            .clip(shape)
            .background(bgColor)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                enabled = !isLoading,
                onClick = onClick,
            ),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {

        Box(
            modifier = Modifier.size(26.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = textColor,
                    strokeWidth = 2.dp,
                )
            } else {
                leading()
            }
        }

        Spacer(Modifier.width(20.dp))

        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = TrendingNowTypography.Inter,
        )
    }
}

@Composable
private fun SocialIconRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        listOf(
            R.drawable.ic_login_instagram,
            R.drawable.ic_login_youtube,
            R.drawable.ic_login_twitter,
            R.drawable.ic_login_news
        ).forEach { label ->
            Image(
                painter = painterResource(label),
                contentDescription = "Icons",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
private fun OrDivider() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    brush = Brush.horizontalGradient(colors = listOf(Color(0xFFA5A5A5), Color(0xFF0C091A)))
                ),
        )
        Text(
            text = "or",
            modifier = Modifier.padding(horizontal = 10.dp),
            color = TrendingNowColors.CardTitle,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = TrendingNowTypography.Inter,
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    brush = Brush.horizontalGradient(colors = listOf(Color(0xFF0C091A),Color(0xFFA5A5A5)))
                ),
        )
    }
}
