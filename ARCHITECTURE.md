The Physical View describes the mapping of software components onto hardware nodes and the communication protocols between them. For the Gamified Focus System, the architecture follows a Client-Server model distributed across mobile devices and cloud infrastructure.

4.2.1 Deployment Diagram

The following diagram illustrates the distribution of the system across the physical nodes, highlighting the local persistence on the device and the optional cloud export.

### 4.2.1 Deployment Diagram

```mermaid
graph TD
    subgraph "User Device (Client Node)"
        A[Android Smartphone]
        B[(Local Room/SQLite DB)]
        A <-->|Local Data Persistence| B
    end

    subgraph "Cloud Infrastructure (Server Node)"
        C[Cloud Server / REST API]
        D[(Cloud Database)]
        C <-->|Data Management| D
    end
24    A -.->|HTTPS / JSON Export| C
25
26    ```
27

28    4.2.2 Hardware and Node Descriptions

Node Name	Category	Description
Android Device	Client	The physical smartphone used by the student. It hosts the Focus App APK, manages UI threads, and handles system services like UsageStatsManager.
Local Room DB	Storage	An internal SQLite-based database (/data/data/focus.db) that ensures core features work offline.
Cloud Server	Server	A remote endpoint that receives anonymized weekly reports via a REST API.
Cloud Database	Storage	A centralized database (Firebase/AWS) used for optional data backup and analytics export.
4.2.3 Communication Protocols

Inter-Process Communication (IPC): The app uses Binder IPC to communicate with Android System Services (e.g., querying the foreground app).

Client-to-Server: Communication is handled via HTTPS POST requests to the /api/export endpoint.

Data Format: The system utilizes JSON to transmit user IDs, weekly reports, and application version data.

4.2.4 Physical Constraints & Requirements

Operating System: Android OS with support for WorkManager and UsageStatsManager.

Hardware Specs: Minimum 100-200 MB RAM and 50+ MB Storage for the local database and cache.

Power Management: The system is designed to respect the Android Doze mode and utilizes AlarmManager for session recovery.