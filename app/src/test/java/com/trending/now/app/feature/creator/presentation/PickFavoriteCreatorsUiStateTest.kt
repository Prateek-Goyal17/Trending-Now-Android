package com.trending.now.app.feature.creator.presentation

import com.trending.now.app.feature.auth.domain.model.AuthProfile
import com.trending.now.app.feature.auth.domain.model.AuthState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PickFavoriteCreatorsUiStateTest {

    private val mockProfile = AuthProfile(
        id = "id",
        firebaseUid = "uid",
        username = null,
        firstName = null,
        lastName = null,
        email = null,
        profileImage = null,
        favoriteCreatorsCount = 0,
        bookmarkPostsCount = 0,
        likedNewsCount = 0,
    )

    @Test
    fun continueRequiresNewUserAndAtLeastOneSelection() {
        assertFalse(
            PickFavoriteCreatorsUiState(
                authState = AuthState.NewUser(mockProfile),
            ).canContinue,
        )
        assertFalse(
            PickFavoriteCreatorsUiState(
                authState = AuthState.Guest,
                selectedCreatorIds = setOf("creator-1"),
            ).canContinue,
        )
        assertTrue(
            PickFavoriteCreatorsUiState(
                authState = AuthState.NewUser(mockProfile),
                selectedCreatorIds = setOf("creator-1"),
            ).canContinue,
        )
    }

    @Test
    fun selectionLocksAfterSubmissionStarts() {
        assertFalse(
            PickFavoriteCreatorsUiState(
                authState = AuthState.NewUser(mockProfile),
                selectedCreatorIds = setOf("creator-1"),
                submissionStarted = true,
            ).canChangeSelection,
        )
    }
}
