# Implement "You might like" section in HomeScreen

Mirror the "You might like" (Creator Suggestions) section from `CreatorScreen` into `HomeScreen` with identical styling, animations, and components.

## Proposed Changes

### [Home Feature]

#### [MODIFY] [HomeScreen.kt](file:///C:/Users/admin/StudioProjects/Trending-Now-Android/app/src/main/java/com/trending/now/app/feature/home/presentation/HomeScreen.kt)
- Add imports for `CreatorSuggestionCard`, `CreatorSuggestionCardUiModel`, and `CreatorSuggestionBadgeType`.
- Define mock data for `CreatorSuggestionCardUiModel` to match the "Akash Gupta" example.
- Insert a "You might like" title and the `CreatorSuggestionCard` component into the `LazyColumn`.

## Verification Plan

### Manual Verification
- Deploy the app and navigate to the Home screen.
- Verify that the "You might like" section appears after the "Creator Kings" carousel.
- Ensure the fonts, colors, and auto-paging animations match the Creator screen.
