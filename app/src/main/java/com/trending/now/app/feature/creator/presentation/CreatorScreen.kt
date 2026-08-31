package com.trending.now.app.feature.creator.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.common.components.GradientAccentButton
import com.trending.now.app.core.common.components.TrendingNowTextField
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.creator.presentation.components.TrendingVideoCard

@Composable
fun CreatorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 15.dp, vertical = 18.dp),
    ) {
        var searchText by rememberSaveable {
            mutableStateOf("")
        }

        TrendingNowTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = "Find your favorite ",
            highlightedPlaceholder = "Comedian",
            leadingIcon = R.drawable.ic_search,
        )

        Spacer(Modifier.height(35.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(color = Color(0xFF1F1115))
                .innerShadow(
                    shape = RoundedCornerShape(20.dp),
                    shadow = androidx.compose.ui.graphics.shadow.Shadow(
                        radius = 8.dp,
                        spread = 0.dp,
                        color = Color(0xFFFF2D88),
                        offset = DpOffset(
                            x = 0.5.dp,
                            y = 0.5.dp,
                        ),
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(20.dp),
                )

        ){
//            Image(
//                modifier = Modifier
//                    .align(Alignment.Center),
//                painter = painterResource(R.drawable.creater_card_bg),
//                contentDescription = "Creator Card",
//                contentScale = ContentScale.Crop
//            )
            Column(
                modifier = Modifier
                    .padding(top = 27.dp, bottom = 33.dp, start = 24.dp)
            ){
                Text(
                    text = "Build a Feed Around Your Favorite Creators",
                    modifier = Modifier.fillMaxWidth(0.6f),
                    style = TextStyle(
                        fontSize = 22.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = TrendingNowTypography.Anton,
                        color = TrendingNowColors.CardTitle
                    )
                )
                Spacer(Modifier.height(13.dp))
                Text(
                    text = "Choose your favorite creators for a personalized feed.",
                    modifier = Modifier.fillMaxWidth(0.5f),
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = TrendingNowTypography.Inter,
                        color = Color(0xFFB6B6B6)
                    )
                )
                Spacer(Modifier.height(30.dp))

                SocialIconRow()

                Spacer(Modifier.height(12.dp))

                GradientAccentButton(
                    text = "Personalize Feed",
                    contentPadding = PaddingValues(horizontal = 15.dp),
//                    modifier = Modifier.fillMaxWidth().height(45.dp),
//                    height = 45.dp,
                    onClick = {}
                )
            }

            Image(
                modifier = Modifier
                    .height(250.dp)
                    .align(Alignment.BottomEnd),
                painter = painterResource(R.drawable.creator_screen_card_img),
                contentDescription = "Creator Card",
            )
        }

        Text(
            text = "Trending Now",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = TrendingNowTypography.Inter,
                color = TrendingNowColors.CardTitle
            )
        )

        TrendingVideoCard(
            username = "viral_biryani",
            title = "Samay & BB Break the Internet",
            imageUrl = "http://testingtrendingnowbe.boostproductivity.online/cdn/images/1786448579063-43c2541778b9.webp"
        )

    }
}




@Composable
private fun SocialIconRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            R.drawable.ic_creator_instagram,
            R.drawable.ic_creator_youtube,
            R.drawable.ic_creator_twitter,
            R.drawable.ic_creator_news
        ).forEach { label ->
            Image(
                painter = painterResource(label),
                contentDescription = "Icons",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}