package com.trending.now.app.core.constants

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.trending.now.app.R

object TrendingNowTypography {
    val Inter = FontFamily(
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_semibold, FontWeight.SemiBold),
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_extrabold, FontWeight.ExtraBold),
        Font(R.font.inter_black, FontWeight.Black),
    )

    val Anton = FontFamily(
        Font(R.font.anton_regular, FontWeight.Normal),
    )
}
