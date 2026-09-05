@file:Suppress("DEPRECATION")

package com.trending.now.app.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trending.now.app.R
import com.trending.now.app.core.constants.TrendingNowColors
import com.trending.now.app.core.constants.TrendingNowTypography
import com.trending.now.app.feature.creator.presentation.components.CreatorSuggestionBadgeType
import com.trending.now.app.feature.creator.presentation.components.CreatorSuggestionCard
import com.trending.now.app.feature.creator.presentation.components.CreatorSuggestionCardUiModel
import com.trending.now.app.feature.home.presentation.components.CommunityFeedbackCard
import com.trending.now.app.feature.home.presentation.components.CreatorPostCard
import com.trending.now.app.feature.home.presentation.components.CreatorReactionCard
import com.trending.now.app.feature.home.presentation.components.TopCreator
import com.trending.now.app.feature.home.presentation.components.VoteSignupDialog
import com.trending.now.app.feature.me.presentation.components.MeHeader

@Composable
fun HomeScreen(
    onViewAllCreatorsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var showVoteSignupDialog by remember { mutableStateOf(false) }
    val isUserAuthenticated = false 

    val suggestions = remember {
        listOf(
            CreatorSuggestionCardUiModel(
                creatorSlug = "akash-gupta",
                creatorName = "Akash Gupta",
                badgeType = CreatorSuggestionBadgeType.RisingCreator,
                role = "Stand-up Comedian",
                suggestionLine = buildAnnotatedString {
                    append("Loved by ")
                    withStyle(style = SpanStyle(color = TrendingNowColors.RisingCreatorTag)) {
                        append("+2.5K fans")
                    }
                    append(" of Samay Raina")
                },
                suggestionImageUrl = "android.resource://com.trending.now.app/drawable/creator_screen_card_img"
            ),
            CreatorSuggestionCardUiModel(
                creatorSlug = "samay-raina",
                creatorName = "Samay Raina",
                badgeType = CreatorSuggestionBadgeType.TopCreator,
                role = "Comedian & Gamer",
                suggestionLine = buildAnnotatedString {
                    append("Trending in ")
                    withStyle(style = SpanStyle(color = TrendingNowColors.RisingCreatorTag)) {
                        append("Gaming")
                    }
                    append(" circles")
                },
                suggestionImageUrl = "android.resource://com.trending.now.app/drawable/ic_samay_home"
            ),
            CreatorSuggestionCardUiModel(
                creatorSlug = "bhuvan-bam",
                creatorName = "Bhuvan Bam",
                badgeType = CreatorSuggestionBadgeType.TopCreator,
                role = "Digital Creator",
                suggestionLine = buildAnnotatedString {
                    append("Loved by ")
                    withStyle(style = SpanStyle(color = TrendingNowColors.RisingCreatorTag)) {
                        append("+10M fans")
                    }
                    append(" globally")
                },
                suggestionImageUrl = "android.resource://com.trending.now.app/drawable/ic_bhuvam_home"
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TrendingNowColors.Background)
            .padding(contentPadding)
    ) {
        item {
            Box(
                modifier = Modifier.padding(
                    start = 15.dp,
                    end = 15.dp,
                    top = 18.dp
                )
            ) {
                MeHeader()
            }
        }

        item {
            TopCreator(
                onViewAllClick = onViewAllCreatorsClick
            )
        }

        item {
            CreatorInfoCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        item {
            CreatorReactionCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        item {
            CreatorPostCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 6.dp,
                    bottom = 6.dp
                ),
                tagLabel = "Instagram",
                tagIcon = R.drawable.ic_instagram
            )
        }

        item {
            CreatorPostCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 6.dp,
                    bottom = 12.dp
                ),
                tagLabel = "Shorts",
                tagIcon = R.drawable.ic_youtube
            )
        }

        item {
            CommunityFeedbackCard(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 6.dp,
                    bottom = 24.dp
                ),
                onVoteAttempt = {
                    if (!isUserAuthenticated) {
                        showVoteSignupDialog = true
                    }
                }
            )
        }

        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .padding(top = 15.dp, bottom = 35.dp)
            ) {
                Text(
                    text = "You might like",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = TrendingNowTypography.Inter,
                        color = TrendingNowColors.CardTitle,
                    ),
                )

                Spacer(Modifier.height(20.dp))

                CreatorSuggestionCard(
                    creatorSuggestions = suggestions,
                    onExploreClick = { }
                )
            }
        }

        item {
            Text(
                text = "More your vibe",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = TrendingNowTypography.Inter,
                modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 12.dp)
            )
        }

        item {
            CreatorInfoCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                creatorName = "Pranit More",
                imageRes = R.drawable.ic_me,
                badgeText = "Similar to carryminati",
                badgeIcon = R.drawable.ic_creators,
                showCheckmark = false
            )
        }

        item {
            CreatorReactionCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                text = "Bhuvan Bam's Dhindora 2 announcement has reignited fan excitement."
            )
        }

        item {
            CreatorPostCard(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
                tagLabel = "Instagram",
                postImageRes = R.drawable.ic_samay_home,
                description = "\"Samay Raina: Still Alive\" is a deeply personal stand-up comedy special released on YouTube in April 2026."
            )
        }
    }

    if (showVoteSignupDialog) {
        VoteSignupDialog(
            onDismissRequest = { showVoteSignupDialog = false },
            onSignUpClick = {
                showVoteSignupDialog = false
            }
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF0C091A
)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
