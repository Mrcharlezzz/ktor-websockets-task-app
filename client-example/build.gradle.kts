plugins {
    kotlin("jvm") version "2.2.21"
    application
    kotlin("plugin.serialization") version "2.2.21"

}

application {
    mainClass.set("com.example.MainKt")  // 👈 tells Gradle where `main()` is
}


group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)

    testImplementation(kotlin("test"))
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

