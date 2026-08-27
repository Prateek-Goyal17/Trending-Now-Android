package com.trending.now.app.core.common.webview

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography

@Composable
fun AppWebViewScreen(
    title: String,
    url: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding()
            .padding(horizontal = 15.dp, vertical = 18.dp),
    ) {
        Text(
            text = title,
            color = TrendingNowColors.CardTitle,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = TrendingNowTypography.Inter,
        )
        Spacer(Modifier.height(16.dp))
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .border(
                    width = 1.dp,
                    color = TrendingNowColors.CardBorder.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),
                ),
            factory = { context ->
                WebView(context).apply {
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            },
            update = { webView ->
                if (webView.url != url) {
                    webView.loadUrl(url)
                }
            },
        )
    }
}
