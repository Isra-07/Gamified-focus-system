
# Thrive – Gamified Focus System – Architecture Documentation

## Change History

| Version | Date | Author | Description |
|---|---|---|---|
| 1.0 | 16/04/2026 | Israa | Initial architecture template |
| 2.0 | 16/04/2026 | Israa | Add Scope section |
| 3.0 | 16/04/2026 | Israa | Add References section |
| 4.0 | 16/04/2026 | Israa | Add Software Architecture section |
| 5.0 | 16/04/2026 | Israa | Add Architectural Goals & Constraints section |

## Table of Contents

- [1. Scope](#1-scope)
- [2. References](#2-references)
- [3. Software Architecture](#3-software-architecture)
- [4. Architectural Goals & Constraints](#4-architectural-goals--constraints)
- [5. Logical Architecture](#5-logical-architecture)
- [6. Process Architecture](#6-process-architecture)
- [7. Development Architecture](#7-development-architecture)
- [8. Physical Architecture](#8-physical-architecture)
- [9. Scenarios](#9-scenarios)
- [10. Size and Performance](#10-size-and-performance)
- [11. Quality](#11-quality)

## List of Figures

*To be completed after adding diagrams*

---

## 1. Scope
The Gamified Focus System is an Android mobile application designed to help students improve their focus and productivity through timed study sessions. The app gamifies the experience by awarding points for completing sessions, tracking distractions, allowing users to level up, and unlocking items for a customizable virtual home.

### Core Features

- Focus timer management
- Distraction detection 
- Points and leveling system
- Gamified challenge system
- Weekly analytics reports

### Out of Scope

The following features are **outside the scope** of this project:

- Social multiplayer
- Cloud sync across devices
- In-app purchases
- Real-time collaboration

### Project Goal

The goal is to keep the system focused, maintainable, and well-structured for an academic software engineering project.


## 2. References

1. Android Developers. (2026). *UsageStatsManager API Reference*. Retrieved from  
   [https://developer.android.com/reference/kotlin/android/app/usage/UsageStatsManager](https://developer.android.com/reference/kotlin/android/app/usage/UsageStatsManager)

2. Android Developers. (2025). *Jetpack Compose - Display Images*. Retrieved from  
   [https://developer.android.com/develop/ui/compose/quick-guides/collections/display-images](https://developer.android.com/develop/ui/compose/quick-guides/collections/display-images)

3. IEEE Computer Society. (2024). *Software Engineering Body of Knowledge (SWEBOK) v4.0*. Retrieved from  
   [https://sfia-online.org/en/tools-and-resources/bodies-of-knowledge/swebok-software-engineering-body-of-knowledge/swebok-summit-quicklinks](https://sfia-online.org/en/tools-and-resources/bodies-of-knowledge/swebok-software-engineering-body-of-knowledge/swebok-summit-quicklinks)

4. Deterding, S., Dixon, D., Khaled, R., & Nacke, L. (2011). *From game design elements to gamefulness: defining "gamification"*. Proceedings of MindTrek. Retrieved from  
   [https://scispace.com/papers/from-game-design-elements-to-gamefulness-defining-1wnas1kptm](https://scispace.com/papers/from-game-design-elements-to-gamefulness-defining-1wnas1kptm)

5. GitHub. (2024). *GitHub Flow*. Retrieved from  
   [https://docs.github.com/en/enterprise-server@3.8/get-started/using-github/github-flow](https://docs.github.com/en/enterprise-server@3.8/get-started/using-github/github-flow) 


## 3. Software Architecture

The architecture follows the **4+1 View Model**, and implements 
the **MVVM pattern** with clean architecture principles. This section provides 
an overview of architectural constraints, technology choices, and the 4+1 
views documented in Sections 5–9.


### Technology Stack

| Component | Technology | Reason |
|---|---|---|
| Language | Kotlin | First-class Android support, coroutines for async |
| UI Framework | Jetpack Compose | Declarative UI, direct StateFlow integration |
| Architecture Pattern | MVVM + Repository | Separates UI state from business logic and data |
| Local Database | Room | Type-safe SQLite with DAO abstraction over raw queries |
| Background Jobs | WorkManager | Survives process death and device restarts unlike AlarmManager |
| Distraction Detection | UsageStatsManager | Only Android API that provides foreground app data |
| Async | Kotlin Coroutines + Flow | Structured concurrency, reactive UI updates |
| Version Control | Git + GitHub | Team collaboration and documentation hosting |

### 4+1 View Model Documentation

This document follows the **4+1 architectural view model** to describe the system 
from five complementary perspectives. Each view addresses a different set of 
concerns and audience:

| View | What it answers | Diagrams | Section |
|---|---|---|---|
| **Logical View** | Classes and their relationships | Class diagram, Strategy pattern | 5 |
| **Process View** | Runtime behavior | State, Activity diagrams | 6 |
| **Development View** | Code organization | Layer, Package diagrams | 7 |
| **Physical View** | Deployment environment | Deployment diagram | 8 |
| **Scenarios (+1)** | Real use cases | Sequence diagrams | 9 |

## 4. Architectural Goals & Constraints

The architecture was designed around three core constraints specific to this system:

### 1. Offline-first

All core features (timer, points, challenges, and analytics) work without internet. Room provides local SQLite persistence so no network call is ever required during a focus session. The only optional network interaction is the analytics export to a cloud server, which requires the user to explicitly opt in.

### 2. Thread Safety

The focus timer runs on the Default Dispatcher (background thread) and emits countdown ticks via `Flow<Int>` to `TimerViewModel`. Database writes go through the IO Dispatcher. The UI thread is never blocked, preventing ANR errors during long sessions.

### 3. Extensibility via Strategy Pattern

The challenge system uses a `ChallengeEvaluator` interface so new challenge types (e.g., `EarlyMorningEvaluator`) can be added without modifying the existing `Challenge` class, satisfying the Open/Closed Principle.

## 5. Logical Architecture
The logical architecture describes the main functional components of the Gamified Focus System and how responsibilities are distributed between them. It focuses on major abstractions rather than implementation details.

The system follows a **layered architecture** with clear separation between presentation, business logic, and data persistence.

### Main Logical Components

#### Focus Session Management
Handles:
- starting, pausing, resuming, and stopping focus sessions
- tracking remaining time and session state
- detecting session completion
- managing session duration targets

This component controls the core timing mechanism that powers the focus experience.

#### Timer Controller
Handles:
- countdown execution
- tick events (per time unit)
- session completion triggers
- running state management

Acts as the orchestration engine for active focus sessions.

#### Session Persistence
Handles:
- storing completed session records
- tracking actual vs. target duration
- linking sessions to users
- generating session events for analytics

Every focus session is recorded for history, reporting, and point calculation.

#### Event & Usage Tracking
Handles:
- capturing app usage during focus sessions
- identifying distracting applications
- recording usage duration per app
- timestamping all events
- formatting dates for display

This component enables distraction analysis and behavioral insights.

#### Analytics & Reporting
Handles:
- aggregating usage statistics
- generating weekly reports
- calculating completion rates
- identifying top distracting apps
- computing average session length
- determining longest focus streaks

Reports provide users with visibility into their focus habits.

#### Points & Gamification
Handles:
- calculating points per completed session
- applying bonus multipliers based on streaks
- computing challenge completion points
- calculating weekly point totals
- checking rank promotion eligibility
- applying streak-based bonuses

Points are the primary reward mechanism driving user engagement.

#### User Progression
Handles:
- storing user profile and state
- tracking total and weekly points
- managing current rank
- promoting and demoting ranks
- calculating progress to next rank
- retrieving global rank position

User state evolves over time based on focus performance.

#### Rank Management
Handles:
- defining rank tiers (Bronze, Silver, Gold, Platinum, Diamond)
- setting minimum points per rank
- determining promotion and demotion thresholds
- providing rank-specific badges and colors
- calculating progress percentage within a rank

Ranks provide clear progression goals and social status.

#### Level Management
Handles:
- defining level requirements
- checking level unlock status
- providing level titles and descriptions
- calculating progress to next level
- awarding level completion rewards

Levels complement ranks with a linear, always-forward progression path.

#### Leaderboard & Competition
Handles:
- maintaining weekly leaderboard entries
- refreshing leaderboard data
- retrieving top users by score
- calculating user rank position
- scoring weekly performance
- promoting top performers

Leaderboards drive social competition and recurring engagement.

#### Persistence Layer
Stores all structured system data using a relational database with normalized tables:

- `users` — account and progression data
- `focus_sessions` — completed session records
- `app_usage_stats` — per-session application usage
- `events` — raw event dispatch data
- `leaderboard_entries` — weekly rankings
- `rank_definitions` — rank tier configuration

### Architecture Style

The logical architecture supports **separation of concerns** and **reduces duplication** between components. Each component has a single, well-defined responsibility:

| Component | Responsibility |
|-----------|----------------|
| Focus Session Management | Timing and state |
| Session Persistence | Data storage |
| Event Tracking | Usage capture |
| Analytics | Insight generation |
| Points & Gamification | Reward calculation |
| User Progression | State management |
| Rank/Level Management | Progression rules |
| Leaderboard | Competition |

### Class Diagram

The following layered class diagram presents the main structural elements of the Gamified Focus System and the relationships between controllers, services, models, entities, and utility components. It shows how responsibilities are separated across the backend architecture and how the major system components interact. 
<img width="2485" height="1610" alt="class-diagram-overview" src="https://github.com/user-attachments/assets/d8671d12-76b5-4f47-ac39-a479ed2190a9" /> 
*Figure 1: Class Diagram (Overview)*

## 6. Process Architecture
*(Madeleine - section in progress)*

## 7. Development Architecture

### Layered Architecture

The system follows a Clean Architecture approach combined with the MVVM (Model-View-ViewModel) pattern, ensuring that dependencies point inward and inner layers remain independent of outer layers. This structure facilitates high testability and maintains a clear separation between business logic and framework-specific implementations.

*Why this matters for the Gamified Focus System:* This architecture ensures that the core focus mechanics, point calculations, and rank progression rules remain completely independent of the Android framework. If we ever need to migrate to a different platform or change the UI framework, the business logic, the heart of the gamification system, can be reused without modification. It also allows us to unit-test critical components like rank promotion logic and challenge evaluation without running an emulator or device.

#### Presentation Layer
The Presentation Layer contains Jetpack Compose UI components such as LeaderboardScreen, ChallengesScreen, AnalyticsScreen, and TimerScreen. These screens are responsible for rendering the user interface and observing state changes from the ViewModel. To ensure a clean separation of concerns, these components contain no business logic and focus exclusively on UI logic.

#### ViewModel Layer
The ViewModel Layer serves as the bridge between the Presentation and Domain layers. ViewModels including TimerViewModel, LeaderboardViewModel, ChallengesViewModel, and AnalyticsViewModel expose the UI state as StateFlow objects. They delegate business logic execution to specific use cases in the Domain layer, ensuring the UI remains reactive and decoupled from the underlying processing.

#### Domain Layer
The Domain Layer is the core of the system and contains the business logic in pure Kotlin, free from framework dependencies. It houses entities such as User, FocusSession, Challenge, and Rank. This layer also defines use cases like StartSessionUseCase, CalculatePointsUseCase, and CheckRankPromotionUseCase. By utilizing the Strategy Pattern for challenge evaluation, this layer adheres to the Open/Closed Principle, allowing new challenge types to be added without modifying existing code.

#### Repository Layer
The Repository Layer acts as a single source of truth and abstracts data sources from the business logic. Repositories such as UserRepository, SessionRepository, ChallengeRepository, AnalyticsRepository, and LeaderboardRepository are defined as interfaces within the Domain Layer. Their concrete implementations reside in the Data Layer, allowing the system to swap data sources without affecting core business rules.

#### Data Layer
The Data Layer contains the concrete implementations of the repositories and manages data persistence. It includes Data Access Objects (DAOs) like UserDAO, SessionDAO, ChallengeDAO, LeaderboardDAO, and AnalyticsDAO. This layer uses the Room Database for local SQLite storage and manages the actual retrieval and storage of system data.

#### External Services
The system integrates with several Android framework services to provide full functionality. This includes the UsageStatsManager for gathering device usage data, WorkManager for handling background tasks and periodic updates, and the NotificationManager for delivering alerts and focus reminders to the user.

#### Dependency Flow
The architecture strictly enforces that dependencies point inward: Presentation → ViewModel → Domain ← Repository ← Data. Because the Domain Layer contains no knowledge of the outer layers, the core business logic remains framework-independent, highly testable, and easily maintainable.

![Layer Diagram](https://github.com/user-attachments/assets/4e18fbfa-3c32-42a3-9967-5e564d1099f8)

*Figure 2: Layer Diagram (MVVM Layers) - Development*

## 8. Physical Architecture
*(Fariha - section in progress)*

## 9. Scenarios
*(Eyis - section in progress)*

## 10. Size and Performance
*(Israa - section in progress)*

## 11. Quality
*(Israa - section in progress)*

## Appendices

### Acronyms and Abbreviations
*(Israa - section in progress)*

### Definitions
*(Israa - section in progress)*

### Design Principles
*(Israa - section in progress)*

