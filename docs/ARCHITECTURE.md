# Study Abroad Guide Architecture

This document outlines the architectural conventions and guidelines for the Study Abroad Guide Android application.

## Overview
The application follows modern Android development practices using MVVM + light Clean Architecture, package-by-feature, and unidirectional data flow (`StateFlow<XxxUiState>` + Events).

```
app/src/main/java/com/shihab/studyabroad/
 ├── core/
 │    ├── common/          # AppResult, DateFormatters, CurrencyFormatter, DispatcherProvider, LanguageManager, UrlOpener
 │    ├── designsystem/    # Theme (Color, Shape, Spacing, Type) and shared UI components (AppTopBar, InfoCard, ReportWrongInfoDialog, StatusChip, etc.)
 │    └── ui/              # UiState, UiText, ThemePreviews
 ├── domain/
 │    ├── model/           # Pure Kotlin data models (Country, University, Scholarship, UserProfile, Report, Track, etc.)
 │    ├── repository/      # Repository interfaces (AuthRepository, CountryRepository, ScholarshipRepository, UserPreferencesRepository, ReportRepository, etc.)
 │    └── usecase/         # Domain use cases (CompareRequirementUseCase, DeleteAccountUseCase, GetScholarshipStatusUseCase, IsRecordStaleUseCase, SubmitReportUseCase, etc.)
 ├── data/
 │    ├── local/
 │    │    ├── dao/        # Room DAOs (CountryDao, ScholarshipDao, SavedItemDao, ChecklistProgressDao, ReportDao)
 │    │    ├── datastore/  # DataStore preferences & UserPreferencesRepositoryImpl
 │    │    └── entity/     # Room Entities (CountryEntity, ScholarshipEntity, SavedItemEntity, ChecklistProgressEntity, ReportEntity)
 │    ├── mapper/         # Mappers converting between Entities and Domain models
 │    └── repository/     # Repository implementations (SavedRepositoryImpl, ChecklistRepositoryImpl, ReportRepositoryImpl, Fake repositories)
 ├── di/                  # Hilt modules (ClockModule, DatabaseModule, DispatcherModule, RepositoryModule, UseCaseModule)
 ├── navigation/          # Compose Navigation (AppNavHost, BottomBarItem, Route)
 └── feature/             # Feature UI modules following MVVM + 5-state UI pattern:
      ├── auth/           # LoginRoute, LoginScreen, LoginViewModel, LoginUiState
      ├── country/        # CountryList, CountryDetail, CountryComparison
      ├── debug/          # ThemePreviewScreen
      ├── documents/      # DocumentsScreen, DocumentsViewModel, DocumentsUiState
      ├── explore/        # ExploreScreen
      ├── home/           # HomeScreen, HomeViewModel, HomeUiState
      ├── notifications/  # NotificationsRoute, NotificationsScreen, NotificationsViewModel, NotificationsUiState
      ├── onboarding/     # OnboardingRoute, OnboardingScreen, OnboardingViewModel, OnboardingUiState
      ├── profile/        # ProfileRoute, ProfileScreen, ProfileViewModel, ProfileUiState, DisclaimerScreen
      ├── profilesetup/   # ProfileSetupRoute, ProfileSetupScreen, ProfileSetupViewModel, ProfileSetupUiState
      ├── saved/          # SavedRoute, SavedScreen, SavedViewModel, SavedUiState
      ├── scholarship/    # ScholarshipList, ScholarshipDetail, ScholarshipViewModel, ScholarshipUiState
      ├── splash/         # SplashRoute, SplashScreen, SplashViewModel, SplashUiState
      ├── track/          # TrackScreen
      ├── trackselect/    # TrackSelectRoute, TrackSelectScreen, TrackSelectViewModel, TrackSelectUiState
      └── university/     # UniversityList, UniversityDetail, UniversityViewModel, UniversityUiState
```

## Naming Conventions
- **XxxRoute**: Stateful Composable function that connects Hilt ViewModel and collects UiState.
- **XxxScreen**: Stateless Composable function taking UiState and event callbacks.
- **XxxViewModel**: Jetpack ViewModel producing `StateFlow<XxxUiState>`.
- **XxxUiState**: Sealed interface or data class representing screen states.
- **XxxUseCase**: Single-responsibility domain logic executor (e.g. `SubmitReportUseCase`, `DeleteAccountUseCase`).
- **XxxRepository**: Repository interface in domain, implemented in data layer (`ReportRepositoryImpl` / `UserPreferencesRepositoryImpl`).

## Component Guidelines
- Screen-specific UI Composables live in that feature's `components/` package (e.g. `feature/home/components/`).
- Shared reusable UI Composables live in `core/designsystem/component/` (e.g. `AppLoadingWheel`, `AppErrorContent`, `AppEmptyContent`, `ReportWrongInfoDialog`).

## 5-State Screen Pattern
Every feature screen explicitly supports 5 UI states:
1. **Loading**: Progress wheel while fetching initial data.
2. **Content**: Valid data payload presented to the user.
3. **Empty**: Data list is empty with appropriate user guidance.
4. **Error**: Clear error message with a retry option.
5. **Offline/Stale**: Cached offline data displayed with an offline banner notice.

## Data Persistence & Platform Independence
- Local caching is managed by Room and DataStore.
- Domain logic is kept in pure Kotlin to ensure future React Native or multiplatform portability.
- Status values (e.g. Scholarship status: OPEN, OPENING_SOON, CLOSED) are computed dynamically and NEVER persisted in database or model layer.
