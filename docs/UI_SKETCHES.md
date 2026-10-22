# Study Abroad Guide App – Master Blueprint (Android first, React Native later)

*Updated 22 Oct 2026 · Ager shob plan + tomar research page + screenshot idea merge kora ekta final blueprint.*

Ei doc ta ek-i jaygay bole: ki banabo, kon order e banabo, data kothay theke ashbe, screen kemon hobe, ar Kotlin Jetpack Compose theke kivabe React Native e jabe.

## 1. App ek line e

Ekjon student (HSC, BSc, MSc ba job-holder) bidesh e porte chaile **kothay, ki, kivabe, koto khoroch, scholarship ache ki, kon document lage, visa kivabe** — shob ek app e, verified data shoho, ar profile onujayi personal Plan A/B/C.

### Kara use korbe (Track)

Onboarding e user ekta Track select korbe; Track onujayi home, scholarship ar document list alada hobe.

| Track | Kara | Main focus |
| --- | --- | --- |
| Fresher | HSC pass | Bachelor scholarship, low-cost country, foundation |
| Graduate | BSc running/complete | Masters scholarship, subject choice |
| Professional | 2+ year job experience | Experience-based scholarship, MBA, career switch, job vs study |
| Researcher | MSc complete | PhD, funded research, professor contact |

### Product principle (shob decision e ei 5 ta rule)

1. **Verified data only:** proti card e source link + last verified date.
2. **Offline-first:** internet slow hole-o saved data dekha jabe.
3. **Bangla + English** shuru theke.
4. **Core free:** scholarship, country, checklist free; premium pore.
5. **Kono guarantee na:** chance estimator ekta guide, promise na.

### Student er chinta → App er solution

| Chinta | Solution | Module |
| --- | --- | --- |
| Kon desh bhalo? | Country comparison + Smart Match | Explore |
| Kon university? | Search, filter, requirement vs profile | Explore |
| Scholarship kothay? | Finder + status + deadline reminder | Explore |
| Total khoroch? | Cost + loan/savings calculator | Tools |
| Document ki ki lage? | Track-wise document pack | Track |
| Visa kivabe? | Step guide + embassy/VFS link | Guides |
| Job/income kivabe? | Work rules + earning guide | Guides |
| Job chere jabo? | Professional track + job vs study | Guides |
| Family nite parbo? | Family shift guide | Guides |
| Fake agent bhoy | Scam alert + verified label | Trust |
| Ami eka | Community + mentor | Community |

---

## 2. Build order: kon ta age, kon ta pore

Niche estimate dhore nisi: **ekjon developer, \~20 ghonta/week**. Full-time korle shomoy ordhek hobe.

| Phase | Shomoy | Goal | Ki ready hobe | Done hobe jokhon |
| --- | --- | --- | --- | --- |
| 0. Prep | 1–2 week | Data + design ready | Data schema, 5 country verified data, 6 screen Figma, Firebase project | Seed data JSON ready, design approve |
| 1. MVP | 8–10 week | Core info app | Login, Track, Country Card, Comparison, Scholarship, University, Checklist, Reminder, Trust label | 20–30 student test kore, crash-free, Play internal test e |
| 2. Smart | 4–6 week | Personalisation | Eligibility checker, Smart Match, Plan A/B/C, Cost calculator, Roadmap, Tracker, Calendar | Student der feedback e plan useful bole |
| 3. Guides + Community | 4–6 week | Full guide | Visa/arrival, work, family, PR, career guide, exam prep, Q&A, mentor | Active user bare, data stable |
| 4. Scale | ongoing | Growth | AI assistant, premium, university partnership, React Native / iOS | Revenue + retention number ache |

**3 ta rule:**

1. UI polish er age data. Data vul hole app ontho.
2. Proti phase real student der hate dao, tarpor porer phase.
3. Community ar AI shobar sheshe: age core data trust toiri koro.

---

## 3. Feature list (merged) + priority

P0 = MVP te must, P1 = Phase 2, P2 = Phase 3, P3 = Phase 4.

| # | Module | Feature | Priority |
| --- | --- | --- | --- |
| 1 | Onboarding + Profile | Phone OTP/Google login, Track select, CGPA/IELTS/experience/budget/goal | P0 |
| 2 | Country Guide | Standard Country Card, Tier toggle (Scholarship / PR-Work), side-by-side compare | P0 |
| 3 | Master Comparison | Sortable table + cost chart, Quick Summary (Top 3 lists) | P0 |
| 4 | University Explorer | Search, filter, detail, shortlist, requirement vs profile meter | P0 |
| 5 | Scholarship Finder | Filter, status (Open / Opening soon / Closed), detail, official apply link | P0 |
| 6 | Notifications | Deadline reminder, new scholarship alert | P0 |
| 7 | Document Checklist | Common + Professional extra + attestation steps | P0 |
| 8 | Trust & Safety | Source + verified date, scam alert, country risk advisory, report wrong info | P0 |
| 9 | Saved + Tracker | Save items, application tracker (Saved → Preparing → Applied → Result) | P1 |
| 10 | Eligibility Checker | Age, experience, CGPA, IELTS → Eligible / Almost / Not eligible | P1 |
| 11 | Smart Match + Plan A/B/C | Weighted country ranking, 3 plan generate | P1 |
| 12 | Cost + Loan planner | Monthly/yearly cost in BDT, savings vs loan | P1 |
| 13 | Roadmap + Calendar | Track-wise timeline, month view deadline | P1 |
| 14 | Visa + Arrival | Visa steps, fee, embassy/VFS link, first-week checklist, airfare | P2 |
| 15 | Earning + Family + PR | Work rules, part-time, spouse/child, PR path | P2 |
| 16 | Career + Exam prep | Subject demand, skill/cert stack, IELTS/GRE/language roadmap | P2 |
| 17 | Community + Mentor | Q&A, verified mentor, group chat | P2 |
| 18 | Document upload (secure) | Optional store, encrypted | P3 |
| 19 | AI assistant + Premium | Chat guide, SOP review, mentor session | P3 |

---

## 4. Plan A / B / C engine

App user er profile theke **3 ta plan** banabe: A = best case, B = realistic, C = fallback. **Version 1 rule-based** (AI na): easy, explainable, test kora jay.

### 4.1 Country score

User slider diye priority weight set korbe; app proti country ke 0–10 score dey.

```latex
Score_c = \frac{\sum_{i=1}^{10} w_i \cdot s_{c,i}}{\sum_{i=1}^{10} w_i}
```

| Factor (i) | Default weight |
| --- | --- |
| Scholarship chance | 20 |
| Career opportunity | 20 |
| Low cost | 15 |
| PR ease | 15 |
| Visa ease | 10 |
| Low risk | 10 |
| Part-time income | 8 |
| Family shift | 5 |
| Language ease | 5 |
| Life quality | 5 |

Weight sum 113; formula te sum diye divide kora hoy tai normalize hoy. `s` (country score per factor) admin panel e verified data theke set hobe.

### 4.2 Plan rules (v1 example, real data diye tune korbe)

| Profile | Plan A (best) | Plan B (realistic) | Plan C (fallback) |
| --- | --- | --- | --- |
| Strong: CGPA 3.5+, IELTS 7+ | Erasmus Mundus, DAAD, Chevening | Stipendium Hungaricum, Swedish Institute | Self-funded Germany / Hungary |
| Average: CGPA \~3.0, IELTS 6.5 | Stipendium Hungaricum, CSC, Türkiye Burslari | Italy IYT, Malaysia MIS | Self-funded Romania / Hungary |
| Weak: CGPA below 3.0, IELTS below 6 | Self-funded Romania / Malaysia (low IELTS ok) | Cyprus, Georgia (confirm each programme) | IELTS/CGPA improve kore next cycle e apply |

Professional track e rule: 2+ year experience thakle experience-based scholarship (Chevening, DAAD EPOS type) age dekhabe; eligibility official site theke verify.

### 4.3 Output screen

```
+------------------------------+
| Tomar Plan (Fall 2027)       |
|                              |
| PLAN A  Best case            |
|  Erasmus Mundus - AI/ML      |
|  Deadline: Jan 2027  Match 82%|
|  [ Roadmap dekho ]           |
|                              |
| PLAN B  Realistic            |
|  Stipendium Hungaricum - CS  |
|  Deadline: Jan 15  Match 74% |
|                              |
| PLAN C  Fallback             |
|  Self-funded Romania         |
|  Budget: 9-14 lakh BDT       |
|                              |
| ! Shob data official site e  |
|   verify koro                |
+------------------------------+
```

---

## 5. Navigation + screen list

Bottom navigation e 5 ta tab: **Home, Explore, Track, Community, Me**. Community Phase 3 porjonto hide thakbe (4 tab).

```
Splash -> Onboarding -> Login -> Profile Setup (Track, Academic, Tests, Budget/Goal) -> Main

Main (bottom nav)
 |- Home       : Quick Summary, Deadline Soon, Plan A/B/C card, Roadmap progress
 |- Explore    : Countries -> Country Card -> Universities -> University Detail
 |               Scholarships -> Scholarship Detail
 |               Compare, Cost Calculator, Eligibility Checker, Smart Match
 |- Track      : Applications, Roadmap, Documents, Calendar, Saved
 |- Community  : Q&A, Mentors            (Phase 3)
 '- Me         : Profile, Settings, Language, Report wrong info, Logout
```

| # | Screen | Tab | Phase |
| --- | --- | --- | --- |
| 1 | Splash | - | 1 |
| 2 | Onboarding (3 slide) | - | 1 |
| 3 | Login (OTP / Google) | - | 1 |
| 4 | Track select | - | 1 |
| 5 | Profile setup (4 step) | - | 1 |
| 6 | Home / Dashboard | Home | 1 |
| 7 | Quick Summary | Home | 1 |
| 8 | Country list + Tier toggle | Explore | 1 |
| 9 | Master Comparison (table + chart) | Explore | 1 |
| 10 | Country Card (detail) | Explore | 1 |
| 11 | University list + filter | Explore | 1 |
| 12 | University detail | Explore | 1 |
| 13 | Scholarship list + filter | Explore | 1 |
| 14 | Scholarship detail | Explore | 1 |
| 15 | Saved items | Track | 1 |
| 16 | Document checklist | Track | 1 |
| 17 | Notifications | Home | 1 |
| 18 | Me / Settings | Me | 1 |
| 19 | Eligibility checker | Explore | 2 |
| 20 | Smart Match quiz + result | Explore | 2 |
| 21 | Plan A/B/C | Home | 2 |
| 22 | Cost calculator + result | Explore | 2 |
| 23 | Loan / savings planner | Explore | 2 |
| 24 | Application tracker | Track | 2 |
| 25 | Roadmap | Track | 2 |
| 26 | Deadline calendar | Track | 2 |
| 27 | Visa guide + Arrival checklist | Explore | 3 |
| 28 | Earning / work guide | Explore | 3 |
| 29 | Family shift + PR path | Explore | 3 |
| 30 | Career + subject guide | Explore | 3 |
| 31 | Exam prep (IELTS/GRE/language) | Explore | 3 |
| 32 | Community feed + question | Community | 3 |
| 33 | Mentor list + chat | Community | 3 |
| 34 | Job vs Study decision guide | Explore | 3 |

---

## 6. Screen sketches (wireframe)

Sketch gulo Figma te design er age layout idea. Compose e proti screen = ekta `Screen` composable + ekta `ViewModel`.

### 6.1 Onboarding: Track select

```
+------------------------+
|  <  Step 1/4           |
|  Tumi kon track e?     |
|                        |
|  ( ) Fresher (HSC)     |
|  ( ) Graduate (BSc)    |
|  ( ) Professional      |
|      (job experience)  |
|  ( ) Researcher (MSc)  |
|                        |
|  [      Next        ]  |
+------------------------+
```

Step 2: CGPA + subject. Step 3: IELTS/GRE + experience year. Step 4: budget, goal (scholarship / self-funded), preferred country.

### 6.2 Home

```
+------------------------------+
| Hi, Rahim!            [bell] |
| [ Search country/univ/sch. ] |
|                              |
| Roadmap  ======----  60%     |
| Next: IELTS register koro    |
|                              |
| Quick Summary                |
| Fully Funded    Low Cost     |
|  1 Hungary       1 Malaysia  |
|  2 Turkiye       2 Romania   |
| PR Friendly     Professional |
|  1 Canada        1 Germany   |
|                              |
| Deadline Soon                |
| > Erasmus Mundus   Jan 2027  |
| > CSC China        Dec 15    |
|                              |
| [ Tomar Plan A/B/C dekho ]   |
+------------------------------+
| Home|Explore|Track|Me       |
+------------------------------+
```

### 6.3 Master Comparison

```
+------------------------------+
| Compare Countries            |
| Tier: [Scholarship|PR|All]   |
| Sort: Cost v        Filter[F]|
|                              |
| Chart: 1st year cost (BDT)   |
| ||  |||  |  ||||  |||||     |
|                              |
| Country  Schol  Cost  Work   |
| Hungary   Yes   Low   30h    |
| Turkiye   Yes   Low   OK     |
| Germany   Yes   Med   120d   |
| Canada    Few   High  20h    |
| [ Row tap -> Country Card ]  |
+------------------------------+
```

### 6.4 Country Card (standard layout, shob country te ek)

```
+--------------------------------------+
| <  Hungary            [save] [Compare]|
| [Fully Funded]  Tier 1               |
+--------------------------------------+
| Deadline | Tuition | Living | Visa    |
| ~Jan 15  | Free*   | EUR/mo | VFS Dhk |
+--------------------------------------+
| Scholarship: Stipendium Hungaricum   |
|  Eligibility / Benefit / Documents   |
|  [ Official apply link ]             |
+--------------------------------------+
| Top Universities                     |
| [BME] [ELTE] [Debrecen] [..]         |
+--------------------------------------+
| Work rights | Post-study | PR path   |
+--------------------------------------+
| WARNING / TIP box                    |
| Source: official site | Verified: date|
+--------------------------------------+
```

### 6.5 Scholarship list + detail

```
+------------------------------+    +------------------------------+
| Scholarships        [Filter] |    | <  Chevening          [save] |
| Chips: Full | Master | All   |    | UK | Masters | Full funded   |
|                              |    |                              |
| +--------------------------+ |    | Status: CLOSED (next Aug 2027)|
| | Erasmus Mundus   OPEN    | |    | Eligibility                  |
| | EU | Masters | Full      | |    |  - 2+ years work experience  |
| | Deadline Jan 2027 (80 d) | |    | Benefits / Documents         |
| | [Save]                   | |    |                              |
| +--------------------------+ |    | [ Set reminder ]             |
| | CSC China   OPENING SOON | |    | [ Official site ]            |
| +--------------------------+ |    | Source: chevening.org | date |
+------------------------------+    +------------------------------+
```

Status badge: **Open** (green), **Opening soon** (amber), **Closed** (grey, next cycle dekhabe).

### 6.6 University detail

```
+------------------------------+
| <  University name    [save] |
| Rank | Fee | Intake | Lang   |
| Tabs: Programs|Admission|Cost|
|                              |
| Requirement vs You           |
|  CGPA  3.0  | You 3.4   OK   |
|  IELTS 6.5  | You 6.0   LOW  |
|                              |
| Deadline: dd Mon yyyy        |
| [ Official website ]         |
| [ Add to my plan ]           |
+------------------------------+
```

### 6.7 Eligibility checker

```
+------------------------------+
| Eligibility Checker          |
| Age [27] Exp [3 yr]          |
| CGPA [3.4] IELTS [6.5]       |
| Level [Masters v]            |
| [        Check        ]      |
+------------------------------+
| Eligible (5)                 |
|  > Scholarship A   OK        |
| Almost (2)                   |
|  > Scholarship C  IELTS +0.5 |
| Not eligible (3)  [ why? ]   |
+------------------------------+
```

### 6.8 Cost calculator

```
+------------------------------+
| Cost Calculator              |
| Country [Germany v]          |
| City    [Berlin  v]          |
| Level   [Masters v]          |
| Style   [Basic | Mid]        |
| [      Calculate       ]     |
+------------------------------+
| Tuition / Rent / Food        |
| Transport / Insurance        |
| One-time: visa, flight       |
| Monthly total      BDT ..    |
| Yearly total       BDT ..    |
| Savings vs Loan   [ Plan ]   |
+------------------------------+
```

### 6.9 Roadmap + Document pack

```
+------------------------------+    +------------------------------+
| Roadmap (Intake: Fall 2027)  |    | Document Pack: Germany       |
| o 12 mash age                |    | Track: Professional          |
|   [x] Country select         |    | [x] Passport        Ready    |
|   [x] IELTS register         |    | [x] Transcript      Ready    |
| o 9 mash age                 |    | [ ] IELTS           Pending  |
|   [ ] SOP draft              |    | [ ] Experience letter Need   |
|   [ ] LOR request            |    | [ ] Reference letter  Need   |
| o 6 mash age                 |    | [ ] SOP draft       25%      |
|   [ ] Apply scholarships     |    | Progress =====-----  50%     |
| o 3 mash age                 |    | [ Checklist PDF download ]   |
|   [ ] Visa, bank statement   |    +------------------------------+
+------------------------------+
```

### 6.10 Application tracker + Community

```
+------------------------------+    +------------------------------+
| My Applications              |    | Community   [Ask question +] |
| Saved | Preparing | Applied  |    | Latest | Country | Mentors   |
|                              |    |                              |
| [Chevening]  [DAAD]  [TUM]   |    | Q: Germany blocked account   |
|  Aug 2027     Aug 2027 Applied|    |    kivabe khulbo?            |
|  Docs 3/6     Docs 5/6        |    |    12 answers | verified     |
| [ + Add application ]        |    | Q: IELTS 6 niye scholarship? |
+------------------------------+    +------------------------------+
```

---

## 7. Data model

Schema ta platform-neutral rakho (Firestore collection ba Postgres table, duto e same field). Tahole Android ar pore React Native e same data use hobe.

| Entity | Main fields |
| --- | --- |
| Country | id, name, tier, tuitionRange, livingRange, workRule, postStudyVisa, prYears, language, risk, advisory, sourceUrl, verifiedOn |
| University | id, countryId, name, rank, programs\[\], tuition, deadline, requirements, website, sourceUrl, verifiedOn |
| Scholarship | id, name, countryId, level, funding, coverage, eligibility, openDate, deadline, status, applyUrl, sourceUrl, verifiedOn |
| ChecklistTemplate | id, track, countryId, items\[\], attestationSteps\[\] |
| UserProfile | uid, track, cgpa, subject, ielts, expYears, age, budget, goal, targetCountries\[\] |
| Application | id, uid, scholarshipId/universityId, stage, docsDone, deadline |
| Post | id, uid, countryId, question, answers\[\], verified |

Scholarship er example (JSON):

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
  "verifiedBy": "admin"
}
```

`status` (Open / Opening soon / Closed) store korbe na; `openDate` ar `deadline` theke app nije calculate korbe. Eligibility field khali (null) thakle app dekhabe "official site e dekho", ekta guess dekhabe na.

---

## 8. Data kothay theke nibo + kivabe verify korbo

**Golden rule:** jekhane shombhob official source. Shob record e `sourceUrl` + `verifiedOn`. Tomar research page ta (Oct 2026 snapshot) prothom seed data hishebe kaje lagbe, kintu app e dewar age proti deadline/fee official site theke abar check korte hobe, karon page er kichu number nijei "anumaan" mark kora.

| Data | Source | Kivabe nibo | Refresh |
| --- | --- | --- | --- |
| Scholarship (deadline, eligibility) | Official portals: [DAAD](https://www.daad.de/en/study-and-research-in-germany/scholarships/), [Chevening](https://www.chevening.org/), [CSC](https://www.campuschina.org/), [Stipendium Hungaricum](https://stipendiumhungaricum.hu/), [Türkiye Burslari](https://turkiyeburslari.gov.tr/en/), [GKS](https://www.studyinkorea.go.kr/en/main.do), [MEXT](https://www.studyinjapan.go.jp/en/), [Erasmus Mundus](https://erasmus-plus.ec.europa.eu/opportunities/individuals/students/erasmus-mundus-joint-masters) | Manual verify (admin panel) | Proti 30 din + deadline er age |
| University tuition + requirement | University official admission page | Manual | Proti intake e |
| University list (name, website) | [Hipolabs Universities API](http://universities.hipolabs.com/) (shudhu name/country/website) | API import | Bochhore 1 bar |
| Country basic info | [REST Countries](https://restcountries.com/) | API | Bochhore 1 bar |
| Visa fee, process, embassy | Embassy / [VFS Global](https://www.vfsglobal.com/en/individuals/index.html) / immigration site | Manual | Proti 90 din |
| Work rule, post-study, PR | Government immigration site ([IRCC](https://www.canada.ca/en/immigration-refugees-citizenship.html), [BAMF](https://www.bamf.de/EN/), [HomeAffairs](https://immi.homeaffairs.gov.au/)) | Manual | Proti 90 din |
| Cost of living | University cost page + [Numbeo](https://www.numbeo.com/cost-of-living/) (reference) | Manual range | Proti 6 mash |
| Exchange rate | Public rate API ba [Wise](https://wise.com/gb/currency-converter/) (check terms) | API, cache 24 ghonta | Daily |
| University ranking | [QS](https://www.topuniversities.com/university-rankings), [THE](https://www.timeshighereducation.com/world-university-rankings) | Link + attribution; data reuse er age license dekho | Bochhore 1 bar |
| Document attestation (BD) | [MoFA](https://www.mfa.gov.bd/), [Ministry of Education](https://www.moe.gov.bd/) | Manual | Proti 6 mash |
| Passport power | [Henley Index](https://www.henleyglobal.com/passport-index) | Link only | - |

### Verification workflow

1. **Collect:** official page theke data nao, screenshot/link save koro.
2. **Enter:** admin panel e record banao (`sourceUrl`, `verifiedOn` bhorte hobe).
3. **Review:** arekjon (ba tumi 24 ghonta pore) abar match koro.
4. **Publish:** app sync e chole jabe.
5. **Re-verify:** deadline-based reminder: 90 din por "stale" hole app e warning badge ("Last verified 4 mash age").
6. **Feedback:** user "Report wrong info" dile admin queue te dhukbe.

Data er 3 level:

- **Tier A (manual verified):** scholarship, country rule, visa, deadline. Eta app er asol value.
- **Tier B (API):** university list, country basic, exchange rate.
- **Tier C (community):** Q&A, mentor tip. Alada label, verified na.

---

## 9. Android architecture (Kotlin + Jetpack Compose)

**Pattern:** MVVM + Clean-lite, offline-first. Ek-ta screen = `Screen` composable + `ViewModel` + `UiState`.

```
UI (Compose Screen)
   | observes StateFlow<UiState>, sends UiEvent
ViewModel
   | calls
UseCase (EligibilityCheck, BuildPlan, CostEstimate ...)
   | calls
Repository (interface)
   |-- Local: Room (cache, saved items, checklist progress)
   '-- Remote: Firebase / REST (verified data, sync)
```

| Layer | Choice | Keno |
| --- | --- | --- |
| Language | Kotlin + Coroutines/Flow | Standard |
| UI | Jetpack Compose + Material 3 | Tomar choice, theme/dark mode easy |
| Navigation | Navigation Compose (type-safe routes) | Official |
| DI | Hilt | Standard, ViewModel inject easy |
| Local DB | Room | Offline cache, saved items, progress |
| Preferences | DataStore | Language, Track, onboarding flag |
| Remote | Firebase (Auth, Firestore, FCM, Remote Config, Crashlytics) | Solo dev MVP er jonno fastest, phone OTP built-in |
| Reminder | WorkManager + FCM | Deadline notification, phone restart e-o thake |
| Images | Coil | Compose-friendly |
| Serialization | kotlinx.serialization | JSON seed + API |
| Test | JUnit, MockK, Turbine, Compose UI test | Rules (eligibility/plan) unit test e |

**Backend decision:** MVP te Firebase. Kintu data access ta `Repository` interface er pichone rakho, tahole pore Supabase/Postgres e jete chaile UI bodlate hobe na. Complex filter (country + level + funding + deadline) Firestore e kothin hole **Phase 1 e full catalog local Room e sync kore local e query koro**: catalog chhoto (kayek hajar record), tai offline-first e fast hoy.

**Offline rule:** app open e `lastSyncedAt` dekhe delta sync; Room theke shob screen render; network na thakle "Last updated" date shoho dekhabe.

### Project structure (package-by-feature)

```
app/src/main/java/.../studyabroad/
  core/
    designsystem/   Theme, Color, Type, Spacing, common composables (Card, Badge, StatusChip)
    network/        Firebase / Retrofit setup
    database/       Room DB, DAOs
    common/         Result, DateUtils, CurrencyFormat
  data/
    remote/         DTOs, data sources
    local/          entities, mappers
    repository/     *RepositoryImpl
  domain/
    model/          Country, University, Scholarship, UserProfile ...
    usecase/        BuildPlan, CheckEligibility, EstimateCost, GetDeadlines
  feature/
    onboarding/ home/ explore/ country/ university/ scholarship/
    tracker/ documents/ cost/ eligibility/ plan/ community/ profile/
  navigation/       NavGraph, Routes
  MainActivity.kt
```

Phase 1 e single module; app boro hole `:core`, `:data`, `:feature-*` module e bhag korbe.

### Rule: Composable e business logic na

Eligibility, Plan A/B/C score, cost calculation = **pure Kotlin function** (`domain/usecase`), unit test shoho. Eta React Native e jawar shomoy shobcheye boro shubidha dibe (section 10).

---

## 10. Pore React Native e jawar strategy

Shotti kotha: **Compose UI React Native e reuse hoy na.** UI ta notun kore likhte hobe. Kintu 70–80% kaj reuse hobe jodi aage thekei ei shob rakho:

1. **Backend + data schema same:** Firebase/Supabase er collection same thakbe. React Native e [react-native-firebase](https://rnfirebase.io/) ba Supabase JS client use kore.
2. **Business rule server e:** Eligibility ar Plan scoring **Cloud Function ba Remote Config JSON** e rakho (client e na). Tahole Android ar RN dui-i same API call korbe, rule porte hobe na. Jodi client e rakho, test case gulo table hishebe likhe rakho; TypeScript e same test pass korate hobe.
3. **Design tokens JSON:** color, spacing, font size ekta JSON file e; Compose theme ar RN theme dui jaygay thekei pore.
4. **Screen spec:** proti screen er `UiState` (sealed class) ar event list likhe rakho; RN e same state shape TypeScript type hobe.
5. **Same route name ar analytics event name.**
6. **API contract doc:** field name, type, null-able; (OpenAPI ba shudhu ekta markdown).

| Android (Compose) | React Native |
| --- | --- |
| Composable | Function component |
| LazyColumn | FlatList / FlashList |
| Navigation Compose | React Navigation |
| ViewModel + StateFlow | Zustand / Redux Toolkit + React Query |
| Hilt | Context / simple module injection |
| Room | WatermelonDB / SQLite (expo-sqlite) |
| DataStore | MMKV / AsyncStorage |
| Retrofit / Firebase SDK | React Query + fetch/axios / react-native-firebase |
| WorkManager + FCM | FCM + notification library (jodi local schedule lage) |
| Coil | expo-image / FastImage |
| Material 3 | React Native Paper |

**Conversion order (screen by screen):** Splash/Login → Profile → Country list/Card → Scholarship → University → Tracker/Checklist → baki. Android app live thakbe, RN version beta te alada release hobe.

**Ekta ektu alada option:** jodi RN er main karon iOS pawa hoy, tahole Kotlin Multiplatform / Compose Multiplatform-o dekhte paro (Kotlin code share hoy). Kintu tomar goal jodi RN team/ecosystem hoy, uporer plan e thako. Phase 1 e ei decision nite hobe na; shudhu upor er 6 ta rule follow korle dui option-i khola thake.

---

## 11. Sprint plan (Phase 0 → Phase 2)

Estimate: ek developer, \~20 ghonta/week, 2 week er sprint.

| Sprint | Week | Ki hobe | Done hobe jokhon |
| --- | --- | --- | --- |
| Phase 0 | 1–2 | Data schema final, 5 country + 15 scholarship verified JSON, Figma 6 screen, Firebase + GitHub repo | Seed JSON + design ready |
| Sprint 1 | 3–4 | Project setup, design system (theme, StatusChip, Card), Login, Track + Profile setup, DataStore | User login kore profile save korte pare |
| Sprint 2 | 5–6 | Room + sync, Country list, Master Comparison, Country Card | Offline e country data dekhay |
| Sprint 3 | 7–8 | Scholarship list/filter/detail, status badge, WorkManager + FCM deadline reminder | Reminder notification ashe |
| Sprint 4 | 9–10 | University list/detail, Saved, Document checklist, Trust labels (source, verified date, report) | Shob P0 feature complete |
| Sprint 5 | 11–12 | Bug fix, Bangla/English, Crashlytics, Play internal test, 20–30 student feedback | MVP release candidate |
| Phase 2 | 13–18 | Eligibility, Smart Match + Plan A/B/C, Cost + Loan, Roadmap, Tracker, Calendar | Student rate kore plan useful |

MVP release er age check:

- [ ] Proti country card e source + verified date ache
- [ ] Deadline reminder real phone e kaj kore
- [ ] Offline e app khule data dekhay
- [ ] Privacy policy + disclaimer ache
- [ ] 20+ student test korse

---

## 12. Trust, legal, income

**Trust ar safety**

- Proti data te source link + last verified date. 90 din por stale warning.
- Scam alert: kono scholarship e taka dite hoy na; fake bank statement = permanent visa ban; "guaranteed admission" = scam.
- Country risk advisory (jemon Russia: war, banking restriction; Cyprus: weak CS job market; Malta: limited CS programme). Eta tomar research page er personal opinion, app e dewar age neutral ar sourced vabe likho.
- Sponsored listing alada label kore dekhabe.
- Chance estimator = "approximate guide", guarantee na.

**Privacy**

- Passport/certificate number app e store korbe na (Phase 1). Document upload Phase 3, optional, encrypted.
- Privacy policy Play Store e lagbe; user data delete option rakho.
- Freelancing/part-time rule student visa te country-wise alada; app e "immigration lawyer/official site e confirm koro" note dao.

**Income (pore, trust toiri holey)**

| Model | Kivabe |
| --- | --- |
| Premium | Personal roadmap, SOP review, mentor session |
| Partnership | Verified university/consultancy listing, "Sponsored" label shoho |
| Course partnership | IELTS/language prep |

Core info (scholarship, country, checklist) shobar jonno free.

---

## 13. Risk ar ki korbe

| Risk | Kothin kothay | Ki korbe |
| --- | --- | --- |
| Data purano/vul | Student er khoti hobe | Verified date, stale warning, report button, 90 din re-verify |
| Content er volume | 18+ country manual verify | Phase 1 e 5–8 country; baki pore |
| Solo developer overload | Kaj onek | Phase gate; Community/AI pore |
| Legal/trust | Visa/immigration advice | Disclaimer, official link, "not legal advice" |
| Ranking/data license | QS/THE data copy | Link + attribution, license check |
| Firestore filter limit | Complex search | Local Room query (section 9) |
| RN conversion cost | UI rewrite | Section 10 er 6 rule |

---

## 14. Porer 7 din e ki korbe

- [ ] 5 ta country select koro (suggestion: Hungary, Türkiye, Germany, Malaysia, Romania) ar official site theke verify koro
- [ ] 15 ta scholarship er JSON toiri koro (section 7 er format)
- [ ] Figma te 6 screen: Track select, Home, Comparison, Country Card, Scholarship list, Scholarship detail
- [ ] Firebase project + Android Studio project (Compose + Hilt + Room) setup
- [ ] Design token (color, spacing, type) ekta JSON/Theme file e fix koro
- [ ] 3–5 jon student ke sketch dekhao, ki chay jiggesh koro

---

## Appendix A. Country master table

Snapshot: 21 Oct 2026, tomar research page theke. Verify kora hoyni; kichu number page nijei anumaan bole mark korse. App e dewar age official site theke check koro.

| Country | Tier | Main scholarship | Tuition/yr | Living/mo | Work/week | Post-study | Fit /10 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Hungary | 1 | Stipendium Hungaricum | EUR 3–5.5K | EUR 375–650 | 30 h | 9 mo | 8 |
| Türkiye | 1 | Türkiye Burslari | USD 1.5–5K | USD 300–550 | allowed | 1 yr | 7 |
| China | 1 | CSC | CNY 18–40K | CNY 2–3.5K | 20 h | 1 yr | 7 |
| South Korea | 1 | GKS | KRW 4–8M | KRW 0.8–1.2M | 20 h | D-10 | 8 |
| Japan | 1 | MEXT | JPY 535–985K | JPY 80–120K | 28 h | 1 yr | 7 |
| Malaysia | 1 | MIS / university | MYR 15–45K | MYR 1.8–3K | 20 h | none | 7 |
| Romania | 1 | Romanian Govt (limited) | EUR 2–7K | EUR 300–600 | 20 h | 9 mo | 8 |
| Italy | 1 | IYT / DSU | EUR 0.9–4.5K | EUR 700–1.1K | 20 h | 12 mo | 6 |
| Sweden | 1 | Swedish Institute | SEK 80–295K | SEK 8–12K | unlimited | 12 mo | 7 |
| Cyprus | 1 | 50% (some) | EUR 3–15K | EUR 700–1.2K | 20 h | none | 4 |
| Russia | 1 | Govt quota | USD 3–8K | USD 250–450 | limited | none | 3 |
| Germany | 2 | DAAD | EUR 0 (public) | EUR 700–1K | 120 days/yr | 18 mo | 9 |
| Canada | 2 | Vanier (PhD) | CAD 15–50K | CAD 1–2K | 20 h | 3 yr | 9 |
| Australia | 2 | Australia Awards | AUD 20–50K | AUD 1–2K | 48 h/2 wk | 2–4 yr | 8 |
| Netherlands | 2 | Orange Tulip | EUR 8–20K | EUR 800–1.5K | 16 h | 12 mo | 8 |
| Poland | 2 | NAWA | EUR 2–6K | EUR 400–700 | unlimited | 9 mo | 7 |
| UK | 2 | Chevening | GBP 10–35K | GBP 800–1.5K | 20 h | 2 yr | 6 |
| USA | 2 | Fulbright | USD 20–60K | USD 1–2K | 20 h (OPT/CPT) | 1–3 yr | 6 |

---

## Appendix B. Scholarship calendar (status: 22 Oct 2026)

Date gulo tomar research page theke; official site e confirm koro.

| Scholarship | Country | Status | Window |
| --- | --- | --- | --- |
| [Erasmus Mundus](https://erasmus-plus.ec.europa.eu/opportunities/individuals/students/erasmus-mundus-joint-masters) | EU | Open | Oct 2026 – Jan 2027 (programme wise alada) |
| [Stipendium Hungaricum](https://stipendiumhungaricum.hu/apply/) | Hungary | Opening soon | Opens \~Nov 2026, deadline \~15 Jan 2027 |
| [CSC](https://www.campuschina.org/) | China | Opening soon | 15 Dec 2026 – 30 Apr 2027 |
| [Türkiye Burslari](https://turkiyeburslari.gov.tr/en/) | Türkiye | Opening soon | 10 Jan – 20 Feb 2027 |
| [GKS Graduate](https://www.niied.go.kr/eng/contents.do?contentsNo=94&menuNo=503) | South Korea | Opening soon | \~Feb 2027 |
| [Swedish Institute](https://si.se/en/apply/scholarships/) | Sweden | Opening soon | \~Feb 2027 |
| [Chevening](https://www.chevening.org/) | UK | Closed | Closed 6 Oct 2026; next \~Aug 2027 |
| [DAAD EPOS](https://www.daad.de/en/studying-in-germany/scholarships/development-related-postgraduate-courses/) | Germany | Closed | Closed 15 Oct 2026; next \~Aug 2027 |
| [MEXT](https://www.bd.emb-japan.go.jp/itpr_en/scholarshipNotice.html) | Japan | Closed | Next Nov–Dec 2027 |

App e eta hard-code korbe na; `openDate`/`deadline` field theke status auto calculate hobe (section 7).

---

## Appendix C. Document checklist (track wise)

| Document | Fresher | Graduate | Professional | Researcher |
| --- | --- | --- | --- | --- |
| Passport (valid) | Yes | Yes | Yes | Yes |
| SSC/HSC certificate + marksheet | Yes | Yes | Yes | Yes |
| BSc/MSc certificate + transcript | - | Yes | Yes | Yes |
| English test (IELTS/TOEFL/PTE/MOI) | Yes | Yes | Yes | Yes |
| CV (Europass format) | Yes | Yes | Yes | Yes |
| SOP / motivation letter | Yes | Yes | Yes (experience focus) | Yes (research focus) |
| Academic recommendation letter | Yes | Yes | Optional | Yes |
| Employer reference + experience letter | - | - | Yes | Optional |
| Salary certificate / tax return | - | - | Yes (jekhane lage) | - |
| Research proposal + professor contact | - | - | - | Yes |
| GMAT/GRE | - | Optional | MBA te | Optional |
| Police clearance + medical | Yes | Yes | Yes | Yes |
| Bank statement / sponsor proof | Yes | Yes | Yes | Yes |
| Attestation: Board/University → MoE → MoFA → Embassy | Yes | Yes | Yes | Yes |

---

## Appendix D. Official links

[MoFA Bangladesh](https://www.mfa.gov.bd/) · [Ministry of Education](https://www.moe.gov.bd/) · [VFS Global](https://www.vfsglobal.com/en/individuals/index.html) · [IELTS (British Council BD)](https://www.britishcouncil.org.bd/exam/ielts) · [Europass CV](https://europass.eu.europa.eu/create-europass-cv) · [Fintiba (Germany blocked account)](https://www.fintiba.com/) · [Bangladesh Bank forex rules](https://www.bangladesh-bank.org/fnansys/paymentsys/fexregulation.php) · [Numbeo](https://www.numbeo.com/cost-of-living/) · [Wise exchange rate](https://wise.com/gb/currency-converter/)

*Ei doc er shob scholarship/visa/cost data tomar research page (Oct 2026) theke neya; official website e verify na kore app e publish korbe na.*
