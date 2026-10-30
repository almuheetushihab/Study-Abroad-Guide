# Study Abroad Guide App (Android)

A trusted mobile application assisting Bangladeshi students (Freshers, Graduates, Professionals, Researchers) in finding verified overseas scholarships, low-cost countries, top universities, visa guides, and document preparation checklists.

---

## 📌 Purpose
The Study Abroad Guide App serves as a single, verified reference point to answer: **where to go, what to study, how much it costs, which scholarships to apply for, which documents to prepare, and what to do next** — backed by 100% verified data, clear sources, and personal track-based planning.

---

## 🛠 Requirements to Run
* **JDK:** Java 17 or higher
* **Android Studio:** Ladybug (2024.2) or newer
* **Android SDK:** Compile SDK 37, Target SDK 35, Min SDK 24 (Android 7.0+)
* **Gradle:** Version Catalog (`gradle/libs.versions.toml`) - AGP 9.4.1, Kotlin 2.3.21

---

## 🚀 Build & Test Commands

```bash
# Check code formatting with Spotless
./gradlew spotlessCheck

# Automatically format code using Spotless
./gradlew spotlessApply

# Run static analysis with Android Lint
./gradlew lint

# Run all unit tests
./gradlew testDebugUnitTest

# Assemble Debug APK
./gradlew assembleDebug

# Assemble Release APK
./gradlew assembleRelease
```

---

## 📦 Package Overview

```
app/src/main/java/com/shihab/studyabroad/
 ├── core/              # Shared design system components, themes, formatting, & common utilities
 ├── domain/            # Pure Kotlin domain models, repository interfaces, and business use cases
 ├── data/              # Room database, DAOs, DataStore preferences, & repository implementations
 ├── di/                # Hilt dependency injection modules (DatabaseModule, RepositoryModule, etc.)
 ├── navigation/        # Compose Navigation host, routes, and bottom navigation bar
 └── feature/           # Feature UI modules following MVVM + 5-state UI pattern:
      ├── auth/         # Phone OTP & Google sign-in (FR-AUTH-001, FR-AUTH-002)
      ├── country/      # Country guide, tier filters, & side-by-side comparison (FR-CTY-001..003, FR-CMP-001)
      ├── documents/    # Track-wise document checklist & BD attestation steps (FR-DOC-001..003)
      ├── home/         # Dashboard with Quick Summary & upcoming deadlines
      ├── notifications/# Notification settings & POST_NOTIFICATIONS permissions (FR-NTF-003, FR-NTF-004)
      ├── onboarding/   # App introduction slides & scam safety notices (FR-TRU-004)
      ├── profile/      # Me tab, profile summary, language switch, disclaimer, & delete account (FR-PRO-003, FR-PRO-004, FR-TRU-005, FR-AUTH-004)
      ├── profilesetup/ # Multi-step profile setup & edit wizard (FR-PRO-001, FR-PRO-002)
      ├── saved/        # Saved items manager (FR-SAV-001)
      ├── scholarship/  # Scholarship search, detail, deadline reminders, & status (FR-SCH-001..006, FR-NTF-001)
      └── university/   # University search, programs, & requirement vs profile meter (FR-UNI-001..003)
```

---

## 🧭 How to Add a New Feature in 6 Steps

1. **Define Domain Model & Requirement IDs:** Add data models under `domain/model/` and identify target SRS requirement IDs (`FR-*` / `NFR-*`).
2. **Define Repository Interface:** Declare repository functions in `domain/repository/` and implement them under `data/repository/` (binding them in `di/RepositoryModule.kt`).
3. **Implement Domain Use Cases:** Write pure Kotlin use cases in `domain/usecase/` executing specific business logic with unit tests in `app/src/test/`.
4. **Create UiState & ViewModel:** Build `XxxUiState` (sealed or data class supporting 5 UI states: Loading, Content, Empty, Error, Offline) and `XxxViewModel` exposing `StateFlow<XxxUiState>`.
5. **Build Stateless Screen & Stateful Route:** Create `XxxScreen` (stateless UI using `core/designsystem/`) and `XxxRoute` (stateful wrapper collecting ViewModel state), providing string resources in both `res/values/strings.xml` and `res/values-bn/strings.xml`.
6. **Register Route in NavHost:** Add the `@Serializable` route to `navigation/Route.kt` and wire destination in `navigation/AppNavHost.kt`.
