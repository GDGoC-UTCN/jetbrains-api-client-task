rootProject.name = "api-client"
include("verification-framework")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}
