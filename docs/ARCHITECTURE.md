# Study Abroad Guide Architecture

This document outlines the architectural conventions and guidelines for the Study Abroad Guide Android application.

## Overview
The application follows modern Android development practices using MVVM + light Clean Architecture, package-by-feature, and unidirectional data flow (StateFlow<UiState> + Events).

```
app/
 ├── core/              # Shared design system, UI components, and common utilities
 ├── domain/            # Pure Kotlin domain models, repository interfaces, and use cases (no Android framework dependencies)
 ├── data/              # Local (Room, DataStore), Remote, and Fake repository implementations
 ├── di/                # Hilt dependency injection modules
 ├── navigation/        # Compose Navigation routes and graph
 └── feature/           # Package-by-feature UI modules (e.g. home, explore, country, scholarship)
```

## Naming Conventions
- **XxxRoute**: Stateful Composable function that connects Hilt ViewModel and collects UiState.
- **XxxScreen**: Stateless Composable function taking UiState and event callbacks.
- **XxxViewModel**: Jetpack ViewModel producing `StateFlow<XxxUiState>`.
- **XxxUiState**: Sealed interface representing screen states.
- **XxxUseCase**: Single-responsibility domain logic executor (e.g. `GetScholarshipStatusUseCase`).
- **XxxRepository**: Repository interface in domain, implemented in data layer (`FakeXxxRepository` / `RoomXxxRepository`).

## Component Guidelines
- Screen-specific UI Composables live in that feature's `components/` package (e.g. `feature/home/components/`).
- Shared reusable UI Composables live in `core/designsystem/component/` (e.g. `AppLoadingWheel`, `AppErrorContent`, `AppEmptyContent`).

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
