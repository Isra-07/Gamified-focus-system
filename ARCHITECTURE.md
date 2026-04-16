# Thrive – Gamified Focus System – Architecture Documentation

## Change History

| Version | Date | Author | Description |
|---|---|---|---|
| 1.0 | 16/04/2026 | Israa | Initial architecture template |
| 1.1 | 16/04/2026 | Israa | Add Scope section |

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

### Architectural Overview

The architecture was designed around three core constraints specific to this system:

- **Offline-first**: All core features (timer, points, challenges, and analytics) 
  work without internet. Room provides local SQLite persistence so no network 
  call is ever required during a focus session. The only optional network 
  interaction is the analytics export to a cloud server, which requires 
  the user to explicitly opt in.

- **Thread safety**: The focus timer runs on the Default Dispatcher (background 
  thread) and emits countdown ticks via `Flow<Int>` to `TimerViewModel`. Database 
  writes go through the IO Dispatcher. The UI thread is never blocked, preventing 
  ANR errors during long sessions.

- **Extensibility via Strategy Pattern**: The challenge system uses a 
  `ChallengeEvaluator` interface so new challenge types (e.g., 
  `EarlyMorningEvaluator`) can be added without modifying the existing `Challenge` 
  class, satisfying the Open/Closed Principle.

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
*(Israa - section in progress)*

## 5. Logical Architecture
*(Israa - section in progress)*

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
*(Israa - section in progress)*

### Definitions
*(Israa - section in progress)*

### Design Principles
*(Israa - section in progress)*