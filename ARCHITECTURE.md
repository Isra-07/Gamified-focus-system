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

### Complete Class Diagram (Detailed)
The following detailed class diagram presents the complete structural view of the Gamified Focus System, including all attributes, methods, and relationships between core entities. It covers the following main components:

| Component | Responsibility |
|---|---|
| **Level** | Defines rank tiers, requirements, and progression tracking |
| **Leaderboard** | Manages weekly rankings, top players, and user positions |
| **Rank** | Handles rank types (Bronze, Silver, Gold, etc.), promotion/demotion logic, and badge management |
| **TimerController** | Orchestrates focus session countdown, emits ticks, and manages session state |
| **FocusSession** | Represents a single study session with duration, completion status, and distraction tracking |
| **AnalyticsReport** | Aggregates usage statistics, session data, and generates weekly performance metrics |

<img width="2815" height="2063" alt="Complete Class Diagram (Detailed)" src="https://github.com/user-attachments/assets/1049696d-52d5-4dea-a18f-fd9309e5469b" /> 
*Figure 2: Complete class diagram showing all attributes, methods, and relationships*

### Strategy Pattern – Challenge Evaluation
The challenge system uses the **Strategy Pattern** to evaluate different types of challenges. This design allows new challenge types to be added without modifying existing code, satisfying the **Open/Closed Principle**.

#### Pattern Structure

| Component | Role |
|---|---|
| **Challenge** | Context class that holds challenge data and delegates evaluation |
| **ChallengeEvaluator** | Interface that defines the evaluation contract |
| **SessionCounterEvaluator** | Concrete strategy: counts total focus sessions |
| **ConsecutiveSessionsEvaluator** | Concrete strategy: tracks consecutive distraction-free sessions |
| **ReduceAppUsageEvaluator** | Concrete strategy: measures reduction in distracting app usage |

#### How It Works

1. Each `Challenge` contains a `ChallengeEvaluator` reference
2. The evaluator is injected at runtime based on the challenge type
3. `updateProgress()` delegates to the evaluator's logic
4. New challenge types only need to implement the interface, no changes to the `Challenge` class

<img width="2441" height="978" alt="Strategy Pattern (ChallengeEvaluator)" src="https://github.com/user-attachments/assets/60edfa9f-dc0a-45a4-bccd-fafebbbb3466" />
*Figure 3: Strategy Pattern implementation for Challenge Evaluation*

#### Benefits

- **Extensibility** – New evaluators can be added without modifying existing code
- **Testability** – Each evaluator can be unit-tested independently
- **Separation of Concerns** – Challenge data is separate from evaluation logic

## 6. Process Architecture
*(Madeleine - section in progress)*

## 7. Development Architecture
*(Ferdos - section in progress)*

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
| DAO | Data Access Object |
| UML | Unified Modeling Language |

### Definitions
| **Open/Closed Principle** | Classes should be open for extension but closed for modification |
| **ChallengeEvaluator** | Interface defining the contract for challenge evaluation |
| **Concrete Strategy** | Specific implementation of a Strategy interface |
| **Delegation** | One object passing a task to another object |

### Design Principles
*(Israa - section in progress)*
