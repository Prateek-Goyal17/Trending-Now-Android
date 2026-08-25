package com.trending.now.app.core.constants

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.trending.now.app.R

object TrendingNowTypography {
    val Inter = FontFamily(
        Font(R.font.inter_variable, FontWeight.Normal),
        Font(R.font.inter_variable, FontWeight.Medium),
        Font(R.font.inter_variable, FontWeight.SemiBold),
        Font(R.font.inter_variable, FontWeight.Bold),
        Font(R.font.inter_variable, FontWeight.ExtraBold),
    )

    val Anton = FontFamily(
        Font(R.font.anton_regular, FontWeight.Normal),
    )
}
