plugins {
    kotlin("jvm") version "1.9.20"
    id("org.jetbrains.compose") version "1.5.11"
    kotlin("plugin.serialization") version "1.9.20"
}

group = "com.jetbrains.apiclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

val verificationFrameworkJar = layout.projectDirectory.file("lib/verification-framework.jar")

dependencies {
    implementation(files(verificationFrameworkJar))
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.compose.material3:material3-desktop:1.5.11")
    implementation("org.jetbrains.compose.material:material-icons-extended:1.5.11")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.junit.platform:junit-platform-launcher:1.10.2")
    implementation("org.junit.jupiter:junit-jupiter:5.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

compose.desktop {
    application {
        mainClass = "com.jetbrains.apiclient.MainKt"
        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
            )
            packageName = "API Client"
            packageVersion = "1.0.0"
        }
    }
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
    dependsOn(tasks.named("embedVerification"))
}

val verificationJar by tasks.registering(Jar::class) {
    archiveBaseName.set("verification")
    from(sourceSets.test.get().output)
    exclude("**/module-info.class")
}

val embedVerification by tasks.registering(Copy::class) {
    dependsOn(tasks.processResources, verificationJar)
    from(verificationJar.get().archiveFile)
    into(layout.buildDirectory.dir("resources/main/verification"))
    rename { "verification.jar" }
}

tasks.jar {
    dependsOn(embedVerification)
}

if (findProject(":verification-framework") != null) {
    val prepareVerificationFrameworkJar by tasks.registering(Copy::class) {
        dependsOn(":verification-framework:jar")
        from(project(":verification-framework").tasks.jar.get().archiveFile)
        into(layout.projectDirectory.dir("lib"))
        rename { "verification-framework.jar" }
    }
    tasks.compileKotlin {
        dependsOn(prepareVerificationFrameworkJar)
    }
}

afterEvaluate {
    tasks.findByName("run")?.dependsOn(embedVerification)
}
