# Study Abroad Guide App - Project Handbook

Oct 22, 2026 · @MD. AL - MUHEETU

This handbook is the single reference to plan, build, test and ship the Study Abroad Guide App: Android first (Kotlin + Jetpack Compose), React Native later. It holds the product requirements (PRD), the software requirements (SRS), the technical and UX design, the data plan, the test and release plan, and the delivery plan. Version 1.0 (draft).

## 0. How to use this handbook

| Part | Answers | Read it if you are |
| --- | --- | --- |
| 1. Product (PRD) | Why this app, for whom, what success looks like | Founder, mentor, partner |
| 2. SRS | Exactly what the app must do, with testable requirement IDs | Developer, tester |
| 3. Technical design | Architecture, data model, sync, security | Developer |
| 4. UX and screens | Screen list, navigation, design rules | Designer, developer |
| 5. Data and content operations | Where data comes from and how it stays correct | Editor, admin |
| 6. Quality, test and release | How we keep the app stable and ship safely | Developer, tester |
| 7. Delivery plan | Phases, sprints, first 14 days | Everyone |
| 8. Appendices | Country table, scholarship calendar, document checklist, links | Everyone |

**Reading order for a new person:** Part 1, then Part 2 sections 2.1 to 2.3, then Part 7. Developers continue with Parts 3 to 6.

**Change rule:** requirements change only through a comment on the requirement ID. Accepted changes get a new version number in the change log (end of Part 2).

**Companion doc:** 10 screen sketches (wireframes) are in [UI Sketches](UI_SKETCHES.md).

### Glossary

| Term | Meaning |
| --- | --- |
| Track | User type: Fresher (HSC), Graduate (BSc), Professional (2+ years job), Researcher (MSc) |
| Scholarship status | Open, Opening soon or Closed; calculated from the open date and deadline |
| Verified date | The date an editor last checked a record against its official source |
| Stale | A record whose verified date is older than its limit (90 days for deadlines and rules) |
| Plan A/B/C | Three suggested paths: best case, realistic, fallback |
| MVP | Minimum Viable Product: the first public release (Phase 1) |
| MoSCoW | Priority: Must, Should, Could, Won't (this release) |

---

# Part 1. Product (PRD)

## 1.1 Problem

A Bangladeshi student who wants to study abroad must collect scholarship, university, cost, visa and document information from many websites, groups and agents. The information is scattered, often outdated, and sometimes false. Students do not know which country fits their profile, whether they are eligible, what it will cost, or what to prepare and when. Fake agents and scams add risk.

## 1.2 Vision and promise

One trusted app that answers: **where to go, what to study, how much it costs, which scholarship to apply for, which documents to prepare, and what to do next**, using verified data and a personal plan.

## 1.3 Target users

| Track | Who | Main need |
| --- | --- | --- |
| Fresher | HSC pass | Bachelor scholarships, low-cost countries |
| Graduate | BSc running or complete | Masters scholarships, subject choice |
| Professional | 2+ years job experience | Experience-based scholarships, MBA, job vs study |
| Researcher | MSc complete | PhD, funded research |
| Guardian | Parent or sponsor | Cost, safety, visa, scam awareness |

Example personas (fictional, for design discussions):

| Persona | Profile | Question they bring |
| --- | --- | --- |
| Rahim | BSc CSE, CGPA 3.4, IELTS not taken | Which scholarships can I apply for next year? |
| Nusrat | HSC pass, family budget limited | Which country is cheapest and safe for a girl? |
| Tanvir | 4 years software job | Should I quit my job to study? Which scholarship values experience? |

## 1.4 Goals and non-goals

**Goals**

1. Give verified, dated, source-linked information for scholarships, countries and universities.
2. Give each user a personal plan (eligibility, Plan A/B/C, roadmap, deadlines).
3. Work on low-end phones and slow networks, with Bangla and English.
4. Keep core features free.

**Non-goals (first year)**

- Handling applications or payments on behalf of users.
- Giving legal or immigration advice.
- Guaranteeing admission or scholarship results.
- Acting as an agent or taking commission for placements.

## 1.5 Success metrics

These are starting targets. Review them after the closed beta.

| Metric | Target | How measured |
| --- | --- | --- |
| Profile completion | 60% of new users | Analytics event profile\_completed |
| Day-7 retention | 15% or more | Firebase Analytics |
| Reminder set rate | 25% of users who open a scholarship detail | reminder\_set / scholarship\_viewed |
| Crash-free sessions | 99.5% or more | Crashlytics |
| Data freshness | 0 published deadlines older than 90 days unverified | Admin report |
| Wrong-info reports closed | Within 7 days | Admin queue |
| Time to first useful result | Under 3 minutes from first launch | Funnel analytics |

## 1.6 Release scope

| Release | In scope | Out of scope |
| --- | --- | --- |
| MVP (Phase 1) | Login, Track and profile, Country Card, Comparison, Scholarship finder, University explorer, Document checklist, Deadline reminders, Trust labels, Saved items | Community, AI, payments, document upload |
| Release 2 (Phase 2) | Eligibility checker, Smart Match, Plan A/B/C, Cost and loan planner, Roadmap, Tracker, Calendar | Community, AI |
| Release 3 (Phase 3) | Visa and arrival, work and family guide, PR path, career guide, exam prep, Q&A, mentors | Payments |
| Release 4 (Phase 4) | AI assistant, premium, partnerships, React Native / iOS | - |

## 1.7 Assumptions and dependencies

- One developer works about 20 hours per week; timelines scale with real capacity.
- Content (scholarships, rules, costs) is verified by hand from official sources; this is the main ongoing cost.
- Firebase is the MVP backend. Cloud Functions need the pay-as-you-go plan, so set budget alerts from day one.
- Phone OTP sign-in sends SMS, which costs money beyond the free quota and can be unreliable; Google sign-in is the fallback.
- Google Play rules for new developer accounts (testing requirements, data safety form, account deletion) must be checked before launch.

## 1.8 Product principles

1. **Verified data only:** every record shows its source link and verified date.
2. **Offline-first:** saved data opens without internet.
3. **Bangla and English** from the first release.
4. **Core free:** scholarships, countries and checklists stay free.
5. **No promises:** estimates are labelled as guides, never guarantees.

---

# Part 2. Software Requirements Specification (SRS)

## 2.1 Purpose and scope

This SRS defines what the Study Abroad Guide App (Android, version 1.x) must do. It covers the Android client, the backend services it depends on, and the admin panel used to manage content. It is written so each requirement can be tested.

## 2.2 System context

```
+------------------+     HTTPS      +--------------------------------------+
| Student          | <------------> | Firebase                             |
| (Android app)    |                |  Auth | Firestore | Remote Config    |
+------------------+                |  FCM  | Crashlytics | Analytics      |
                                    +-------------------+------------------+
+------------------+                                    |
| Admin / Editor   | ---- web panel ------------------->|  content + rules
| (web panel)      |                                    |
+------------------+                +-------------------v------------------+
                                    | Cloud Functions                      |
+------------------+                |  validation, scoring, sync jobs      |
| Public sources   | -------------> +--------------------------------------+
| (exchange rate,  |
|  university list)|
+------------------+
```

## 2.3 User classes

| Class | Description | Access |
| --- | --- | --- |
| Student | Any user with an account | Read content, manage own profile, saved items, tracker |
| Guest | Not signed in (optional, Release 1.1) | Read public content only |
| Editor | Enters and updates content | Create and edit records in the admin panel |
| Verifier | Checks records against sources | Mark records verified and publish |
| Admin | Manages users, reports, settings | Full access |

## 2.4 Operating environment

- Android 7.0 (API 24) and above (proposal; confirm with install-base data).
- Phones with 2 to 3 GB RAM, small screens, and intermittent 2G/3G/4G networks.
- Languages: Bangla and English. Bangla text must render correctly on all supported versions.
- Backend: Firebase (Auth, Firestore, FCM, Remote Config, Crashlytics, Analytics) and Cloud Functions.

## 2.5 Constraints

- Content accuracy depends on manual verification against official sources.
- The app must follow Google Play policies, including the data safety declaration and an in-app way to delete the account and data.
- Ranking data from QS or THE may not be copied without a licence; use links and attribution only.
- The app gives information, not legal or immigration advice.

## 2.6 Document conventions

| Item | Rule |
| --- | --- |
| ID format | FR-MODULE-number for functional, NFR-AREA-number for non-functional |
| Wording | MUST = required, SHOULD = recommended, MAY = optional |
| Priority | M = Must, S = Should, C = Could (MoSCoW) |
| Verification | T = test, D = demo, I = inspection, A = analysis |
| Release | R1 = MVP, R2, R3, R4 as in section 1.6 |

## 2.7 Functional requirements

Columns: Pri = priority (M/S/C), Rel = release, Ver = verification method.

### Account (AUTH)

| ID | Requirement and acceptance | Pri | Rel | Ver |
| --- | --- | --- | --- | --- |
| FR-AUTH-001 | The app MUST let a user sign in with phone OTP. A valid code signs the user in; an invalid code shows an error and never crashes. | M | R1 | T |
| FR-AUTH-002 | The app MUST let a user sign in with Google as an alternative to OTP. | M | R1 | T |
| FR-AUTH-003 | The session MUST persist across app restarts until the user signs out. | M | R1 | T |
| FR-AUTH-004 | The user MUST be able to delete the account and personal data from inside the app. | M | R1 | T |
| FR-AUTH-005 | The user MUST be able to sign out from the Me tab; signing out ends the session and returns to the Login screen. | M | R1 | T |

### Profile (PRO)

| ID | Requirement and acceptance | Pri | Rel | Ver |
| --- | --- | --- | --- | --- |
| FR-PRO-001 | The user MUST choose a Track (Fresher, Graduate, Professional, Researcher) during onboarding. | M | R1 | T |
| FR-PRO-002 | The profile MUST store CGPA with its scale, subject, test scores (optional), experience years, age, budget range, goal and target countries. Invalid values (for example IELTS above 9) are rejected with a message. | M | R1 | T |
| FR-PRO-003 | The user MUST be able to edit the profile at any time; dependent results update on the next view. | M | R1 | T |
| FR-PRO-004 | The user MUST be able to switch Bangla and English; the change applies without restarting the app. | M | R1 | T |

### Country guide and comparison (CTY, CMP)

| ID | Requirement and acceptance | Pri | Rel | Ver |
| --- | --- | --- | --- | --- |
| FR-CTY-001 | The app MUST list countries and filter by Tier (Scholarship, PR/Work, All). | M | R1 | T |
| FR-CTY-002 | Each Country Card MUST use one standard layout: key stats, scholarship box, top universities, work/post-study/PR, tip or warning, source and verified date. | M | R1 | I |
| FR-CTY-003 | A country with a risk advisory MUST show a visible warning banner. | M | R1 | T |
| FR-CMP-001 | The Master Comparison table MUST sort by any column. | M | R1 | T |
| FR-CMP-002 | The user MUST be able to compare up to 3 countries side by side. | S | R1 | T |
| FR-CMP-003 | Quick Summary lists (Top 3 funded, low cost, PR friendly, professional) MUST be computed from stored data, not hard-coded. | S | R1 | T |

### Universities (UNI)

| ID | Requirement and acceptance | Pri | Rel | Ver |
| --- | --- | --- | --- | --- |
| FR-UNI-001 | The app MUST search universities by name and filter by country, level, subject, scholarship availability and language. | M | R1 | T |
| FR-UNI-002 | University detail MUST show programs, requirements, deadline, fee and an official link. | M | R1 | T |
| FR-UNI-003 | The requirement-versus-profile meter MUST show OK, Low or Unknown. When a requirement is missing it MUST show Unknown, never a guess. | M | R1 | T |
| FR-UNI-004 | The user MUST be able to shortlist universities and compare up to 3. | S | R1 | T |

### Scholarships (SCH)

| ID | Requirement and acceptance | Pri | Rel | Ver |
| --- | --- | --- | --- | --- |
| FR-SCH-001 | The app MUST list scholarships with filters: country, level, funding type, deadline range, status. | M | R1 | T |
| FR-SCH-002 | Status (Open, Opening soon, Closed) MUST be calculated from open date, deadline and the current date, not stored. | M | R1 | T |
| FR-SCH-003 | Detail MUST show eligibility, benefits, documents, deadline, official apply link, source and verified date. | M | R1 | I |
| FR-SCH-004 | A closed scholarship MUST show the next expected cycle when known, labelled Expected. | S | R1 | T |
| FR-SCH-005 | When an eligibility field is empty the app MUST show Check the official site, not a guess. | M | R1 | T |
| FR-SCH-006 | Deadlines MUST show the date and a note to confirm the time zone on the official site. | S | R1 | I |

### Reminders and notifications (NTF)

| ID | Requirement and acceptance | Pri | Rel | Ver |
| --- | --- | --- | --- | --- |
| FR-NTF-001 | The user MUST be able to set a deadline reminder at 30, 14, 7 or 1 day before. | M | R1 | T |
| FR-NTF-002 | Reminders MUST fire when the app is closed and after a phone restart. | M | R1 | T |
| FR-NTF-003 | Notification permission (Android 13+) MUST be requested in context; the app MUST work if it is denied. | M | R1 | T |
| FR-NTF-004 | The user MUST be able to turn each notification type on or off. | S | R1 | T |

### Documents (DOC) and saved items (SAV)

| ID | Requirement and acceptance | Pri | Rel | Ver |
| --- | --- | --- | --- | --- |
| FR-DOC-001 | The app MUST show a document checklist by Track and country; ticks persist offline. | M | R1 | T |
| FR-DOC-002 | The checklist MUST show a progress percentage. | S | R1 | T |
| FR-DOC-003 | The app MUST show the Bangladesh attestation steps with official links. | M | R1 | I |
| FR-DOC-004 | The user MAY export the checklist as PDF. | C | R2 | D |
| FR-SAV-001 | The user MUST be able to save and unsave countries, universities and scholarships; the Saved screen works offline. | M | R1 | T |

### Trust and safety (TRU)

| ID | Requirement and acceptance | Pri | Rel | Ver |
| --- | --- | --- | --- | --- |
| FR-TRU-001 | Every content record MUST show its source link and verified date. | M | R1 | I |
| FR-TRU-002 | A record older than its stale limit MUST show a May be outdated badge. | M | R1 | T |
| FR-TRU-003 | The user MUST be able to report wrong information on any record; the report is stored for admin review and the user sees a confirmation. | M | R1 | T |
| FR-TRU-004 | A scam warning MUST appear in onboarding and on scholarship detail: official applications are usually free; confirm on the official site. | M | R1 | I |
| FR-TRU-005 | A disclaimer (information only, not legal advice) MUST be reachable from the Me tab. | M | R1 | I |
| FR-TRU-006 | Any sponsored content MUST be labelled Sponsored. | M | R4 | I |

### Data sync (SYN)

| ID | Requirement and acceptance | Pri | Rel | Ver |
| --- | --- | --- | --- | --- |
| FR-SYN-001 | On first launch the app MUST download the content catalog and store it locally. | M | R1 | T |
| FR-SYN-002 | The app MUST sync changes on launch and at least every 24 hours while connected. | M | R1 | T |
| FR-SYN-003 | Without a network, screens MUST show cached data with a Last updated date. | M | R1 | T |
| FR-SYN-004 | A sync failure MUST NOT block the UI or crash the app. | M | R1 | T |

### Smart features (Release 2)

| ID | Requirement and acceptance | Pri | Rel | Ver |
| --- | --- | --- | --- | --- |
| FR-ELG-001 | The eligibility checker MUST take age, experience, CGPA, test score and level, and return Eligible, Almost, Not eligible or Unknown per scholarship, naming the failed criterion. | M | R2 | T |
| FR-ELG-002 | A scholarship with a missing required field MUST be Unknown, never Eligible. | M | R2 | T |
| FR-PLN-001 | The app MUST generate Plan A, B and C from the profile; each plan shows the reason, deadline and source. | M | R2 | T |
| FR-PLN-002 | Weight sliders MUST re-rank countries within 1 second. | S | R2 | T |
| FR-PLN-003 | Plan rules and weights MUST be server-configurable so they update without an app release. | M | R2 | T |
| FR-PLN-004 | The plan screen MUST show a disclaimer: a guide, not a guarantee. | M | R2 | I |
| FR-CST-001 | The cost calculator MUST show monthly and yearly cost in BDT from stored ranges and show the exchange-rate date. | M | R2 | T |
| FR-CST-002 | The app SHOULD include a savings-versus-loan planner. | S | R2 | T |
| FR-RDM-001 | The roadmap MUST be built per Track and intake date with tickable tasks. | M | R2 | T |
| FR-CAL-001 | A calendar MUST show deadlines for saved and tracked items. | S | R2 | T |
| FR-TRK-001 | The tracker MUST support stages Saved, Preparing, Applied, Result with document progress. | M | R2 | T |

### Release 3 and 4 (outline)

| ID | Requirement | Rel |
| --- | --- | --- |
| FR-VIS | Visa guide, fees, embassy or VFS links, arrival checklist, airfare reference | R3 |
| FR-WRK | Work rules, earning guide, tax and visa warnings | R3 |
| FR-FAM | Family shift guide (spouse, children) | R3 |
| FR-PR | PR and citizenship path by country | R3 |
| FR-CAR | Career and subject guide, skills and certificates | R3 |
| FR-EXM | Exam prep (IELTS, GRE, language) roadmap | R3 |
| FR-COM | Q&A community with moderation and reporting | R3 |
| FR-MEN | Verified mentors and chat | R3 |
| FR-AI | AI assistant answering from verified content only | R4 |
| FR-PRM | Premium plan and payments | R4 |

### Admin and content (ADM)

| ID | Requirement and acceptance | Pri | Rel | Ver |
| --- | --- | --- | --- | --- |
| FR-ADM-001 | The admin panel MUST let an Editor create and edit records; sourceUrl and verifiedOn are required. | M | R1 | T |
| FR-ADM-002 | A record MUST be published only after a Verifier approves it. | M | R1 | T |
| FR-ADM-003 | The panel MUST list stale records and records due for re-verification. | M | R1 | T |
| FR-ADM-004 | The panel MUST show a queue of wrong-info reports with status. | M | R1 | T |
| FR-ADM-005 | Every change MUST be logged with user, time and old and new value. | M | R1 | I |
| FR-ADM-006 | Access MUST be role based (Editor, Verifier, Admin) and enforced on the server. | M | R1 | T |

## 2.8 Non-functional requirements

Reference device for performance targets: a mid-range Android phone with 3 GB RAM. Targets are starting values; confirm them in the beta.

| ID | Requirement | Target | Ver |
| --- | --- | --- | --- |
| NFR-PERF-001 | Cold start to first usable screen | 2.5 s or less | T |
| NFR-PERF-002 | Cached screen render | 1 s or less | T |
| NFR-PERF-003 | Install size (AAB, per device) | 30 MB or less | I |
| NFR-PERF-004 | First catalog download | 5 MB or less | T |
| NFR-REL-001 | Crash-free sessions | 99.5% or more | A |
| NFR-REL-002 | ANR and crash rates | Below Google Play bad-behavior thresholds (check current values) | A |
| NFR-REL-003 | App opens and shows cached data with no network | Always | T |
| NFR-SEC-001 | All network traffic over HTTPS | 100% | I |
| NFR-SEC-002 | Firestore rules: users read and write only their own profile data; clients cannot write content | Enforced and tested | T |
| NFR-SEC-003 | No key with write power in the app; Firebase App Check enabled | Yes | I |
| NFR-SEC-004 | Passport and ID numbers are not collected in R1 | Yes | I |
| NFR-SEC-005 | Release build shrunk and obfuscated (R8); signing keys stored securely and backed up | Yes | I |
| NFR-PRV-001 | Collect only needed data; privacy policy link and Play data safety form accurate | Yes | I |
| NFR-PRV-002 | Account deletion removes profile, saved items and tracker data | Within 30 days (verify with backups) | T |
| NFR-USA-001 | Find a scholarship and set a reminder from Home | 5 taps or less | D |
| NFR-USA-002 | Bangla and English complete; no untranslated strings | 100% | I |
| NFR-USA-003 | Touch targets 48 dp or more; text contrast WCAG AA; font scale up to 200% without clipping; TalkBack labels on controls | Yes | T |
| NFR-CMP-001 | Supported Android versions and widths | API 24+, 320 dp to 600 dp | T |
| NFR-CMP-002 | Light and dark theme | Both | T |
| NFR-MNT-001 | Business rules (eligibility, plan, cost, status) are pure functions with unit tests | 80% or more coverage on domain layer | T |
| NFR-MNT-002 | CI runs build, lint and unit tests on every pull request | Always | I |
| NFR-MNT-003 | Risky features sit behind remote feature flags with a kill switch | Yes | I |
| NFR-DAT-001 | Published records with sourceUrl and verifiedOn | 100% | I |
| NFR-DAT-002 | Deadlines and rules re-verified | Every 90 days | A |
| NFR-DAT-003 | Share of stale published records | Below 5% | A |
| NFR-OBS-001 | Analytics events for the funnel in section 6.6; non-fatal logging of sync failures | Yes | I |

## 2.9 External interfaces

| Interface | Purpose | Release |
| --- | --- | --- |
| Firebase Auth | Phone OTP and Google sign-in | R1 |
| Cloud Firestore | Content catalog, user data | R1 |
| Firebase Cloud Messaging | Push notifications | R1 |
| Firebase Remote Config | Feature flags, plan rules, thresholds | R1 |
| Crashlytics and Analytics | Stability and usage | R1 |
| Exchange-rate API | BDT conversion, cached 24 hours | R2 |
| University list import (Hipolabs) | Name, country, website only | R2 |
| Play Billing | Premium payments | R4 |

## 2.10 Key use cases

**UC-01 Find a scholarship and set a reminder** (Student)

1. Student opens Explore, then Scholarships.
2. Student filters by country, level and funding.
3. System lists matches with status badges.
4. Student opens a scholarship; system shows detail, source and verified date.
5. Student taps Set reminder and picks 14 days before.
6. System schedules the reminder and confirms.

Alternative: if the scholarship is Closed, the system shows the expected next cycle (FR-SCH-004). If offline, cached data is shown (FR-SYN-003).

**UC-02 Build a plan** (Student, R2)

1. Student completes the profile.
2. Student opens Plan; system runs eligibility and scoring with server rules.
3. System shows Plan A, B, C with reasons, deadlines and sources.
4. Student adjusts weights; ranking updates.

Alternative: missing profile fields are listed; the plan still shows with Unknown where data is missing.

**UC-03 Report wrong information** (Student)

1. Student taps Report on a record and writes what is wrong.
2. System stores the report with the record reference and confirms.
3. Admin sees it in the queue (FR-ADM-004), checks the source and fixes or rejects it.

**UC-04 Verify and publish a record** (Editor, Verifier)

1. Editor enters data with sourceUrl and verifiedOn.
2. Verifier compares it with the official page and approves.
3. System publishes; apps receive it on the next sync.

## 2.11 Traceability (goals to requirements)

| Goal or metric | Requirements |
| --- | --- |
| Verified, source-linked data | FR-TRU-001, FR-TRU-002, FR-ADM-001 to FR-ADM-005, NFR-DAT-001 to NFR-DAT-003 |
| Personal plan | FR-PRO-002, FR-ELG-001, FR-ELG-002, FR-PLN-001 to FR-PLN-004 |
| Works on low-end phones and weak networks | FR-SYN-001 to FR-SYN-004, NFR-PERF-001 to NFR-PERF-004, NFR-REL-003 |
| Bangla and English | FR-PRO-004, NFR-USA-002 |
| Reminder set rate | FR-NTF-001 to FR-NTF-004 |
| Crash-free sessions | NFR-REL-001, NFR-REL-002, NFR-MNT-001 to NFR-MNT-003 |

## 2.12 Change log

| Version | Date | Change |
| --- | --- | --- |
| 1.0 | 22 Oct 2026 | First full draft |
| 1.1 | 24 Oct 2026 | Added FR-AUTH-005 (Sign out from Me tab) and re-authentication flow for account deletion |

---

# Part 3. Technical design

## 3.1 Architecture

Pattern: MVVM with a light clean-architecture split, offline-first. One screen = one Screen composable + one ViewModel + one UiState.

```
UI (Jetpack Compose screens)
   | observes StateFlow<UiState>, sends events
ViewModel
   | calls
Use cases (pure Kotlin: CheckEligibility, BuildPlan, EstimateCost, ScholarshipStatus)
   | calls
Repository interfaces
   |-- Local:  Room database (catalog cache, saved items, checklist progress)
   '-- Remote: Firebase (catalog sync, auth, reports, config)
```

All screens render from Room. The network only fills Room. This is why the app works offline and stays fast.

## 3.2 Technology

| Area | Choice | Reason |
| --- | --- | --- |
| Language | Kotlin, Coroutines, Flow | Standard for Android |
| UI | Jetpack Compose, Material 3 | Modern, theme and dark mode built in |
| Navigation | Navigation Compose | Official, type-safe routes |
| Dependency injection | Hilt | ViewModel injection |
| Local database | Room | Offline catalog and progress |
| Preferences | DataStore | Language, Track, onboarding flag |
| Backend | Firebase: Auth, Firestore, FCM, Remote Config, App Check, Crashlytics, Analytics | Fastest for one developer |
| Server logic | Cloud Functions | Admin writes, validation, scheduled jobs |
| Reminders | WorkManager plus FCM | Survives app close and restart |
| Serialization | kotlinx.serialization | Seed data and API models |
| Images | Coil | Compose friendly |
| Quality tools | JUnit, MockK, Turbine, Compose UI test, detekt, ktlint, Android Lint | See Part 6 |
| CI | GitHub Actions | Build, lint, tests on every pull request |

Backend choice is kept behind Repository interfaces, so Supabase or Postgres can replace Firebase later without changing screens.

## 3.3 Project structure

```
app/src/main/java/.../studyabroad/
  core/        designsystem, network, database, common
  data/        remote (DTOs), local (entities, mappers), repository (impl)
  domain/      model, usecase
  feature/     onboarding, home, explore, country, university, scholarship,
               tracker, documents, cost, eligibility, plan, profile
  navigation/  NavGraph, Routes
```

Start as a single module. Split into :core, :data and :feature-\* modules when build time or team size needs it.

## 3.4 Data model

Keep the schema platform-neutral so Android and React Native read the same data.

| Entity | Main fields |
| --- | --- |
| Country | id, name, tier, tuitionRange, livingRange, workRule, postStudyVisa, prYears, language, risk, advisory, sourceUrl, verifiedOn, updatedAt, deleted |
| University | id, countryId, name, rank, programs, tuition, deadline, requirements, website, sourceUrl, verifiedOn, updatedAt, deleted |
| Scholarship | id, name, countryId, level, funding, coverage, eligibility, openDate, deadline, applyUrl, sourceUrl, verifiedOn, updatedAt, deleted |
| ChecklistTemplate | id, track, countryId, items, attestationSteps, updatedAt |
| UserProfile | uid, track, cgpa, cgpaScale, subject, ielts, expYears, age, budget, goal, targetCountries |
| Application | id, uid, refType, refId, stage, docsDone, deadline |
| Report | id, uid, refType, refId, message, status, createdAt |

Scholarship example:

```json
{
  "id": "sch_stipendium_hungaricum",
  "name": "Stipendium Hungaricum",
  "countryId": "HU",
  "level": ["bachelor", "master", "phd"],
  "funding": "full",
  "eligibility": { "minCgpa": null, "minIelts": null, "minExpYears": 0, "maxAge": null },
  "openDate": "2026-11-01",
  "deadline": "2027-01-15",
  "applyUrl": "https://stipendiumhungaricum.hu/apply/",
  "sourceUrl": "https://stipendiumhungaricum.hu/",
  "verifiedOn": "2026-10-21",
  "updatedAt": "2026-10-21T10:00:00Z",
  "deleted": false
}
```

Example values must be re-verified before publishing. Status is never stored; it is calculated from openDate, deadline and today.

## 3.5 Sync strategy

1. First launch: download the full catalog (target 5 MB or less) into Room.
2. Later: query records where updatedAt is after the stored lastSyncedAt. This keeps reads and cost low.
3. Removed records are marked deleted: true (not erased), so devices can remove them locally.
4. Run on launch and at least every 24 hours (WorkManager, network required).
5. On failure, keep cached data, record a non-fatal log, retry with backoff.
6. Show Last updated on screens that use cached data.

Because the whole catalog is local, filters and search run in Room, which avoids Firestore query limits.

## 3.6 Business rules as pure functions

These rules have no Android code in them, so they are easy to unit test and to reuse in React Native.

| Rule | Logic |
| --- | --- |
| Scholarship status | today before openDate: Opening soon; between openDate and deadline: Open; after deadline: Closed |
| Eligibility | Compare profile with each non-null criterion; any failed criterion gives Not eligible or Almost (within a defined margin); any missing required field gives Unknown |
| Country score | Weighted average of factor scores (below) |
| Cost estimate | Sum of stored ranges converted to BDT with the latest cached rate |

```latex
Score_c = \frac{\sum_{i=1}^{10} w_i \cdot s_{c,i}}{\sum_{i=1}^{10} w_i}
```

w are the user weights, s the verified factor scores of country c. Plan rules and default weights live in Remote Config or Firestore so they change without an app release (FR-PLN-003).

## 3.7 Security design

Clients can read the catalog but never write it. Content is written only by the admin panel through server code that checks the user role.

```
rules_version = '2';
service cloud.firestore {
  match /databases/{db}/documents {
    match /users/{uid}/{doc=**} {
      allow read, write: if request.auth != null && request.auth.uid == uid;
    }
    match /catalog/{col}/{id} {
      allow read: if request.auth != null;
      allow write: if false;
    }
    match /reports/{id} {
      allow create: if request.auth != null;
      allow read, update, delete: if false;
    }
  }
}
```

This is a starting sketch. Test it with the Firebase emulator before release (Part 6). Also: enable App Check, use R8 shrinking, keep the signing key backed up, and never ship an admin key in the app.

## 3.8 Remote configuration

| Key | Use |
| --- | --- |
| plan\_weights\_default | Default factor weights |
| plan\_rules\_version | Which rule set the app uses |
| stale\_days\_deadline | Days before a deadline record is stale (default 90) |
| feature\_flags | On and off switches per feature, with kill switch |
| min\_supported\_version | Prompt to update when the app is too old |

## 3.9 React Native readiness

Compose screens are not reused in React Native. These choices keep most of the work reusable:

1. Same backend and data schema.
2. Rules on the server or in Remote Config, or a written test-case table to port.
3. Design tokens (colors, spacing, type) kept in one JSON file.
4. A written UiState and event list per screen.
5. Same route names and analytics event names.
6. A short API contract: field names, types, nullable fields.

---

# Part 4. UX and screens

## 4.1 Navigation

Bottom navigation: Home, Explore, Track, Me (Community joins in Release 3). Sketches for the main screens are in [UI Sketches](UI_SKETCHES.md).

## 4.2 Screen list

| Screen | Tab | Rel | Main requirements |
| --- | --- | --- | --- |
| Splash, Onboarding (3 slides) | - | R1 | FR-TRU-004 |
| Login | - | R1 | FR-AUTH-001, FR-AUTH-002 |
| Track select, Profile setup (4 steps) | - | R1 | FR-PRO-001, FR-PRO-002 |
| Home (progress, Quick Summary, deadlines) | Home | R1 | FR-CMP-003, FR-NTF-001 |
| Notifications | Home | R1 | FR-NTF-003, FR-NTF-004 |
| Country list with Tier toggle | Explore | R1 | FR-CTY-001 |
| Master Comparison | Explore | R1 | FR-CMP-001, FR-CMP-002 |
| Country Card | Explore | R1 | FR-CTY-002, FR-CTY-003 |
| University list and detail | Explore | R1 | FR-UNI-001 to FR-UNI-004 |
| Scholarship list and detail | Explore | R1 | FR-SCH-001 to FR-SCH-006 |
| Saved items | Track | R1 | FR-SAV-001 |
| Document checklist | Track | R1 | FR-DOC-001 to FR-DOC-003 |
| Me, Settings, Language, Disclaimer, Delete account | Me | R1 | FR-PRO-004, FR-AUTH-004, FR-TRU-005 |
| Report wrong info (dialog) | all | R1 | FR-TRU-003 |
| Eligibility checker | Explore | R2 | FR-ELG-001, FR-ELG-002 |
| Smart Match and Plan A/B/C | Home | R2 | FR-PLN-001 to FR-PLN-004 |
| Cost calculator and Loan planner | Explore | R2 | FR-CST-001, FR-CST-002 |
| Application tracker | Track | R2 | FR-TRK-001 |
| Roadmap | Track | R2 | FR-RDM-001 |
| Deadline calendar | Track | R2 | FR-CAL-001 |
| Visa, work, family, PR, career, exam guides | Explore | R3 | FR-VIS to FR-EXM |
| Community and Mentors | Community | R3 | FR-COM, FR-MEN |

## 4.3 Design rules

- Material 3 with one brand color, light and dark themes.
- Use a font that supports Bangla well (for example Noto Sans Bengali) and test long Bangla text.
- Spacing on a 4 dp grid; touch targets 48 dp or more.
- Status badges use color plus text plus icon, never color alone: Open (green), Opening soon (amber), Closed (grey).
- **Every screen defines five states:** loading, content, empty, error, offline or stale. No screen ships without them.
- Dates are written as 15 Jan 2027; money shows BDT first and the original currency in brackets.
- Plain, short sentences in both languages. No promises such as guaranteed or sure.

---

# Part 5. Data and content operations

The app is only as good as its data. This part is the working process for keeping it correct.

## 5.1 Source hierarchy

| Rank | Source | Use |
| --- | --- | --- |
| 1 | Official scholarship, government, embassy or immigration site | Deadlines, eligibility, visa and work rules |
| 2 | University official admission page | Tuition, requirements, intake |
| 3 | Respected data tools (exchange rate, Numbeo, QS or THE as reference) | Reference ranges, links only where licence is unclear |
| 4 | News and aggregators | Hints to check at rank 1 or 2; never the final source |
| - | Forums, social groups, agents | Never a source |

## 5.2 Workflow

1. **Collect:** open the official page, save the URL and a note of what it says.
2. **Enter:** create the record in the admin panel; sourceUrl and verifiedOn are required.
3. **Review:** a second person (or the same person after 24 hours) compares the record with the page.
4. **Publish:** the Verifier approves; apps receive it on the next sync.
5. **Re-verify:** the panel lists records due for checking.
6. **Correct:** wrong-info reports go to the queue and are fixed or rejected with a note.

## 5.3 Re-verification schedule

| Data | Re-verify every |
| --- | --- |
| Scholarship deadlines and eligibility | 30 days, and 14 days before each deadline |
| Visa fees, work rules, post-study and PR rules | 90 days |
| University tuition and requirements | Each intake |
| Cost of living ranges | 6 months |
| Attestation steps and Bangladesh documents | 6 months |
| Exchange rate | Automatic, daily |

## 5.4 Before a record is published

- [ ] sourceUrl opens and is an official page
- [ ] verifiedOn is today's date
- [ ] Dates, amounts and currencies match the source
- [ ] Empty eligibility fields are left empty, not guessed
- [ ] Text is short, neutral and has no promise
- [ ] A second person has checked it

## 5.5 Wrong-info reports

Acknowledge within 24 hours, decide within 7 days. A wrong deadline or fee is treated as a content incident: fix in the admin panel within 24 hours.

## 5.6 Launch content target

| Item | Target for MVP |
| --- | --- |
| Countries with full Country Card | 5 to 8 |
| Scholarships | 20 to 25 |
| Universities | 50 to 60 |
| Document checklists | One per Track, plus country extras for the launch countries |

Start small and correct, then grow. Candidates for the first countries are in Appendix A.

---

# Part 6. Quality, test and release

No app is bug-free. The goal is to find problems early, ship in small steps, and recover fast. This part sets the gates.

## 6.1 Test levels

| Level | What it covers | Tools |
| --- | --- | --- |
| Static checks | Style, common bugs, unused code | detekt, ktlint, Android Lint |
| Unit tests | Eligibility, status, plan score, cost, validation | JUnit, MockK, Turbine |
| Data layer tests | Room queries, mappers, sync logic | Room in-memory DB, Firebase emulator |
| Security rules tests | Users cannot read others' data; clients cannot write content | Firebase emulator rules tests |
| UI tests | Key flows: onboarding, scholarship detail, reminder, checklist | Compose UI test |
| Performance | Cold start, scroll, memory | Macrobenchmark, Baseline Profiles, StrictMode, LeakCanary (debug) |
| Accessibility | TalkBack, font scale, contrast | Accessibility Scanner, manual |
| Localization | Bangla and English, long text, no missing strings | Manual, pseudo-locale |
| Exploratory | Real phones, real flows, odd inputs | Manual before every release |

## 6.2 Definition of Done (every feature)

- [ ] Requirement IDs linked in the pull request
- [ ] Unit tests for new rules; domain coverage stays at 80% or more
- [ ] Five states built: loading, content, empty, error, offline or stale
- [ ] Bangla and English strings added
- [ ] Accessibility checked (labels, 48 dp, font scale)
- [ ] Analytics events added where needed (section 6.6)
- [ ] CI is green (build, lint, tests)
- [ ] Tested on at least one low-end device

## 6.3 Device and condition matrix

| Dimension | Cover at least |
| --- | --- |
| Android versions | 7, 9, 11, 13, latest |
| Memory | 2 GB, 3 GB, 6 GB or more |
| Screen | Small (320 dp) and large phones; font scale 200% |
| Network | Offline, slow (throttled), flight-mode toggle during sync |
| Locale | Bangla and English, light and dark |
| Phone brands | Samsung, Xiaomi, Oppo or Vivo, Realme (strong battery savers can delay reminders) |
| Storage | Low free space |

Reminders must be tested on brands with aggressive battery management. Add a Reminders not arriving? help screen that explains battery settings.

## 6.4 Release process

1. Work in short branches; every pull request runs CI.
2. Tag a release candidate (version 1.0.0, increment versionCode).
3. **Internal testing:** developer and 3 to 5 people.
4. **Closed beta:** 20 to 30 students. Check Google Play's current requirements for new developer accounts (closed testing may be required before production).
5. **Production, staged rollout:** 5%, then 20%, 50%, 100%, holding 24 to 48 hours at each step.
6. **Promote only if** crash-free sessions are 99.5% or more and there is no open P0 bug.
7. **Rollback:** halt the rollout, flip the remote kill switch for the faulty feature, ship a hotfix.

**Store checklist before the first submission**

- [ ] Privacy policy URL and Data safety form match what the app collects
- [ ] Account deletion works in the app and is described in the listing
- [ ] Target API level meets the current Play requirement
- [ ] Content rating and target audience answered
- [ ] Listing text and screenshots in Bangla and English
- [ ] App signing set up; key backed up
- [ ] Disclaimer visible: information only, not legal advice

## 6.5 Bug severity

| Level | Meaning | Response |
| --- | --- | --- |
| P0 | Crash on launch, data loss, sign-in broken, security issue | Stop rollout, hotfix within 24 hours |
| P1 | Major feature broken, wrong deadline or fee shown | Fix within 3 days; content errors within 24 hours |
| P2 | Minor feature problem with a workaround | Next release |
| P3 | Cosmetic | Backlog |

## 6.6 Analytics and monitoring

No personal data (name, phone, email) in event parameters.

| Event | When | Parameters |
| --- | --- | --- |
| onboarding\_started | First screen shown | - |
| profile\_completed | Last setup step done | track |
| country\_viewed | Country Card opened | countryId |
| scholarship\_viewed | Scholarship detail opened | scholarshipId, status |
| reminder\_set | Reminder saved | daysBefore |
| search\_performed | Search or filter used | area |
| plan\_generated (R2) | Plan A/B/C shown | track |
| report\_submitted | Wrong-info report sent | refType |
| sign\_out | User signs out from Me tab | - |
| sync\_failed (non-fatal) | Sync error | errorCode |

Main funnel: install, onboarding\_started, profile\_completed, scholarship\_viewed, reminder\_set.

Watch weekly: crash-free sessions, ANR rate, funnel drop-offs, sync failures, stale-record share, open wrong-info reports.

---

# Part 7. Delivery plan

## 7.1 Phases

Estimates assume one developer at about 20 hours per week. Full-time work roughly halves the time.

| Phase | Time | Goal | Exit gate |
| --- | --- | --- | --- |
| 0. Preparation | 1 to 2 weeks | Data, design and project setup ready | Seed data and 6 screens designed |
| 1. MVP (R1) | 8 to 10 weeks | Core info app | Beta with 20 to 30 students; crash-free 99.5% or more; Play requirements met |
| 2. Smart (R2) | 4 to 6 weeks | Personalisation | Students rate the plan as useful |
| 3. Guides and community (R3) | 4 to 6 weeks | Full guidance | Data process stable; moderation ready |
| 4. Scale (R4) | Ongoing | Growth and income | Retention and revenue numbers known |

**Rules:** data before UI polish; every phase goes to real students before the next starts; community and AI come last, after the core data is trusted.

## 7.2 Sprint plan (Phase 0 to MVP)

| Sprint | Weeks | Work | Done when |
| --- | --- | --- | --- |
| Phase 0 | 1 to 2 | Schema final; 5 countries and 15 scholarships verified; 6 screens in Figma; Firebase and GitHub set up; CI skeleton | Seed JSON and designs approved |
| Sprint 1 | 3 to 4 | Design system, login, Track and profile, DataStore, language switch | User signs in and saves a profile |
| Sprint 2 | 5 to 6 | Room and sync, country list, Master Comparison, Country Card | Country data works offline |
| Sprint 3 | 7 to 8 | Scholarship list, filters, detail, status, reminders (WorkManager and FCM) | Reminder arrives on real phones |
| Sprint 4 | 9 to 10 | Universities, saved items, document checklist, trust labels, report flow, admin panel basics | All R1 Must requirements built |
| Sprint 5 | 11 to 12 | Bug fixing, performance, accessibility, store checklist, closed beta | Release candidate approved |
| Phase 2 | 13 to 18 | Eligibility, Smart Match, Plan A/B/C, cost and loan, roadmap, tracker, calendar | R2 requirements verified |

## 7.3 First 14 days

- [ ] Pick the first 5 countries and verify each on official sites (suggestion: Hungary, Turkiye, Germany, Malaysia, Romania)
- [ ] Write 15 scholarship records in the JSON format of section 3.4
- [ ] Design in Figma: Track select, Home, Comparison, Country Card, Scholarship list, Scholarship detail
- [ ] Create the Firebase project; set budget alerts; enable Auth, Firestore, Crashlytics, Analytics
- [ ] Create the Android Studio project (Compose, Hilt, Room) and the GitHub repo with CI
- [ ] Put design tokens (colors, spacing, type) in one theme file
- [ ] Write the Firestore rules and test them in the emulator
- [ ] Show the sketches to 3 to 5 students and write down what they ask for
- [ ] Draft the privacy policy and disclaimer text
- [ ] Create the Google Play developer account and read the current testing requirements

## 7.4 Risk register

| Risk | Impact | Likelihood | Response |
| --- | --- | --- | --- |
| Wrong or outdated data | High: students lose chances | Medium | Verified date, stale badge, 30 and 90 day checks, report button |
| Too much content to verify | High | High | Start with 5 to 8 countries; grow only as fast as verification allows |
| One developer overload | High | High | Phase gates; cut scope before dates; Community and AI last |
| Legal or trust issue (visa advice) | High | Low | Disclaimer, official links, no advice wording |
| Reminders delayed by phone battery settings | Medium | High | Test on key brands; help screen; show deadline in-app too |
| OTP SMS cost or delivery problems | Medium | Medium | Google sign-in fallback; watch cost |
| Firebase cost grows | Medium | Medium | Budget alerts; local-first sync; delta reads |
| Ranking or data licence | Medium | Medium | Links and attribution; check terms before reuse |
| Play account or policy delay | Medium | Medium | Read requirements early; keep the store checklist (6.4) |
| React Native conversion cost | Medium | Medium | Follow section 3.9 |

## 7.5 Income (after trust is built)

| Model | How |
| --- | --- |
| Premium | Personal roadmap, SOP review, mentor sessions |
| Partnerships | Verified university or consultancy listings, always labelled Sponsored |
| Course partnerships | IELTS and language preparation |

Core information (scholarships, countries, checklists) stays free.

---

# Part 8. Appendices

## Appendix A. Country master table

Snapshot of 21 Oct 2026 from the project's research page. **These values are not verified.** Some figures are estimates. Verify each one on official sources before it goes into the app.

| Country | Tier | Main scholarship | Tuition per year | Living per month | Work per week | Post-study | Fit (/10) |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Hungary | 1 | Stipendium Hungaricum | EUR 3K to 5.5K | EUR 375 to 650 | 30 h | 9 months | 8 |
| Turkiye | 1 | Turkiye Burslari | USD 1.5K to 5K | USD 300 to 550 | allowed | 1 year | 7 |
| China | 1 | CSC | CNY 18K to 40K | CNY 2K to 3.5K | 20 h | 1 year | 7 |
| South Korea | 1 | GKS | KRW 4M to 8M | KRW 0.8M to 1.2M | 20 h | D-10 | 8 |
| Japan | 1 | MEXT | JPY 535K to 985K | JPY 80K to 120K | 28 h | 1 year | 7 |
| Malaysia | 1 | MIS or university | MYR 15K to 45K | MYR 1.8K to 3K | 20 h | none | 7 |
| Romania | 1 | Romanian Govt (limited) | EUR 2K to 7K | EUR 300 to 600 | 20 h | 9 months | 8 |
| Italy | 1 | IYT or DSU | EUR 0.9K to 4.5K | EUR 700 to 1.1K | 20 h | 12 months | 6 |
| Sweden | 1 | Swedish Institute | SEK 80K to 295K | SEK 8K to 12K | unlimited | 12 months | 7 |
| Cyprus | 1 | 50% (some) | EUR 3K to 15K | EUR 700 to 1.2K | 20 h | none | 4 |
| Russia | 1 | Govt quota | USD 3K to 8K | USD 250 to 450 | limited | none | 3 |
| Germany | 2 | DAAD | EUR 0 (public) | EUR 700 to 1K | 120 days per year | 18 months | 9 |
| Canada | 2 | Vanier (PhD) | CAD 15K to 50K | CAD 1K to 2K | 20 h | 3 years | 9 |
| Australia | 2 | Australia Awards | AUD 20K to 50K | AUD 1K to 2K | 48 h per 2 weeks | 2 to 4 years | 8 |
| Netherlands | 2 | Orange Tulip | EUR 8K to 20K | EUR 800 to 1.5K | 16 h | 12 months | 8 |
| Poland | 2 | NAWA | EUR 2K to 6K | EUR 400 to 700 | unlimited | 9 months | 7 |
| UK | 2 | Chevening | GBP 10K to 35K | GBP 800 to 1.5K | 20 h | 2 years | 6 |
| USA | 2 | Fulbright | USD 20K to 60K | USD 1K to 2K | 20 h (OPT/CPT) | 1 to 3 years | 6 |

Tier 1 = scholarship focused, Tier 2 = PR or work focused. Risk notes (for example Russia, Cyprus) must be written neutrally with sources before they are shown to users.

## Appendix B. Scholarship calendar (status on 22 Oct 2026)

Dates come from the research page. Confirm each on the official site.

| Scholarship | Country | Status | Window |
| --- | --- | --- | --- |
| [Erasmus Mundus](https://erasmus-plus.ec.europa.eu/opportunities/individuals/students/erasmus-mundus-joint-masters) | EU | Open | Oct 2026 to Jan 2027 (differs by programme) |
| [Stipendium Hungaricum](https://stipendiumhungaricum.hu/apply/) | Hungary | Opening soon | Opens about Nov 2026, deadline about 15 Jan 2027 |
| [CSC](https://www.campuschina.org/) | China | Opening soon | 15 Dec 2026 to 30 Apr 2027 |
| [Turkiye Burslari](https://turkiyeburslari.gov.tr/en/) | Turkiye | Opening soon | 10 Jan to 20 Feb 2027 |
| [GKS Graduate](https://www.niied.go.kr/eng/contents.do?contentsNo=94&menuNo=503) | South Korea | Opening soon | About Feb 2027 |
| [Swedish Institute](https://si.se/en/apply/scholarships/) | Sweden | Opening soon | About Feb 2027 |
| [Chevening](https://www.chevening.org/) | UK | Closed | Closed 6 Oct 2026; next about Aug 2027 |
| [DAAD EPOS](https://www.daad.de/en/studying-in-germany/scholarships/development-related-postgraduate-courses/) | Germany | Closed | Closed 15 Oct 2026; next about Aug 2027 |
| [MEXT](https://www.bd.emb-japan.go.jp/itpr_en/scholarshipNotice.html) | Japan | Closed | Next Nov to Dec 2027 |

In the app these are not hard-coded; status comes from openDate and deadline (FR-SCH-002).

## Appendix C. Document checklist by Track

| Document | Fresher | Graduate | Professional | Researcher |
| --- | --- | --- | --- | --- |
| Passport (valid) | Yes | Yes | Yes | Yes |
| SSC and HSC certificates and marksheets | Yes | Yes | Yes | Yes |
| BSc or MSc certificate and transcript | - | Yes | Yes | Yes |
| English test (IELTS, TOEFL, PTE, Duolingo or MOI certificate) | Yes | Yes | Yes | Yes |
| CV (Europass format) | Yes | Yes | Yes | Yes |
| SOP or motivation letter | Yes | Yes | Yes (experience focus) | Yes (research focus) |
| Academic recommendation letter | Yes | Yes | Optional | Yes |
| Employer reference and experience letter | - | - | Yes | Optional |
| Salary certificate or tax return | - | - | Where required | - |
| Research proposal and professor contact | - | - | - | Yes |
| GMAT or GRE | - | Optional | For MBA | Optional |
| Police clearance and medical report | Where required | Where required | Where required | Where required |
| Bank statement or sponsor proof | Yes | Yes | Yes | Yes |
| Attestation: Board or University, Ministry of Education, MoFA, Embassy | Yes | Yes | Yes | Yes |

Country rules differ; the app shows country-specific extras from verified records.

## Appendix D. Official links

[MoFA Bangladesh](https://www.mfa.gov.bd/) · [Ministry of Education](https://www.moe.gov.bd/) · [VFS Global](https://www.vfsglobal.com/en/individuals/index.html) · [Europass CV](https://europass.eu.europa.eu/create-europass-cv) · [DAAD scholarships](https://www.daad.de/en/study-and-research-in-germany/scholarships/) · [Chevening](https://www.chevening.org/) · [Campus China](https://www.campuschina.org/) · [Stipendium Hungaricum](https://stipendiumhungaricum.hu/) · [Turkiye Burslari](https://turkiyeburslari.gov.tr/en/) · [REST Countries](https://restcountries.com/) · [Numbeo](https://www.numbeo.com/cost-of-living/)

*All scholarship, visa and cost data in this handbook comes from the project's research page (Oct 2026) and must be verified on official websites before it is published in the app.*
