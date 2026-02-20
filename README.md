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

- **Java 17:**
  ```bash
  java -version
  ```
  You should see a 17.x version.

- **curl:**
  ```bash
  curl --version
  ```