
# Vocab+ — Production & Software Requirements Specification

**Document Version:** 1.0.0 (Production-Ready)  
**Platform:** Android (API 26+ / Android 8.0 to Android 14 API 34)  
**Tech Stack:** Kotlin 2.0, Jetpack Compose Material 3, Room SQLite with KSP, Coroutines & StateFlow, Navigation Compose, WorkManager, DataStore  
**Architecture:** Clean Architecture + Offline-First MVI/MVVM  
**Primary Goal:** Lightweight, low-stimulation daily vocabulary learning and retrieval practice application  
**Core Categories:** Synonyms, Antonyms, Idioms

---

## 1. Product Overview

**Vocab+** is a lightweight, offline-first mobile application built for users who already possess basic English proficiency and want to master nuanced word meanings, uncommon vocabulary, precise synonyms, antonyms, and idiomatic expressions.

The application is **not designed to teach elementary English**. It targets 10th-standard-level English proficiency and above, including competitive exams, academic excellence, and professional communication.

### The Daily Interaction Loop

1. **Synonyms** — 10 questions
2. **Antonyms** — 10 questions
3. **Idioms** — 10 questions
4. **Daily Total** — 30 questions/day

Every question features immediate, non-punitive answer feedback, an in-depth contextual explanation, and a formatted example sentence.

---

## 2. Core Product Philosophy

- **Lightweight & Offline-First:** 100% of core quiz and revision features operate with zero network dependency.
- **Low-Stimulation UX:** No neon gradients, flashing banners, sound effects, countdown timers, hearts/lives, or manipulative streak guilt.
- **Short Daily Sessions:** 5–10 minutes per day to complete the 30-question challenge.
- **Permanent Progress:** Points earned can **never decrease** or be wiped by broken streaks.
- **Breakable Perfection Streaks:** Section streaks require **10/10** to continue; the **Giga Streak** requires a perfect **30/30** day.
- **Retrieval Revision:** Previously encountered questions are prioritized using a weighted retrieval algorithm.

---

## 3. Core Business Rules Matrix

| Feature | Production Rule |
| :--- | :--- |
| **Daily Structure** | 3 sections × 10 questions = 30 questions/day |
| **Daily Quiz Scoring** | **+10 points** per correct answer, **0 points** for incorrect. Maximum: **300 pts/day** |
| **Points Persistence** | **Permanent.** Points accumulate infinitely and never decrease. |
| **Section Streak** | **10/10** = streak increments by +1; **< 10/10** = streak resets to 0. |
| **Giga Streak** | **30/30** across all 3 sections = Giga Streak increments by +1; **< 30/30** = resets to 0. |
| **Streak History** | Current and Lifetime Best streaks are tracked independently for all 4 streak types. |
| **Revision Scoring** | **+2 bonus points** per correct answer, capped at **20 bonus points/day**. |
| **Revision Composition** | Mixed randomized categories drawn exclusively from seen questions. |
| **Revision Priority** | 40% Incorrect, 30% Due for Review, 20% Low Accuracy (< 60%), 10% Random Seen. |
| **Notification Limit** | Maximum **one quiet reminder per day**, strictly opt-in. |
| **Idempotency** | Duplicate question submissions or app restarts can **never** award duplicate points. |

---

## 4. Application Architecture & Tech Stack

```text
┌────────────────────────────────────────────────────────────────────────┐
│                        Vocab+ Architecture                             │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
                       ┌────────────▼────────────┐
                       │       MainActivity      │
                       │ System/Light/Dark Theme │
                       └────────────┬────────────┘
                                    │
                       ┌────────────▼────────────┐
                       │      VocabNavHost       │
                       │ Type-Safe Navigation    │
                       └────────────┬────────────┘
                                    │
     ┌──────────────────────────────┼──────────────────────────────┐
     │                              │                              │
┌────▼─────────────┐       ┌────────▼────────┐           ┌─────────▼────────┐
│   HomeScreen     │       │   QuizScreen    │           │  RevisionScreen  │
│ • Total Points   │       │ • 10 Questions  │           │ • Mixed Category │
│ • Giga Streak    │       │ • Explanations  │           │ • +2 Bonus Points│
│ • 3 Section Cards│       │ • Score Tracker │           │ • Weak-Word Focus│
└────┬─────────────┘       └────────┬────────┘           └─────────┬────────┘
     │                              │                              │
     └──────────────────────────────┼──────────────────────────────┘
                                    │
                       ┌────────────▼────────────┐
                       │      Domain Layer       │
                       │ • Strict Models         │
                       │ • Revision Engine       │
                       │ • Deterministic Hasher  │
                       └────────────┬────────────┘
                                    │
                       ┌────────────▼────────────┐
                       │     Data Layer (Room)   │
                       │ • user_stats            │
                       │ • daily_answers         │
                       │ • question_history      │
                       │ • JSON Asset Loader     │
                       └─────────────────────────┘
````

### Technology Highlights

* **Language:** Kotlin 2.0.0 with Java 17 toolchain.
* **UI Framework:** Jetpack Compose Material 3 with edge-to-edge support and custom low-stimulation palette.
* **Local Persistence:** Room 2.6.1 with KSP code generation and SQLite.
* **Preferences:** AndroidX DataStore Preferences.
* **Background Work:** AndroidX WorkManager for quiet daily notification dispatch.
* **Concurrency:** Kotlin Coroutines & StateFlow.
* **Navigation:** Navigation Compose with type-safe screen routes.
* **Code Shrinking:** ProGuard / R8 optimization rules tailored for Compose, Room, and Kotlinx Serialization.
* **Question Storage:** Bundled JSON assets for fully offline operation.
* **Architecture:** Clean Architecture with offline-first MVI/MVVM patterns.
* **Dependency Management:** Gradle Version Catalog.

---

## 5. Directory & File Structure

```text
vocab-plus/
├── gradle/
│   ├── libs.versions.toml                          # Centralized Version Catalog
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
│
├── app/
│   ├── build.gradle.kts                            # App module build scripts
│   ├── proguard-rules.pro                          # R8 shrinking & obfuscation rules
│   │
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml                 # App manifest & permissions
│       │   │
│       │   ├── assets/
│       │   │   └── questions/                      # Bundled JSON Question Banks
│       │   │       ├── synonyms.json
│       │   │       ├── antonyms.json
│       │   │       └── idioms.json
│       │   │
│       │   ├── java/com/vocabplus/app/
│       │   │   ├── VocabApplication.kt             # Application class & DI container
│       │   │   ├── MainActivity.kt                 # Single Activity & Theme wrapper
│       │   │   │
│       │   │   ├── core/
│       │   │   │   ├── designsystem/               # Low-stimulation styling
│       │   │   │   ├── Color.kt
│       │   │   │   ├── Typography.kt
│       │   │   │   ├── Shape.kt
│       │   │   │   ├── Theme.kt
│       │   │   │   └── components/
│       │   │   │       ├── VocabCard.kt
│       │   │   │       ├── VocabButton.kt
│       │   │   │       └── VocabTopAppBar.kt
│       │   │   │
│       │   │   ├── di/
│       │   │   │   └── AppContainer.kt             # Dependency container
│       │   │   │
│       │   │   ├── navigation/
│       │   │   │   ├── Screen.kt
│       │   │   │   └── VocabNavHost.kt
│       │   │   │
│       │   │   ├── notification/
│       │   │   │   ├── NotificationHelper.kt
│       │   │   │   └── DailyReminderWorker.kt
│       │   │   │
│       │   │   └── util/
│       │   │       ├── Result.kt
│       │   │       ├── DispatcherProvider.kt
│       │   │       └── DateUtils.kt
│       │   │
│       │   ├── domain/
│       │   │   ├── engine/
│       │   │   │   └── RevisionEngine.kt           # Spaced retrieval prioritization
│       │   │   │
│       │   │   ├── model/
│       │   │   │   ├── Category.kt
│       │   │   │   ├── Difficulty.kt
│       │   │   │   ├── Question.kt
│       │   │   │   ├── SectionProgress.kt
│       │   │   │   ├── DailyQuizState.kt
│       │   │   │   ├── UserStats.kt
│       │   │   │   ├── QuestionHistory.kt
│       │   │   │   ├── CategoryPerformance.kt
│       │   │   │   └── ThemePreference.kt
│       │   │   │
│       │   │   └── repository/
│       │   │       ├── QuestionRepository.kt
│       │   │       ├── UserProgressRepository.kt
│       │   │       ├── QuizRepository.kt
│       │   │       └── UserPreferencesRepository.kt
│       │   │
│       │   ├── data/
│       │   │   ├── datasource/
│       │   │   │   └── AssetQuestionDataSource.kt
│       │   │   │
│       │   │   ├── local/                          # Room SQLite Database
│       │   │   │   ├── VocabDatabase.kt
│       │   │   │   ├── dao/
│       │   │   │   │   ├── UserStatsDao.kt
│       │   │   │   │   ├── DailyAnswerDao.kt
│       │   │   │   │   ├── DailySectionDao.kt
│       │   │   │   │   └── QuestionHistoryDao.kt
│       │   │   │   │
│       │   │   │   └── entity/
│       │   │   │       ├── UserStatsEntity.kt
│       │   │   │       ├── DailyAnswerEntity.kt
│       │   │   │       ├── DailySectionEntity.kt
│       │   │   │       └── QuestionHistoryEntity.kt
│       │   │   │
│       │   │   ├── model/
│       │   │   │   └── QuestionDto.kt
│       │   │   │
│       │   │   ├── repository/
│       │   │   │   ├── QuestionRepositoryImpl.kt
│       │   │   │   ├── RoomQuizRepository.kt
│       │   │   │   ├── RoomUserProgressRepository.kt
│       │   │   │   └── DataStoreUserPreferencesRepository.kt
│       │   │   │
│       │   │   └── validator/
│       │   │       └── ContentValidator.kt
│       │   │
│       │   └── presentation/                      # Jetpack Compose UI
│       │       ├── home/
│       │       │   ├── HomeScreen.kt
│       │       │   ├── HomeViewModel.kt
│       │       │   └── HomeUiState.kt
│       │       │
│       │       ├── quiz/
│       │       │   ├── QuizScreen.kt
│       │       │   ├── QuizViewModel.kt
│       │       │   ├── QuizUiState.kt
│       │       │   └── components/
│       │       │       ├── OptionSelector.kt
│       │       │       └── ExplanationCard.kt
│       │       │
│       │       ├── result/
│       │       │   ├── SectionResultScreen.kt
│       │       │   ├── DailySummaryScreen.kt
│       │       │   └── DailySummaryViewModel.kt
│       │       │
│       │       ├── revision/
│       │       │   ├── RevisionScreen.kt
│       │       │   ├── RevisionViewModel.kt
│       │       │   └── RevisionUiState.kt
│       │       │
│       │       ├── statistics/
│       │       │   ├── StatisticsScreen.kt
│       │       │   ├── StatisticsViewModel.kt
│       │       │   └── StatisticsUiState.kt
│       │       │
│       │       └── settings/
│       │           ├── SettingsScreen.kt
│       │           ├── SettingsViewModel.kt
│       │           └── SettingsUiState.kt
│       │
│       └── test/java/com/vocabplus/app/            # Unit Test Suite
│           ├── core/util/
│           │   └── TestDispatcherProvider.kt
│           │
│           ├── domain/
│           │   ├── engine/
│           │   │   └── RevisionEngineTest.kt
│           │   │
│           │   └── model/
│           │       ├── QuestionModelTest.kt
│           │       ├── DailyQuizStateTest.kt
│           │       └── CategoryPerformanceTest.kt
│           │
│           ├── data/
│           │   ├── repository/
│           │   │   └── QuestionRepositoryTest.kt
│           │   │
│           │   └── validator/
│           │       ├── ContentValidatorTest.kt
│           │       └── ProductionAssetContentTest.kt
│           │
│           └── presentation/
│               ├── quiz/
│               │   └── QuizViewModelTest.kt
│               └── revision/
│                   └── RevisionViewModelTest.kt
│
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── gradlew
└── gradlew.bat
```

---

## 6. Deterministic Daily Quiz Algorithm

To ensure every user globally receives the exact same questions for a given calendar date without requiring an active backend connection, Vocab+ uses a deterministic hashing permutation.

The seed is derived from the ISO date and category:

```kotlin
val seed =
    (dateIso.hashCode().toLong() shl 16) xor
        category.slug.hashCode().toLong()

val random = Random(seed)

val shuffled = questions.toMutableList()

for (i in shuffled.size - 1 downTo 1) {
    val j = random.nextInt(i + 1)

    val temp = shuffled[i]
    shuffled[i] = shuffled[j]
    shuffled[j] = temp
}

return shuffled.take(10)
```

### Guarantees

* Every user receives the same daily questions.
* Each category has an independent deterministic permutation.
* No backend service is required.
* The same date produces the same question ordering.
* The algorithm remains reproducible across application restarts.

---

## 7. Data Integrity & Idempotency Rules

Daily completion must be strictly idempotent.

The `daily_answers` table uses the combination of `date_iso` and `question_id` as a unique logical key:

```sql
CREATE TABLE daily_answers (
    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    date_iso TEXT NOT NULL,
    question_id TEXT NOT NULL,
    category_slug TEXT NOT NULL,
    selected_option_index INTEGER NOT NULL,
    is_correct INTEGER NOT NULL,
    points_awarded INTEGER NOT NULL,
    answered_at_millis INTEGER NOT NULL
);

CREATE UNIQUE INDEX index_daily_answers_date_iso_question_id
ON daily_answers(date_iso, question_id);
```

### Idempotency Guarantees

* Answer insertion uses `OnConflictStrategy.IGNORE`.
* The same question cannot award points twice for the same day.
* App restarts cannot duplicate rewards.
* App crashes during a quiz cannot duplicate rewards.
* Re-submitting an already answered question cannot award additional points.
* A completed section cannot be reopened as an editable quiz.
* Selecting a completed section opens its Section Result immediately.

This ensures the scoring system remains consistent even under interrupted sessions or repeated user actions.

---

## 8. Build, Test & Release Instructions

### Prerequisites

* **JDK:** Java 17
* **Android SDK:** API 34 (Android 14)
* **Android Build Tools:** 34.0.0 or compatible installed version
* **Gradle:** Gradle 8.5+
* **Kotlin:** Kotlin 2.0.0
* **Android Studio:** Iguana (2023.2.1) or later
* **Minimum Android Version:** API 26 / Android 8.0

The project uses the **Gradle Wrapper**, so a system-wide Gradle installation is not required for normal development.

### Command-Line Execution

#### 1. Run the Full Unit Test Suite

```bash
./gradlew test
```

#### 2. Assemble Debug APK

```bash
./gradlew assembleDebug
```

Output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

#### 3. Assemble Optimized Release APK

```bash
./gradlew assembleRelease
```

Output:

```text
app/build/outputs/apk/release/app-release-unsigned.apk
```

#### 4. Generate Google Play Release App Bundle

```bash
./gradlew bundleRelease
```

Output:

```text
app/build/outputs/bundle/release/app-release.aab
```

### Running on a Physical Android Device

A physical Android device can be connected through either USB debugging or Android Wireless Debugging.

Verify an ADB connection with:

```bash
adb devices
```

Then run the application through Android Studio or install the generated debug APK.

For wireless debugging, the phone and development machine must be connected to the same local network.

---

## 9. Testing & Quality Assurance

Vocab+ uses automated unit tests to protect its core business logic and content pipeline.

### Core Test Areas

* Question validation and invariants.
* Daily quiz scoring.
* Section streak calculations.
* Giga Streak calculations.
* Revision prioritization.
* Category performance calculations.
* Repository behavior.
* Content validation.
* Production JSON asset integrity.
* Quiz ViewModel state transitions.
* Revision ViewModel state transitions.
* Coroutine dispatcher behavior.

### Content Pipeline Validation

All bundled question assets are validated automatically.

The production content test ensures that **100% of bundled JSON question assets** satisfy the application's structural and content requirements.

This prevents malformed, incomplete, duplicated, or invalid questions from silently entering the production question bank.

---

## 10. Definition of Done

### Product Features

* [x] Category Quizzes: Synonyms (10 Q), Antonyms (10 Q), Idioms (10 Q) = 30 Q/day.
* [x] Immediate answer feedback.
* [x] Detailed explanations for every question.
* [x] Correct answer display.
* [x] Natural example sentences.
* [x] +10 points per correct answer.
* [x] 0 points for incorrect answers.
* [x] Permanent points that never decrease.
* [x] Independent section streaks.
* [x] 10/10 required to continue a section streak.
* [x] 30/30 required to continue the Giga Streak.
* [x] Current and lifetime-best streak tracking.
* [x] Daily quiz idempotency.
* [x] Persistent Room SQLite database.
* [x] Question history tracking.
* [x] Weighted revision engine.
* [x] +2 revision bonus points.
* [x] Revision bonus capped at 20 points/day.
* [x] Mixed-category revision sessions.
* [x] Statistics screen.
* [x] Overall accuracy tracking.
* [x] Questions answered tracking.
* [x] Per-category accuracy tracking.
* [x] Current and best streak statistics.
* [x] Light theme.
* [x] Dark theme.
* [x] System default theme.
* [x] Persistent theme preference.
* [x] Zero navigation flicker.
* [x] Reset-data confirmation dialog.
* [x] Optional daily reminder.
* [x] Maximum one quiet notification per day.
* [x] WorkManager-based notification scheduling.
* [x] Fully offline core functionality.
* [x] Production JSON asset validation.
* [x] Accessibility support.
* [x] Scalable typography.
* [x] Minimum 48dp touch targets.
* [x] Semantic screen-reader labels.
* [x] Color-independent status indicators.

### Engineering Standards

* [x] Clean Architecture.
* [x] Offline-first architecture.
* [x] Kotlin Coroutines.
* [x] StateFlow-based reactive state.
* [x] Jetpack Compose Material 3.
* [x] Room SQLite persistence.
* [x] KSP code generation.
* [x] AndroidX DataStore.
* [x] WorkManager.
* [x] Type-safe Navigation Compose.
* [x] Gradle Version Catalog.
* [x] Gradle Wrapper.
* [x] R8 / ProGuard configuration.
* [x] Automated unit testing.
* [x] Deterministic daily question selection.
* [x] Idempotent scoring and persistence.

---

## 11. Design Principles

Vocab+ deliberately avoids engagement patterns that encourage excessive or compulsive use.

### No Artificial Pressure

The application does not use:

* Countdown timers.
* Hearts or lives.
* Energy systems.
* Streak guilt messaging.
* Flashing notifications.
* Reward animations designed to create excessive stimulation.
* Competitive leaderboards.
* Forced social features.

### Progress Without Punishment

Points are permanent.

Breaking a streak does **not** remove previously earned points. Streaks represent consistency rather than a penalty system.

A user who misses several days can return without losing their historical progress.

### Short Sessions

The intended daily interaction is approximately:

```text
Synonyms      → 10 questions
Antonyms      → 10 questions
Idioms        → 10 questions
                    ↓
              30 questions
                    ↓
             ~5–10 minutes
```

The application is designed around deliberate retrieval practice rather than prolonged daily engagement.

---

## 12. Revision System

Revision uses previously encountered questions rather than introducing unseen material.

Questions are prioritized according to:

| Priority           | Weight | Purpose                                                     |
| :----------------- | -----: | :---------------------------------------------------------- |
| **Incorrect**      |    40% | Reinforce questions previously answered incorrectly         |
| **Due for Review** |    30% | Surface questions according to review timing                |
| **Low Accuracy**   |    20% | Focus on questions with accuracy below 60%                  |
| **Random Seen**    |    10% | Provide general exposure to previously encountered material |

Revision questions are selected only from questions the user has already encountered.

### Revision Rewards

Each correct revision answer awards:

```text
+2 points
```

The daily revision bonus is capped at:

```text
20 points/day
```

Therefore, revision cannot become a replacement for the primary daily quiz scoring system.

---

## 13. Streak System

Vocab+ maintains four independent streak types:

1. **Synonyms Streak**
2. **Antonyms Streak**
3. **Idioms Streak**
4. **Giga Streak**

### Section Streak

A section requires a perfect score:

```text
10 / 10 → +1 streak
9 / 10  → streak reset
8 / 10  → streak reset
...
0 / 10  → streak reset
```

### Giga Streak

The Giga Streak requires perfection across the complete daily challenge:

```text
30 / 30 → +1 Giga Streak
29 / 30 → reset
...
0 / 30  → reset
```

### Lifetime Best

Current streaks and lifetime-best streaks are stored independently.

Breaking a current streak therefore never destroys the historical best.

---

## 14. Persistence Model

The application separates permanent user statistics from daily activity and question-level history.

### User Statistics

Stores:

* Total permanent points.
* Current section streaks.
* Best section streaks.
* Current Giga Streak.
* Best Giga Streak.

### Daily Answers

Stores:

* Calendar date.
* Question ID.
* Category.
* Selected answer.
* Correctness.
* Points awarded.
* Answer timestamp.

The unique date/question constraint guarantees idempotency.

### Question History

Stores exposure and retrieval information used by the revision engine.

This enables the application to determine:

* How often a question has been seen.
* How often it was answered correctly.
* How often it was answered incorrectly.
* Accuracy.
* Review timing.
* Revision priority.

---

## 15. Offline-First Design

The core application requires no active network connection.

### Bundled Content

Question banks are packaged directly into the application:

```text
assets/questions/
├── synonyms.json
├── antonyms.json
└── idioms.json
```

### Local Data

User progress is stored locally using:

```text
Room
  ↓
SQLite
```

User preferences are stored using:

```text
DataStore Preferences
```

Background reminders use:

```text
WorkManager
```

Therefore, the core experience remains functional when:

* Wi-Fi is unavailable.
* Mobile data is unavailable.
* The user is traveling.
* The application is used offline for extended periods.

---

## 16. Accessibility

Accessibility is treated as a core product requirement rather than an optional enhancement.

The application supports:

* Scalable typography.
* Semantic Compose UI labels.
* Screen-reader-friendly controls.
* Minimum 48dp interactive touch targets.
* Color-independent status communication.
* Readable contrast.
* Clear question and answer hierarchy.
* Non-color-dependent correctness indicators.

The low-stimulation design also avoids relying on animation, sound, or flashing visual effects to communicate important information.

---

## 17. Security & Data Principles

Vocab+ does not require a backend account for its core functionality.

User progress is stored locally on the device.

The application should avoid storing unnecessary sensitive information and should not require personal information to perform vocabulary quizzes or revision.

Local application data should be treated as user-owned application state and reset only after explicit confirmation.

---

## 18. Release Build

The application supports standard Android debug and release workflows.

### Debug

```bash
./gradlew assembleDebug
```

### Release APK

```bash
./gradlew assembleRelease
```

### Google Play App Bundle

```bash
./gradlew bundleRelease
```

Release builds are intended to use R8 shrinking and optimization according to the configured ProGuard rules.

Before publishing, the release artifact must be signed using the appropriate Android release keystore.

---

## 19. Development Workflow

A typical development cycle is:

```text
Edit Kotlin / Compose code
          ↓
Run unit tests
          ↓
./gradlew test
          ↓
Build application
          ↓
./gradlew assembleDebug
          ↓
Run on Android device/emulator
          ↓
Verify UI & behavior
          ↓
Run final tests
          ↓
Commit changes
```

### Recommended Verification

Before committing significant changes:

```bash
./gradlew test
./gradlew assembleDebug
```

A successful build and passing test suite should be maintained throughout development.

---

## 20. Project Status

Vocab+ is structured as a production-oriented Android application with a focus on:

* Maintainable architecture.
* Deterministic behavior.
* Offline reliability.
* Persistent progress.
* Idempotent data operations.
* Automated testing.
* Accessibility.
* Low-stimulation interaction design.
* Long-term maintainability.

The project uses the Gradle Wrapper, allowing development and CI environments to use the exact Gradle version defined by the project.

---

## 21. License

This project is licensed under the **MIT License** — see the `LICENSE` file for details.
