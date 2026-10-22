# Project rules for AI agents

Project: Study Abroad Guide (Android, Kotlin, Jetpack Compose, MVVM).

## Source of truth
- Requirements: docs/HANDBOOK.md (SRS in Part 2: FR-* and NFR-* IDs)
- Architecture and naming: docs/ARCHITECTURE.md
- Screen sketches: docs/UI_SKETCHES.md
- If a request conflicts with the Handbook, stop and ask. Do not silently change requirements.

## Architecture rules
- MVVM + light Clean Architecture, package-by-feature.
- Dependency rule: feature -> domain <- data. domain is pure Kotlin (no Android, Room, Firebase imports).
- Naming: XxxRoute (stateful), XxxScreen (stateless), XxxViewModel, XxxUiState (sealed), XxxUseCase.
- Screen-specific composables live in that screen's components/ package; shared ones in core/designsystem/component.
- Every screen has 5 states: Loading, Content, Empty, Error, Offline/Stale.

## Quality rules
- Version Catalog only (gradle/libs.versions.toml). Stable versions only.
- No hard-coded user-facing strings: use res/values (en) and res/values-bn (bn).
- Scholarship status is computed from dates, never stored.
- Missing eligibility data shows "Check the official site" or Unknown, never a guess.
- Accessibility: 48dp touch targets, content descriptions, works at 200% font scale.
- Sample data must stay marked SAMPLE_DATA_NOT_VERIFIED.
- After every change run: ./gradlew spotlessCheck testDebugUnitTest assembleDebug
- Never commit: local.properties, keystores, google-services.json (prod), service account keys.

## Workflow
- Mention the requirement IDs (FR-/NFR-) that a change implements.
- Commit style: feat:, fix:, chore:, docs:
