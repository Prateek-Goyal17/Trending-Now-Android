# Implement Report a Problem UI

Implement the UI for the `ReportProblemScreen.kt` based on the provided screenshot and existing design patterns. The screen will include a custom header, a multiline text field for issue description, an attachment section, and a submit button.

## Proposed Changes

### [Me Feature]

#### [MODIFY] [ReportProblemScreen.kt](file:///C:/Users/admin/StudioProjects/Trending-Now-Android/app/src/main/java/com/trending/now/app/feature/me/presentation/ReportProblemScreen.kt)
- Add state for issue description (`mutableStateOf("")`).
- Implement the header with a back button and a centered gradient title "Report a problem".
- Add a multiline input field with a 150-character limit and character counter.
- Add an "Attach Screenshot" placeholder box.
- Add an information row with an icon and text.
- Add a "Send" button using `GradientAccentButton` component.
- Update the function signature to accept `onBack` callback.

#### [MODIFY] [TrendingNowApp.kt](file:///C:/Users/admin/StudioProjects/Trending-Now-Android/app/src/main/java/com/trending/now/app/core/navigation/TrendingNowApp.kt)
- Pass `navController.popBackStack()` to the `ReportProblemScreen` composable.

## Verification Plan

### Automated Tests
- Not applicable for UI-only changes.

### Manual Verification
- Deploy the app to a device or emulator.
- Navigate to the "Me" screen and click on "Report a Problem".
- Verify the header matches the style of `TrendingCreators.kt`.
- Verify the character count updates as you type in the text field.
- Verify the "Send" button is correctly styled and positioned.
- Verify the back button returns to the "Me" screen.
