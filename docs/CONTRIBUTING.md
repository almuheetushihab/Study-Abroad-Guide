# Contributing Guidelines

Thank you for contributing to the Study Abroad Guide Android application! Please follow these guidelines to keep the codebase clean, consistent, and compliant with project requirements.

---

## 🌿 Branch Naming Strategy
Name your feature or bugfix branches using the standard requirement ID prefix:
- `feature/<FR-ID>-short-description` (e.g. `feature/FR-NTF-003-notification-settings`)
- `fix/<FR-ID>-short-description` (e.g. `fix/FR-SCH-002-status-calculation`)

---

## 📝 Commit Conventions
Follow Conventional Commits style:
- `feat:` New feature implementation (e.g. `feat: add notification settings toggles and permission handling (FR-NTF-003, FR-NTF-004)`)
- `fix:` Bug fixes (e.g. `fix: correct CGPA validation scale bounds`)
- `chore:` Maintenance, dependency updates, or configuration changes (e.g. `chore: update Gradle dependencies`)
- `docs:` Documentation updates (e.g. `docs: update ARCHITECTURE.md package tree`)

---

## 🗄️ Database Migrations
From the first public release:
- Every Room schema change needs an explicit `Migration` implementation.
- Updated schema JSON files must be committed under `app/schemas/`.
- Schema migrations must be accompanied by an automated migration test verifying data preservation.

---

## 🔍 Static Code Analysis & Detekt Note
- **Android Lint & Spotless:** Active and checked on every build.
- **Detekt:** `detekt 1.23.8` is currently incompatible with AGP 9 built-in Kotlin (requires deprecated `android.newDsl=false` and `android.builtInKotlin=false` flags). Detekt has been temporarily removed from the Gradle build in Stage 1 of the toolchain upgrade and will be re-added once a stable Detekt release with native AGP 9 support is published.

---

## ✅ Definition of Done (DoD)
Before opening or merging a Pull Request, ensure all the following criteria are met:

1. **Requirement IDs Included:** PR title and description explicitly mention all implemented functional requirement IDs (e.g., `FR-NTF-003`, `FR-NTF-004`, `FR-PRO-003`, `FR-PRO-004`, `FR-TRU-003`, `FR-TRU-005`, `FR-AUTH-004`, `NFR-MNT-002`).
2. **5 UI States Supported:** Screen UI explicitly handles all 5 states: Loading, Content, Empty, Error, and Offline/Stale.
3. **No Hard-coded Strings:** All user-facing strings are defined in both English (`res/values/strings.xml`) and Bangla (`res/values-bn/strings.xml`).
4. **Compose Previews:** Every screen and component composable includes `@ThemePreviews` with sample data.
5. **Unit Tests for Domain Rules:** Pure Kotlin domain use cases and repository logic have 100% passing unit tests under `app/src/test/`.
6. **Green CI Pipeline:** The GitHub Actions CI pipeline passes `./gradlew lint spotlessCheck testDebugUnitTest assembleDebug assembleRelease` without any warnings or errors.
