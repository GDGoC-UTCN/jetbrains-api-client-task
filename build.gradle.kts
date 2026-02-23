plugins {
    kotlin("jvm") version "2.1.0"
    id("org.jetbrains.compose") version "1.7.3"
    kotlin("plugin.serialization") version "2.1.0"
    kotlin("plugin.compose") version "2.1.0"
}

group = "com.jetbrains.apiclient"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

val verificationFrameworkJar = layout.projectDirectory.file("lib/verification-framework.jar")

dependencies {
    implementation(files(verificationFrameworkJar))
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.compose.material3:material3-desktop:1.7.3")
    implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
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

// Separate source set for verification JAR to avoid Gradle 9 cycle (compileTestKotlin -> jar -> embedVerification -> verificationJar -> testClasses).
// Use Java API so we can set compileClasspath; Kotlin plugin will create compileVerificationKotlin.
sourceSets.create("verification") {
    kotlin.srcDir("src/test/kotlin")
    resources.srcDir("src/test/resources")
    compileClasspath += sourceSets.main.get().output + sourceSets.main.get().compileClasspath
    compileClasspath += configurations.testCompileClasspath.get()
    runtimeClasspath += output + compileClasspath + sourceSets.main.get().runtimeClasspath
    runtimeClasspath += configurations.testRuntimeClasspath.get()
}

tasks.test {
    useJUnitPlatform()
    dependsOn(tasks.named("embedVerification"))
}

val verificationJar by tasks.registering(Jar::class) {
    archiveBaseName.set("verification")
    from(sourceSets["verification"].output)
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
