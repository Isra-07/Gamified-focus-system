## Layered Architecture

The system follows a Clean Architecture approach combined with the MVVM (Model-View-ViewModel) pattern, ensuring that dependencies point inward and inner layers remain independent of outer layers. This structure facilitates high testability and maintains a clear separation between business logic and framework-specific implementations.

*Why this matters for the Gamified Focus System:* This architecture ensures that the core focus mechanics, point calculations, and rank progression rules remain completely independent of the Android framework. If we ever need to migrate to a different platform or change the UI framework, the business logic, the heart of the gamification system, can be reused without modification. It also allows us to unit-test critical components like rank promotion logic and challenge evaluation without running an emulator or device.

### Presentation Layer
The Presentation Layer contains Jetpack Compose UI components such as LeaderboardScreen, ChallengesScreen, AnalyticsScreen, and TimerScreen. These screens are responsible for rendering the user interface and observing state changes from the ViewModel. To ensure a clean separation of concerns, these components contain no business logic and focus exclusively on UI logic.

### ViewModel Layer
The ViewModel Layer serves as the bridge between the Presentation and Domain layers. ViewModels including TimerViewModel, LeaderboardViewModel, ChallengesViewModel, and AnalyticsViewModel expose the UI state as StateFlow objects. They delegate business logic execution to specific use cases in the Domain layer, ensuring the UI remains reactive and decoupled from the underlying processing.

### Domain Layer
The Domain Layer is the core of the system and contains the business logic in pure Kotlin, free from framework dependencies. It houses entities such as User, FocusSession, Challenge, and Rank. This layer also defines use cases like StartSessionUseCase, CalculatePointsUseCase, and CheckRankPromotionUseCase. By utilizing the Strategy Pattern for challenge evaluation, this layer adheres to the Open/Closed Principle, allowing new challenge types to be added without modifying existing code.

### Repository Layer
The Repository Layer acts as a single source of truth and abstracts data sources from the business logic. Repositories such as UserRepository, SessionRepository, ChallengeRepository, AnalyticsRepository, and LeaderboardRepository are defined as interfaces within the Domain Layer. Their concrete implementations reside in the Data Layer, allowing the system to swap data sources without affecting core business rules.

### Data Layer
The Data Layer contains the concrete implementations of the repositories and manages data persistence. It includes Data Access Objects (DAOs) like UserDAO, SessionDAO, ChallengeDAO, LeaderboardDAO, and AnalyticsDAO. This layer uses the Room Database for local SQLite storage and manages the actual retrieval and storage of system data.

### External Services
The system integrates with several Android framework services to provide full functionality. This includes the UsageStatsManager for gathering device usage data, WorkManager for handling background tasks and periodic updates, and the NotificationManager for delivering alerts and focus reminders to the user.

### Dependency Flow
The architecture strictly enforces that dependencies point inward: Presentation → ViewModel → Domain ← Repository ← Data. Because the Domain Layer contains no knowledge of the outer layers, the core business logic remains framework-independent, highly testable, and easily maintainable.
<img width="1600" height="552" alt="WhatsApp Image 2026-04-17 at 2 20 36 PM" src="https://github.com/user-attachments/assets/4e18fbfa-3c32-42a3-9967-5e564d1099f8" />
*Figure 2: Layer Diagram (MVVM Layers) - Development*
