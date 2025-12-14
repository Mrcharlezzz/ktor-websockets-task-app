rootProject.name = "ktor-websockets-task-app"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":server")

include(":client-example")