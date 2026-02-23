# API Client Task Contest handout

This is the contest handout. Run the app, implement the required behavior, use **Check progress** to see aggregate test results, and **Submit solution** when ready.

# Prerequisites

- **JDK 17**  
  Install a JDK 17 (e.g. [Eclipse Temurin](https://adoptium.net/), [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)).

- **curl**  
  The app runs HTTP requests by calling `curl` on your system. It must be available on your **PATH**.
  - **macOS:** Usually pre-installed. If not: `brew install curl`.
  - **Windows:** Available on Windows 10+ (build 1803 or later), or install [curl for Windows](https://curl.se/windows/).

# Build & run

### macOS / Linux

From the project root:

```bash
# Run the application
./gradlew run

# Build 
./gradlew build

# Run tests
./gradlew test
```

### Windows

From the project root in **Command Prompt** or **PowerShell**:

```cmd
# Run the application
gradlew.bat run

# Build (compile, no distribution)
gradlew.bat build

# Run tests
gradlew.bat test
```

In PowerShell you can also use:

```powershell
.\gradlew.bat run
.\gradlew.bat build
.\gradlew.bat test
```

# Verify prerequisites

- **Java 17 or 21** (required for Gradle; the app then runs on any JVM 17+):
  ```bash
  java -version
  ```
  You should see version 17 or 21. If you see 22, 25, etc., Gradle 8.5 may fail — set `JAVA_HOME` to a JDK 17 or 21 installation when building.

- **curl:**
  ```bash
  curl --version
  ```

# Troubleshooting

- **Build fails with warnings about `native-platform` or "What went wrong" on JDK 22+ / 25**  
  Gradle 8.5 only supports running on JDK 17–21. Use JDK 17 or 21 to run the build:
  1. Install [Eclipse Temurin 17](https://adoptium.net/temurin/releases/?version=17) or 21.
  2. Point Gradle at it when building, e.g. in PowerShell:
     ```powershell
     $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot"   # adjust path
     .\gradlew.bat build
     ```
  Or set `JAVA_HOME` permanently in System Properties → Environment variables.

# Submit solution

In the status bar you can send your work to our server in one step:

1. Click **Submit solution**.
2. A dialog asks you to confirm and enter your name.
3. On confirm, the app **zips** the project and sends them over to us. 

Before submitting you can run an obfuscated test suite locally :)

# Contest mode info (dev only, pls guys remove)

The app can be used in a contest where participants see **only aggregate test results** (e.g. “execution: 5/8”, “helpers: 7/7”, “viewmodel: 12/15”). They do **not** see test code, test names, or test output.

Tests are compiled into a `verification.jar` and embedded in the app. The app runs them in-process and reports only totals via a custom JUnit listener.

**Preparing the handout (so participants cannot see or reverse-engineer tests):**

1. **Do not distribute test source.** Omit `src/test/` from the repo participants receive.
2. **Exclude verification-framework source, ship only the JAR.** The app depends on `lib/verification-framework.jar`. Run `./gradlew build` once (populates `lib/`), then remove the `verification-framework/` folder and `include("verification-framework")` from `settings.gradle.kts`. Distribute the rest including `lib/verification-framework.jar`. Participants get no verification-framework source.
3. **Distribute a pre-built app** that already contains the verification JAR (e.g. `./gradlew build` then ship the JAR or native distribution from `build/`). Then participants run the app and use “Check progress”. Participants can then run the app and use "Check progress" and "Submit solution".

I TRIED TO OBFUSCATE IT BUT IT MAKES READING JUNIT TEST RESULTS SUPER HARD :(((

