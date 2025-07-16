# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Android application called "Switcher" built with Kotlin and Jetpack Compose. It follows standard Android project structure with Gradle Kotlin DSL build system.

## Build System & Development Commands

### Build Commands
- `./gradlew build` - Build the entire project
- `./gradlew assembleDebug` - Build debug APK
- `./gradlew assembleRelease` - Build release APK
- `./gradlew installDebug` - Install debug APK on connected device/emulator

### Testing Commands
- `./gradlew test` - Run unit tests
- `./gradlew testDebugUnitTest` - Run debug unit tests specifically
- `./gradlew connectedAndroidTest` - Run instrumented tests on connected device
- `./gradlew connectedDebugAndroidTest` - Run debug instrumented tests

### Development Commands
- `./gradlew clean` - Clean build outputs
- `./gradlew lint` - Run lint checks
- `./gradlew lintDebug` - Run lint on debug variant

## Architecture

### Technology Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Build System**: Gradle with Kotlin DSL
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Java Version**: 11

### Project Structure
```
app/src/main/java/com/deerhunter/switcher/
├── MainActivity.kt - Main activity with Compose UI
└── ui/theme/ - Theme definitions (colors, typography, themes)
```

### Key Components
- **MainActivity**: Single activity hosting Compose UI with edge-to-edge design
- **SwitcherTheme**: Custom Material 3 theme with dynamic color support (Android 12+)
- **Package**: `com.deerhunter.switcher`

### Dependencies Management
- Uses Gradle version catalog (`gradle/libs.versions.toml`) for dependency management
- Key dependencies: AndroidX Core, Lifecycle, Activity Compose, Material 3, Compose BOM
- **Ktor 3.2.0**: HTTP client library with Android engine, content negotiation, JSON serialization, and logging
- **Hilt 2.56.2**: Dependency injection framework built on Dagger, uses KSP for annotation processing

### Testing Structure
- Unit tests: `app/src/test/java/com/deerhunter/switcher/`
- Instrumented tests: `app/src/androidTest/java/com/deerhunter/switcher/`
- Uses JUnit 4 for unit tests and AndroidJUnit4 for instrumented tests

## Development Notes

### Theme System
- Supports both light and dark themes
- Dynamic color theming available on Android 12+
- Custom color schemes defined in `ui/theme/Color.kt`

### Compose Setup
- Uses Compose BOM for version alignment
- Includes UI tooling for previews and debugging
- Material 3 design system implementation

### Dependency Injection
- **Hilt**: Modern DI framework for Android
- Uses KSP instead of KAPT for faster annotation processing
- Provides automatic component generation and scoping
- Integrates seamlessly with ViewModels and Compose

### Network Layer
- **NetworkModule**: Hilt module providing configured HttpClient
- **Ktor Client**: Configured for POST requests to http://hello.com
- **Basic Authentication**: Supports username:password@hello.com format
- **JSON Serialization**: Uses kotlinx.serialization for request/response handling
- **Logging**: Configured with request/response logging for debugging