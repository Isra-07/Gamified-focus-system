pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Thrive"
include(":app")
include(":core:ui")
include(":data")
include(":domain")
include(":feature:auth")
include(":feature:home")
include(":feature:timer")
include(":feature:summary")
include(":feature:friends")
