# Software Architecture: 4+1 View Model

## Development View (Implementation View)
[SWE332 Software Architecture Project]This section describes how the system is decomposed into modules and subsystems. [4+1 architectural view mode]

### 🏗️ Layered Architecture (MVVM)
The system uses a layered architecture to separate user interface, logic, and data.

#### UI Layer (Jetpack Compose)
* `TimerScreen`, `HomeScreen`, `ChallengesScreen`, `AnalyticsScreen`

#### ViewModel Layer
* `TimerViewModel`, `HomeViewModel`, `ChallengesViewModel`, `AnalyticsViewModel`

#### Repository Layer
* `SessionRepository`, `UserRepository`, `ChallengeRepository`, `AnalyticsRepository`

#### Domain Layer
* Entities: `FocusSession`, `User`, `Level`, `VirtualHome`, `Challenge`, `AppUsageStat`

#### Data Layer (Room)
* `AppDatabase`
* DAOs: `SessionDAO`, `UserDAO`, `ChallengeDAO`, `AnalyticsDAO`

#### External Services
* `UsageStatsManager`, `WorkManager`
