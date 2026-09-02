# Vocab+ — Product & Software Requirements Specification

**Document Version:** 1.0
**Status:** MVP / Production Planning
**Platform:** Android initially
**Distribution:** Google Play Store
**Primary Goal:** Lightweight daily vocabulary-learning and revision application
**Core Categories:** Synonyms, Antonyms, Idioms

---

## 1. Product Overview

**Vocab+** is a lightweight mobile application designed for users who already possess basic English proficiency but want to improve their vocabulary, understanding of uncommon words, nuanced word meanings, synonyms, antonyms, and idiomatic expressions.

The application is **not intended to teach basic English**. It assumes approximately 10th-standard-level familiarity with English and focuses on vocabulary that is more challenging, uncommon, nuanced, or easily confused.

The primary interaction is a short daily quiz consisting of three sections:

1. **Synonyms**
2. **Antonyms**
3. **Idioms**

Each section contains **10 multiple-choice questions**, resulting in a maximum of **30 questions per day**.

Every question provides an explanation after the user answers, along with an example sentence where appropriate.

The application additionally maintains:

* permanent points
* individual section streaks
* a combined Giga Streak
* revision history
* previously encountered questions
* performance statistics

The application is intentionally designed around a **low-stimulation, calm, focused UX** rather than aggressive gamification.

---

# 2. Product Philosophy

Vocab+ should follow these principles throughout development.

### 2.1 Lightweight

The application should have a small footprint and minimal runtime resource consumption.

### 2.2 Low Stimulation

The interface should be visually calm and avoid excessive:

* animations
* colors
* sound effects
* flashing elements
* reward animations
* attention-grabbing UI
* notifications

### 2.3 Short Sessions

The intended daily session should take approximately **5–10 minutes**.

### 2.4 Difficult but Fair

Questions should challenge users who already know basic English.

The app should avoid elementary questions such as:

> Happy → Sad

unless the question is intentionally testing a more nuanced distinction.

### 2.5 Every Question Should Teach Something

A question is not considered complete merely because the user selected an answer.

The user should understand **why the answer is correct**.

### 2.6 Progress Should Be Permanent

Points earned by the user are never removed because of poor performance.

### 2.7 Streaks Represent Consistency

Streaks are intentionally breakable.

A user must achieve a perfect section to maintain its streak.

### 2.8 Revision Matters

The application should repeatedly expose users to previously encountered questions using a revision system.

---

# 3. Target Audience

The primary target user:

* already understands basic English
* wants to improve vocabulary
* enjoys quizzes
* may be preparing for competitive exams, interviews, academics, or professional communication
* wants a short daily activity
* does not necessarily want a full English-learning course

The application should **not** assume the user needs:

* alphabet/phonics lessons
* basic grammar lessons
* beginner vocabulary
* long lessons
* video courses

---

# 4. Core User Journey

```text
Install Vocab+
      │
      ▼
Open Application
      │
      ▼
Home Screen
      │
      ├───────────────┐
      ▼               ▼
Daily Quiz         Revision
      │
      ▼
Select Section
      │
      ▼
Question
      │
      ▼
Select Answer
      │
      ▼
Immediate Result
      │
      ▼
Explanation + Example
      │
      ▼
Next Question
      │
      ▼
Section Complete
      │
      ▼
Section Score
      │
      ▼
Repeat Other Sections
      │
      ▼
Daily Result
      │
      ├── Points
      ├── Section Streaks
      └── Giga Streak
```

---

# 5. Application Sections

The MVP contains the following primary areas:

```text
Home
├── Daily Synonyms
├── Daily Antonyms
├── Daily Idioms
├── Points
├── Section Streaks
├── Giga Streak
└── Revision

Quiz
├── Question
├── Options
├── Answer
├── Explanation
└── Example

Results
├── Section score
├── Points earned
└── Streak status

Revision
├── Previously seen questions
├── Mixed categories
└── Revision results

Statistics
├── Total points
├── Accuracy
├── Questions answered
├── Best scores
└── Longest streaks

Settings
├── Notifications
├── Appearance
├── Data
└── About
```

---

# 6. Daily Quiz System

Each calendar day has one daily quiz.

The daily quiz contains:

### Synonyms

10 questions.

### Antonyms

10 questions.

### Idioms

10 questions.

### Daily Total

**30 questions/day**

The user may complete the sections in any order.

For example:

```text
Synonyms → completed
        ↓
User leaves application
        ↓
Returns later
        ↓
Antonyms
        ↓
Idioms
```

The application must preserve progress.

---

# 7. Question Design

All MVP questions are multiple-choice questions.

Each question contains:

```text
Question
4 answer options
Correct answer
Explanation
Example sentence
Category
Difficulty
```

Example:

> Which word most nearly means **perfunctory**?

```text
A. Thorough
B. Superficial
C. Enthusiastic
D. Elaborate
```

Correct answer:

**B. Superficial**

Explanation:

> Perfunctory describes something done with little care, attention, or enthusiasm.

Example:

> He gave the report a perfunctory review.

---

# 8. Question Quality Requirements

Questions should generally satisfy the following requirements:

### 8.1 Avoid Overly Obvious Distractors

Bad:

```text
Abundant

A. Plenty
B. Banana
C. Car
D. Window
```

Good:

```text
Abundant

A. Scarce
B. Plentiful
C. Temporary
D. Fragile
```

Distractors should ideally be:

* semantically related
* plausible
* commonly confused
* grammatically appropriate

### 8.2 Avoid Ambiguity

There must be **one clearly defensible correct answer**.

Questions where multiple answers could reasonably be considered correct should not be published.

### 8.3 Prefer Nuance

Questions should test understanding rather than simple memorization where possible.

For example, the app can distinguish between words that are related but not exact equivalents.

---

# 9. Difficulty Model

Questions should have an internal difficulty rating.

Suggested levels:

| Level | Description |
| ----: | ----------- |
|     1 | Familiar    |
|     2 | Moderate    |
|     3 | Challenging |
|     4 | Difficult   |
|     5 | Advanced    |

The MVP does not necessarily need to expose these levels to users.

Difficulty can later be used by the question-selection algorithm.

---

# 10. Answer Flow

The question screen should operate as follows:

```text
Display question
      ↓
Display four options
      ↓
User selects option
      ↓
Disable additional selections
      ↓
Evaluate answer
      ↓
Record result
      ↓
Display correct answer
      ↓
Display explanation
      ↓
Display example
      ↓
Continue
```

The user should not need to navigate to another screen simply to discover whether they were correct.

---

# 11. Points System

Points represent **permanent progress**.

### MVP Rule

**Correct answer = 10 points**

**Incorrect answer = 0 points**

Therefore:

| Score | Points |
| ----: | -----: |
| 10/10 |    100 |
|  9/10 |     90 |
|  8/10 |     80 |
|  7/10 |     70 |
|  6/10 |     60 |
|  5/10 |     50 |
|  0/10 |      0 |

Points are accumulated across all daily activity.

### Critical Rule

**Points never decrease.**

If a user has:

> 12,840 points

and earns:

> 270 points

their new total is:

> 13,110 points

Even if all streaks are subsequently reset, the points remain.

---

# 12. Section Streak System

Each category maintains its own independent streak.

There are three section streaks:

```text
Synonym Streak
Antonym Streak
Idiom Streak
```

A section streak is maintained **only when the user achieves 10/10**.

If the user scores anything below 10/10, that section's streak resets.

### Example

Current streaks:

```text
Synonyms: 12
Antonyms: 7
Idioms: 4
```

Today's result:

```text
Synonyms: 10/10
Antonyms: 9/10
Idioms: 10/10
```

New state:

```text
Synonyms: 13
Antonyms: 0
Idioms: 5
```

Points earned:

**290**

No previously accumulated points are removed.

---

# 13. Giga Streak

The **Giga Streak** represents a perfect daily performance across all three sections.

Requirement:

```text
Synonyms = 10/10
Antonyms = 10/10
Idioms = 10/10
```

Therefore:

**30/30 = Giga Streak**

If any section is below 10/10:

**Giga Streak resets.**

Example:

```text
Synonyms: 10/10
Antonyms: 10/10
Idioms: 9/10
```

Result:

```text
290 points
Synonym streak continues
Antonym streak continues
Idiom streak resets
Giga streak resets
```

---

# 14. Longest Streaks

Current streaks are not the only values that should be stored.

The application should maintain:

```text
current_synonym_streak
best_synonym_streak

current_antonym_streak
best_antonym_streak

current_idiom_streak
best_idiom_streak

current_giga_streak
best_giga_streak
```

This ensures that achievements remain visible even after a current streak is broken.

Example:

```text
Current Giga Streak: 0
Best Giga Streak: 14
```

The user's previous achievement remains permanent.

---

# 15. Daily Completion State

Each section needs a completion state.

Suggested states:

```text
NOT_STARTED
IN_PROGRESS
COMPLETED
```

A section becomes `COMPLETED` after all 10 questions have been answered.

A daily quiz becomes completely finished when:

```text
Synonyms = COMPLETED
AND
Antonyms = COMPLETED
AND
Idioms = COMPLETED
```

---

# 16. Daily Quiz Determinism

Daily questions should not be purely random.

A specific daily quiz should be deterministically associated with a calendar date.

Example:

```text
2026-09-02
```

maps to:

```text
Synonym questions 1–10
Antonym questions 1–10
Idiom questions 1–10
```

This can be implemented through:

* server-generated daily quiz assignments, or
* a deterministic local selection algorithm.

Benefits:

* reproducibility
* easier debugging
* easier analytics
* easier content management
* consistent daily experience
* easier support

---

# 17. Question History

The application must track questions previously encountered by the user.

For every question, the application should maintain data such as:

```text
question_id
first_seen_at
last_seen_at
times_seen
times_correct
times_incorrect
last_result
next_revision_at
```

This history forms the foundation of the Revision system.

---

# 18. Revision System

Revision is a dedicated feature for previously encountered questions.

Unlike the Daily Quiz, Revision:

* mixes all three categories
* randomly orders questions
* uses questions the user has already seen
* prioritizes questions that require reinforcement

Example:

```text
Revision

Question 1 → Idiom
Question 2 → Synonym
Question 3 → Antonym
Question 4 → Synonym
Question 5 → Idiom
Question 6 → Antonym
...
```

Questions should be randomly jumbled so that the user cannot predict the category sequence.

---

# 19. Revision Eligibility

A question should not necessarily appear immediately after the user has answered it.

A simple initial revision model:

```text
First exposure
      ↓
Wait several days
      ↓
Question becomes eligible
      ↓
Revision
```

For example, a question may initially become eligible after **3 days**.

This can later evolve into a more sophisticated spaced-repetition algorithm.

---

# 20. Revision Prioritization

Revision should not simply select historical questions randomly.

The initial revision engine should prioritize:

1. Previously incorrect questions
2. Questions due for review
3. Questions with low historical accuracy
4. Older questions
5. Randomly selected previously seen questions

A possible initial weighting:

```text
40% previously incorrect
30% due for review
20% low-accuracy questions
10% random previously seen questions
```

The exact weighting can be adjusted based on actual usage data.

---

# 21. Revision Points

Revision should not become an infinite point-farming mechanism.

Recommended:

**Daily Quiz:**

```text
+10 points / correct answer
```

**Revision:**

```text
+2 points / correct answer
```

A daily revision point cap may also be introduced.

Example:

```text
Daily Quiz:
30 × 10 = 300 maximum points

Revision:
Maximum 20 bonus points/day
```

The primary progression therefore remains tied to the Daily Quiz.

---

# 22. Home Screen Requirements

The Home Screen is the most important screen in Vocab+.

It should immediately communicate:

* total points
* current Giga Streak
* three daily sections
* section streaks
* completion state
* Revision availability

Conceptual layout:

```text
                         Vocab+

                      12,840 points

                        ⚡ 7

                 ─────────────────

                         TODAY

                 Synonyms
                 10 questions
                 🔥 12 day streak

                 Antonyms
                 10 questions
                 🔥 9 day streak

                 Idioms
                 10 questions
                 🔥 4 day streak

                 ─────────────────

                       REVISION
                 24 questions ready

                   [ Start Revision ]
```

The interface should not overwhelm the user with statistics.

---

# 23. Visual Design System

Vocab+ should deliberately avoid the visual language of highly stimulating mobile games.

### Design Characteristics

* neutral or soft backgrounds
* restrained accent colors
* high-quality typography
* generous whitespace
* subtle borders
* subtle shadows
* minimal iconography
* restrained animation

### Avoid

* neon colors
* excessive gradients
* flashing UI
* excessive red/green
* huge reward banners
* animated streak counters
* full-screen celebrations
* excessive confetti
* constant sound effects
* attention-grabbing reward animations

The application should feel closer to a **calm reading/productivity application with quizzes** than a traditional mobile game.

---

# 24. Correct / Incorrect Presentation

Correct answers should be communicated clearly but calmly.

### Correct

```text
✓ Correct

Perfunctory

Done with minimal effort or care.

Example:
"He gave the report a
perfunctory review."

[ Continue ]
```

### Incorrect

```text
Not quite

Correct answer:
Superficial

Perfunctory means something done
with little care or enthusiasm.

Example:
"He gave the report a
perfunctory review."

[ Continue ]
```

The app should not make incorrect answers feel like a significant failure.

The objective is learning, not punishment.

---

# 25. Daily Result Screen

After completing a section:

```text
SYNONYMS COMPLETE

8 / 10

+80 points

🔥 Streak reset

Keep going.
```

After completing all three:

```text
TODAY COMPLETE

27 / 30

+270 points

Synonyms      10/10   🔥
Antonyms       9/10
Idioms         8/10

⚡ Giga Streak reset

Total points
13,110
```

If the user achieves a perfect day:

```text
TODAY COMPLETE

30 / 30

+300 points

🔥 All section streaks continue

⚡ GIGA STREAK: 8
```

Celebration should remain restrained.

---

# 26. Statistics

A simple Statistics screen can provide:

## Overall

```text
Total points
Questions answered
Overall accuracy
Perfect sections
Perfect days
```

## Streaks

```text
Current Synonym streak
Best Synonym streak

Current Antonym streak
Best Antonym streak

Current Idiom streak
Best Idiom streak

Current Giga streak
Best Giga streak
```

## Category Performance

```text
Synonyms       82%
Antonyms       76%
Idioms         69%
```

The purpose is to help users understand their performance without creating an unnecessarily complex dashboard.

---

# 27. Local-First Architecture

The application should be designed **offline-first**.

The initial question bank should be bundled with the application or stored locally after installation.

The user should be able to perform the core functionality without an internet connection:

* open the application
* take the Daily Quiz
* view explanations
* access Revision
* view progress
* maintain points
* maintain streaks

Internet connectivity can later be used for optional functionality such as:

* content updates
* cloud synchronization
* authentication
* analytics
* remote configuration
* optional leaderboards
* advertisements
* subscription verification

---

# 28. Suggested Technical Architecture

A reasonable Android implementation is:

```text
Presentation Layer
│
├── Home
├── Quiz
├── Result
├── Revision
├── Statistics
└── Settings
       │
       ▼
ViewModel / State Layer
       │
       ▼
Domain Layer
│
├── Quiz Engine
├── Scoring Engine
├── Streak Engine
├── Revision Engine
└── Daily Quiz Generator
       │
       ▼
Data Layer
│
├── Local Database
├── Question Repository
├── User Progress Repository
└── Optional Remote API
```

A modern Android implementation could use:

* Kotlin
* Jetpack Compose
* ViewModel
* Kotlin Coroutines
* Room
* DataStore
* Retrofit or Ktor Client if a backend is introduced

The exact technology stack can be finalized during implementation.

---

# 29. Local Database Model

A relational local database is appropriate for the MVP.

## `questions`

```text
id
category
prompt
option_a
option_b
option_c
option_d
correct_option
explanation
example_sentence
difficulty
content_version
```

## `question_history`

```text
id
question_id
first_seen_at
last_seen_at
times_seen
times_correct
times_incorrect
last_result
next_revision_at
```

## `daily_quiz`

```text
id
date
category
question_id
position
```

## `daily_answers`

```text
id
date
question_id
category
selected_option
is_correct
points_earned
answered_at
```

## `user_stats`

```text
total_points

synonym_current_streak
synonym_best_streak

antonym_current_streak
antonym_best_streak

idiom_current_streak
idiom_best_streak

giga_current_streak
giga_best_streak
```

---

# 30. Important Data Integrity Rules

Daily completion must be **idempotent**.

The application must not accidentally award points twice if:

* the app is restarted
* the user navigates backwards
* the device loses power
* the application crashes
* the same screen is reopened
* the answer screen is recreated

Example:

```text
Question 4 answered correctly
        ↓
+10 points
```

If the application restarts:

```text
Question 4 must NOT award another +10
```

Every daily answer should therefore have a unique identity, such as:

```text
date + question_id
```

for daily quiz purposes.

---

# 31. Streak Calculation Rules

Streak updates should occur only after a section is fully completed.

For each section:

```text
IF score == 10/10
    continue/increment current streak
ELSE
    current streak = 0
```

For Giga Streak:

```text
IF
    synonyms == 10/10
    AND antonyms == 10/10
    AND idioms == 10/10
THEN
    continue/increment Giga Streak
ELSE
    Giga Streak = 0
```

Best streak values must be updated before resetting or after incrementing the current streak as appropriate.

---

# 32. Date Handling

Daily streak logic should be based on the user's **local calendar date**, while timestamps should be stored consistently internally.

The implementation should correctly handle:

* midnight
* timezone changes
* daylight-saving changes where applicable
* offline usage
* returning to the app after multiple days

If cloud-backed competitive features are eventually introduced, additional safeguards against device-date manipulation should be implemented.

For a purely local MVP, complete anti-cheat protection is not necessary.

---

# 33. Content Management

The question bank should be treated as a first-class product asset.

During development, content can be stored in a structured format such as JSON or database seed data.

Example:

```json
{
  "id": "syn_0001",
  "category": "synonym",
  "prompt": "Which word most nearly means 'perfunctory'?",
  "options": [
    "Thorough",
    "Superficial",
    "Enthusiastic",
    "Elaborate"
  ],
  "correct_option": 1,
  "explanation": "Perfunctory describes something done with little care, attention, or enthusiasm.",
  "example": "He gave the report a perfunctory review.",
  "difficulty": 3
}
```

---

# 34. Initial Content Target

For Play Store launch, a reasonable target is approximately:

### Synonyms

300–400 questions

### Antonyms

300–400 questions

### Idioms

300–400 questions

### Total

**900–1,200 questions**

This provides a meaningful initial content library without requiring an enormous content-production effort.

The question bank should be manually reviewed before release.

---

# 35. Content Quality Pipeline

Every question should pass through:

```text
Draft
 ↓
Language Review
 ↓
Difficulty Review
 ↓
Distractor Review
 ↓
Example Sentence Review
 ↓
Duplicate Check
 ↓
Final Approval
 ↓
Published
```

Particular attention should be paid to:

* ambiguous questions
* multiple technically correct answers
* incorrect definitions
* unnatural example sentences
* regional usage
* outdated expressions
* overly easy questions

---

# 36. Notifications

Notifications should be optional.

The application should not rely on aggressive notification strategies.

Recommended maximum:

**One daily notification.**

Examples:

> Vocab+ is ready.

> Today's 30 questions are waiting.

> Can you get today's 30 right?

Avoid:

> Your 17-day streak is about to DIE!!!

The application should respect the user's attention and maintain the low-stimulation product philosophy.

---

# 37. Settings

MVP settings:

```text
Notifications
Appearance
Sound
Data / Reset Progress
About
Privacy
Terms
```

Appearance options can include:

```text
System
Light
Dark
```

Extensive customization is unnecessary for V1.

---

# 38. Accessibility

The application should support:

* scalable text
* sufficient contrast
* screen readers
* large touch targets
* no information conveyed exclusively through color
* reduced-motion considerations
* clear semantic labels

Correctness should never be communicated solely through:

```text
green = correct
red = incorrect
```

Text and/or icons should accompany color indicators.

---

# 39. Error Handling

Because the core application is local-first, most functionality should remain available without connectivity.

Potential failures should be handled gracefully.

### Database Failure

Display a useful error and prevent corrupted state.

### Content Corruption

Fall back to bundled content where possible.

### Network Unavailable

Continue offline.

### Content Update Failure

Keep the existing question bank.

### Interrupted Quiz

Restore the user's position.

The user should not lose progress due to a temporary application or network failure.

---

# 40. Security & Privacy

V1 should collect as little user data as possible.

### Preferred MVP Model

**No account required.**

Local data may include:

* points
* streaks
* answers
* revision history
* settings
* statistics

If analytics are introduced, only collect information necessary for product improvement.

If accounts and cloud synchronization are introduced later, the privacy model should be explicitly documented.

---

# 41. Analytics

Analytics should be minimal and product-focused.

Useful anonymous events include:

```text
app_opened
daily_quiz_started
question_answered
question_correct
question_incorrect
section_completed
daily_quiz_completed
revision_started
revision_completed
streak_continued
streak_broken
giga_streak_achieved
```

Useful product metrics:

```text
Daily Active Users
Daily Quiz Completion Rate
Average Questions per Day
Average Accuracy
Revision Usage
Day-1 Retention
Day-7 Retention
Day-30 Retention
```

Avoid collecting unnecessary personal or behavioral information.

---

# 42. MVP Scope

## Core

* [ ] Synonym quiz
* [ ] Antonym quiz
* [ ] Idiom quiz
* [ ] 10 questions per category
* [ ] 30 questions per day
* [ ] MCQ format
* [ ] Immediate answer feedback
* [ ] Explanations
* [ ] Example sentences

## Progress

* [ ] Permanent points
* [ ] Independent section streaks
* [ ] Giga Streak
* [ ] Best streaks
* [ ] Daily score
* [ ] Total points

## Revision

* [ ] Previously seen questions
* [ ] Mixed categories
* [ ] Randomized ordering
* [ ] Basic revision prioritization
* [ ] Delayed revision eligibility

## UX

* [ ] Calm visual design
* [ ] Offline functionality
* [ ] Progress persistence
* [ ] Optional notifications
* [ ] Dark/light/system appearance
* [ ] Accessibility support

---

# 43. Explicitly Out of Scope for V1

Do **not** implement the following in the initial release:

* social networking
* multiplayer
* friends
* public profiles
* leaderboards
* chat
* AI tutor
* voice recognition
* pronunciation analysis
* video lessons
* user-generated questions
* complex achievement systems
* XP currencies
* virtual coins
* avatars
* energy systems
* lives/hearts
* countdown timers
* aggressive gamification

These features can be reconsidered only after the core product proves useful.

---

# 44. Future Roadmap

## V1.0 — Core Vocab+

```text
3 daily categories
30 questions/day
Permanent points
Section streaks
Giga Streak
Revision
Offline functionality
Basic statistics
```

## V1.1 — Better Revision

```text
Spaced repetition
Weak-word prioritization
Personalized revision
Mastery tracking
Improved revision scheduling
```

## V1.2 — More Content

```text
One-word substitutions
Phrasal verbs
Commonly confused words
Homophones
Sentence completion
Vocabulary in context
```

## V1.3 — Cloud

```text
Optional account
Cloud backup
Cross-device synchronization
```

## V2 — Advanced Personalization

```text
Adaptive difficulty
Personalized daily quizzes
Vocabulary mastery
Advanced statistics
Custom practice
```

## Potential Later Features

```text
Word of the Day
Content packs
Exam-specific vocabulary
Achievements
Optional leaderboards
Premium subscription
```

All future features should be evaluated against the original low-stimulation philosophy before implementation.

---

# 45. Product Success Criteria

Vocab+ should not measure success purely by how long users remain inside the application.

The ideal outcome is:

```text
Open app
    ↓
Spend 5–10 useful minutes
    ↓
Learn something
    ↓
Complete or partially complete the daily challenge
    ↓
Leave the app
    ↓
Return tomorrow
```

Important metrics should therefore include:

* daily completion
* retention
* questions answered
* revision usage
* improvement in repeat-question accuracy
* streak continuation
* user-reported usefulness

Metrics such as the following should not become primary product goals:

* minutes spent per session
* number of screens visited
* number of notifications opened
* number of ads viewed

The product should **help users leave the app**, rather than keeping them inside it unnecessarily.

---

# 46. Definition of Done — Individual Question

A question is production-ready only when:

* [ ] Prompt is unambiguous
* [ ] Exactly one answer is clearly correct
* [ ] Distractors are plausible
* [ ] Definition is accurate
* [ ] Explanation is understandable
* [ ] Example sentence is grammatically correct
* [ ] Difficulty is appropriate
* [ ] Category is correct
* [ ] Question ID is unique
* [ ] Question has been reviewed
* [ ] Question can be displayed offline

---

# 47. Definition of Done — Daily Quiz

A Daily Quiz implementation is complete when:

* [ ] Exactly 10 questions are assigned per category
* [ ] Questions are deterministic for that date
* [ ] User can leave and resume
* [ ] Each answer is recorded exactly once
* [ ] Correct answers award points
* [ ] Incorrect answers award zero points
* [ ] Explanations are shown
* [ ] Example sentences are shown
* [ ] Section score is calculated
* [ ] Section streak is correctly continued/reset
* [ ] Giga Streak is correctly continued/reset
* [ ] Results survive application restart
* [ ] Offline operation works
* [ ] No duplicate points can be awarded

---

# 48. Core Business Rules

These rules are authoritative for the MVP.

## Daily Questions

```text
3 categories
10 questions/category
30 questions/day
```

## Points

```text
Correct answer = +10
Incorrect answer = +0
Points never decrease
```

## Section Streak

```text
10/10 = streak continues/increments
<10/10 = streak resets
```

## Giga Streak

```text
30/30 = streak continues/increments
<30/30 = streak resets
```

## Revision

```text
Previously seen questions only
All categories mixed
Randomized order
Delayed eligibility
Weighted toward weak/incorrect questions
```

## UX

```text
Low stimulation
Minimal animation
Minimal color usage
No aggressive gamification
Optional notifications
Offline-first
Progress persistence
```

---

# 49. Example Complete User Session

A user opens Vocab+.

The Home Screen shows:

```text
12,840 points

⚡ Giga Streak: 7

Synonyms       🔥 12
Antonyms       🔥 9
Idioms         🔥 4

Revision       24 ready
```

The user starts Synonyms.

They answer:

```text
10/10
```

They earn:

```text
+100 points
```

Their Synonym Streak changes:

```text
12 → 13
```

The user completes Antonyms:

```text
8/10
```

They earn:

```text
+80 points
```

Their Antonym Streak changes:

```text
9 → 0
```

The user completes Idioms:

```text
10/10
```

They earn:

```text
+100 points
```

Their Idiom Streak changes:

```text
4 → 5
```

Daily total:

```text
+280 points
```

Giga Streak:

```text
7 → 0
```

Total points:

```text
12,840 → 13,120
```

Nothing is lost.

Several days later, Revision becomes available for questions the user has previously encountered.

The user starts Revision:

```text
Perfunctory       Synonym
Break the ice     Idiom
Scarce            Antonym
Ubiquitous        Synonym
...
```

The questions are mixed and randomized.

Questions the user previously struggled with receive higher priority.

This creates the complete Vocab+ learning loop:

```text
Daily Challenge
      ↓
Answer
      ↓
Immediate Explanation
      ↓
Permanent Points
      ↓
Section Streak / Giga Streak
      ↓
Delayed Revision
      ↓
Retrieval Practice
      ↓
Improved Retention
      ↓
Next Daily Challenge
```

---

# 50. Final Product Definition

**Vocab+ is a lightweight, offline-first daily vocabulary challenge for users who already know basic English.**

Its fundamental product loop is:

> **Challenge → Answer → Understand → Earn → Maintain Streak → Revise → Remember**

The three daily sections provide the initial structure:

**Synonyms · Antonyms · Idioms**

The permanent point system provides long-term progress.

The three independent section streaks reward perfect performance within each category.

The **Giga Streak** rewards a perfect 30/30 day.

The Revision system converts the application from a simple quiz application into an actual **vocabulary-retention tool** by bringing previously encountered material back after an appropriate delay.

Most importantly, Vocab+ remains intentionally **small, calm, focused, and lightweight**.

The goal is not to maximize screen time.

The goal is for a user to spend a few useful minutes with Vocab+ every day, learn something new, reinforce what they previously learned, and gradually become noticeably better at English vocabulary.

---

# 51. MVP Implementation Summary

The first production release can therefore be reduced to the following system:

```text
                    ┌───────────────────┐
                    │      Vocab+       │
                    └─────────┬─────────┘
                              │
                    ┌─────────▼─────────┐
                    │    Home Screen    │
                    └─────────┬─────────┘
                              │
              ┌───────────────┼───────────────┐
              │               │               │
              ▼               ▼               ▼
         Synonyms         Antonyms          Idioms
           10 Q             10 Q             10 Q
              │               │               │
              └───────────────┼───────────────┘
                              ▼
                       Answer + Learn
                              │
              ┌───────────────┼───────────────┐
              ▼               ▼               ▼
           Points       Section Streak    Giga Streak
         Permanent      Perfect 10/10     Perfect 30/30
              │               │               │
              └───────────────┼───────────────┘
                              ▼
                       Question History
                              │
                              ▼
                          Revision
                              │
                    ┌─────────▼─────────┐
                    │ Mixed + Randomized│
                    │ Previously Seen   │
                    │ Weak Questions    │
                    └───────────────────┘
```

### Core MVP Numbers

| Feature                      | MVP Value |
| ---------------------------- | --------: |
| Daily categories             |         3 |
| Questions per category       |        10 |
| Daily questions              |        30 |
| Points per correct answer    |        10 |
| Maximum daily points         |       300 |
| Section streak requirement   |     10/10 |
| Giga Streak requirement      |     30/30 |
| Revision categories          |   3 mixed |
| Daily notification limit     |         1 |
| Initial question bank target | 900–1,200 |
| Account required             |        No |
| Core internet requirement    |        No |

**Vocab+ MVP = 30 carefully designed questions per day, permanent progress, breakable perfection streaks, and intelligent revision — presented through a calm and lightweight mobile experience.**
