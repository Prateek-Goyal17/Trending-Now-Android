package com.trending.now.app.feature.creator.presentation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PickFavoriteCreatorsUiStateTest {
    @Test
    fun continueRequiresNewUserAndAtLeastOneSelection() {
        assertFalse(
            PickFavoriteCreatorsUiState(
                access = FavoriteCreatorPickerAccess.NewUser,
            ).canContinue,
        )
        assertFalse(
            PickFavoriteCreatorsUiState(
                access = FavoriteCreatorPickerAccess.GuestLocked,
                selectedCreatorIds = setOf("creator-1"),
            ).canContinue,
        )
        assertTrue(
            PickFavoriteCreatorsUiState(
                access = FavoriteCreatorPickerAccess.NewUser,
                selectedCreatorIds = setOf("creator-1"),
            ).canContinue,
        )
    }

    @Test
    fun selectionLocksAfterSubmissionStarts() {
        assertFalse(
            PickFavoriteCreatorsUiState(
                access = FavoriteCreatorPickerAccess.NewUser,
                selectedCreatorIds = setOf("creator-1"),
                submissionStarted = true,
            ).canChangeSelection,
        )
    }
}
