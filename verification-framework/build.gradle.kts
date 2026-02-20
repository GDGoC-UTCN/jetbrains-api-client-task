plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
}

group = "com.jetbrains.apiclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.junit.platform:junit-platform-launcher:1.10.2")
    implementation("org.junit.jupiter:junit-jupiter:5.10.2")
    implementation("org.jetbrains.kotlin:kotlin-test:1.9.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

kotlin {
    jvmToolchain(17)
}

tasks.jar {
    archiveBaseName.set("verification-framework")
}
