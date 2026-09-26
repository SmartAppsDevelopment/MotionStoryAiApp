# MotionStory

> Turn your photos into beautiful stories.

**MotionStory** is a modern Android photo-to-video story maker and slideshow editor built natively with **Kotlin**, **Jetpack Compose**, and **Material 3**. It allows users to turn collections of photos into cinematic video stories with motion effects, transitions, background soundtracks, text overlays, and customizable export settings.

---

## 📌 Project Status

> **Current Stage: Phase 1 — UI & MVVM Architecture Complete**
>
> The complete application UI, end-to-end navigation, view models, UI states, and mock data repositories are fully implemented and verified. Native video encoding, real-time audio multiplexing, and image rendering pipelines are scheduled for the next development phase.

---

## 📱 Screenshots

| Home & Projects | Photo Selection | Video Editor |
| :---: | :---: | :---: |
| *[Add screenshot]* | *[Add screenshot]* | *[Add screenshot]* |

| Animations & Transitions | Photo Adjustments | Export & Video Ready |
| :---: | :---: | :---: |
| *[Add screenshot]* | *[Add screenshot]* | *[Add screenshot]* |

---

## ✨ Features

### 1. Project Management
- **Recent Projects Dashboard**: Display saved projects with thumbnail previews, photo counts, durations, and timestamps.
- **Project Actions**: Create new projects, duplicate existing projects, rename project titles, and delete projects.
- **Project Details Screen**: Dedicated overview screen with video hero preview, metadata chips, and quick edit/export actions.
- **Empty States**: Custom illustrated state prompting users to start their first project.

### 2. Photo Selection & Sequencing
- **Multi-Photo Selection**: Gallery grid with category filter tabs (*Photos*, *Albums*, *Recent*, *Favorites*).
- **Selection Tray**: Bottom-pinned preview bar displaying selected photos in real time with remove triggers.
- **Review Screen**: Step-by-step sequential inspection of selected items with timing information.
- **Rearrange Photos**: Dedicated reordering screen with drag handles and photo position indexing.

### 3. Video Story Editor
- **Timeline & Media Controls**: Interactive timeline scrubber, current timestamp vs. total duration indicator, and playback controls.
- **Motion Animations**: Sheet to configure photo animations (*None, Fade, Zoom In, Zoom Out, Ken Burns, Pan Left, Pan Right, Slide, Rotate, Bounce*).
- **Transitions**: Sheet to assign inter-photo transitions (*None, Fade, Dissolve, Slide, Push, Zoom, Blur, Wipe, Circle, Spin*) with duration sliders.
- **Duration Control**: Fine-tuned per-photo duration sliders (0.5s to 10s) with quick presets (1s, 2s, 3s, 5s) and a *"Apply to all"* toggle.
- **Music & Audio**: Music library bottom sheet with categorized playlists (*Recommended, My Music, Imported*), search, and a dedicated Audio Editor with waveform display and trim controls.

### 4. Photo & Text Editing
- **Photo Adjustments**: Interactive sliders for Brightness, Contrast, Saturation, Exposure, Highlights, Shadows, and Straightening.
- **Transform / Crop**: Preset aspect ratio selections (*Original, 9:16, 1:1, 16:9, 4:5, 3:4*).
- **Filters**: Visual filter chips (*Original, Vivid, Warm, Cool, Vintage, Cinematic, B&W, Dramatic*) with intensity adjustments.
- **Text Overlays**: Typography editor supporting custom fonts, text sizing, color swatches, background badges, and entrance animations.

### 5. Export & Sharing
- **Export Configuration**: Select Aspect Ratio (*9:16, 1:1, 16:9*), Resolution (*720p, 1080p, 4K*), Frame Rate (*24, 30, 60 FPS*), and Quality (*Standard, High, Maximum*) with dynamic estimated file size calculation.
- **Export Progress**: Animated render screen with real-time percentage counter and cancellation support.
- **Export Success**: Completion screen with video preview card, metadata chips, and actions (*Save to Gallery, Share Video, Edit Again*).

### 6. App Settings & Onboarding
- **Onboarding Carousel**: 3-step feature introduction with animated pager dots.
- **Settings**: Preference controls for theme selection, default photo durations, audio defaults, storage cache inspection, and clearing.

---

## 📊 Feature Implementation Matrix

| Component | Status | Details |
|---|:---:|---|
| **Design System & Theme** | ✅ Implemented | Complete dark theme palette, M3 typography, custom shapes, and reusable controls |
| **Navigation Graph** | ✅ Implemented | Jetpack Navigation Compose with sealed destination routes and arguments |
| **MVVM Architecture** | ✅ Implemented | ViewModels, immutable UiState, StateFlow, and Repository patterns across all screens |
| **UI Screens & Bottom Sheets** | ✅ Implemented | 12 major screens and 4 modal bottom sheets matching specifications |
| **Mock Repositories** | ✅ Implemented | In-memory CRUD operations for Projects, Photos, and Audio tracks |
| **Unit & JVM Robolectric Tests** | ✅ Implemented | Robolectric tests verifying resource loading and screen assertions |
| **Image Filters & Adjustments** | 🚧 In Progress | UI controls functional; GPU shader / RenderScript / Media3 filter integration pending |
| **Audio Multiplexing & Trimming** | 🚧 In Progress | Waveform UI & trim markers implemented; native audio decoder pending |
| **Video Rendering & MP4 Encoding** | 🚧 In Progress | Rendering flow & progress UI implemented; Media3 / MediaCodec pipeline pending |
| **Local Room Database** | 📋 Planned | Room dependencies configured; entity persistence to replace in-memory mocks |

---

## 🔄 App Flow

```text
Onboarding / Home Dashboard
  ├── Settings
  └── Create New Video / Open Project
        ↓
      Select Photos (Multi-select gallery & category tabs)
        ↓
      Review & Rearrange Photos (Sequence order & durations)
        ↓
      Main Video Editor
        ├── Animation Sheet (Entrance, Motion, Exit)
        ├── Transition Sheet (Dissolve, Blur, Spin, Wipe...)
        ├── Duration Sheet (Per-photo & global duration)
        ├── Music Library & Audio Editor (Waveform, trim, fade)
        ├── Photo Editor (Adjust, Transform / Crop, Filters)
        └── Text Editor (Typography, colors, overlays)
        ↓
      Full-Screen Preview
        ↓
      Export Settings (Aspect ratio, 1080p / 4K, FPS, bitrate)
        ↓
      Export Progress (Simulated frame rendering)
        ↓
      Video Ready (Save to gallery, share, re-edit)
```

---

## 🏛️ Architecture

MotionStory adheres to standard **Android Modern Architecture (MVVM)** principles:

```text
┌────────────────────────────────────────────────────────┐
│                   UI / Compose Layer                   │
│  (Screens, BottomSheets, Theme, Components, NavGraph)   │
└───────────────────────────▲────────────────────────────┘
                            │ Observes State (collectAsStateWithLifecycle)
                            │ Dispatches User Actions
┌───────────────────────────┴────────────────────────────┐
│                    ViewModel Layer                     │
│      (StateFlow<UiState>, viewModelScope, Coroutines)   │
└───────────────────────────▲────────────────────────────┘
                            │ Invokes Operations
                            │ Receives Reactive Flows
┌───────────────────────────┴────────────────────────────┐
│                    Repository Layer                    │
│   (ProjectRepository, PhotoRepository, MusicRepository)│
└───────────────────────────▲────────────────────────────┘
                            │ Fetches / Persists
┌───────────────────────────┴────────────────────────────┐
│                  Data Source Layer                     │
│    (In-memory / Fake Repositories ➔ Future Room / DB)  │
└────────────────────────────────────────────────────────┘
```

- **Unidirectional Data Flow (UDF)**: Composables only emit user intents and observe immutable `UiState` objects.
- **Coroutines & StateFlow**: Asynchronous actions are managed safely within `viewModelScope` using Kotlin `StateFlow`.
- **Decoupled Components**: Design primitives (`Buttons.kt`, `Cards.kt`, `SlidersAndControls.kt`, `TopBars.kt`) are modularized under `core/ui/components`.

---

## 📁 Project Structure

```text
MotionStory/
├── app/
│   ├── build.gradle.kts
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── core/
│   │   │   │   │   ├── navigation/
│   │   │   │   │   │   ├── NavGraph.kt
│   │   │   │   │   │   └── Screen.kt
│   │   │   │   │   ├── theme/
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   ├── Dimensions.kt
│   │   │   │   │   │   ├── Shapes.kt
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   └── Type.kt
│   │   │   │   │   └── ui/components/
│   │   │   │   │       ├── Buttons.kt
│   │   │   │   │       ├── Cards.kt
│   │   │   │   │       ├── SlidersAndControls.kt
│   │   │   │   │       └── TopBars.kt
│   │   │   │   ├── data/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── AnimationType.kt
│   │   │   │   │   │   ├── MusicTrack.kt
│   │   │   │   │   │   ├── PhotoItem.kt
│   │   │   │   │   │   └── Project.kt
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── MusicRepository.kt
│   │   │   │   │       ├── PhotoRepository.kt
│   │   │   │   │       └── ProjectRepository.kt
│   │   │   │   └── presentation/
│   │   │   │       ├── audio/              # EditAudioScreen & ViewModel
│   │   │   │       ├── editor/             # EditorScreen, ViewModel & sheets/
│   │   │   │       │   └── sheets/         # Animation, Duration, Music, Transition
│   │   │   │       ├── export/             # Settings, Progress, Success & ViewModel
│   │   │   │       ├── gallery/            # SelectPhotos, Review, Rearrange
│   │   │   │       ├── home/               # HomeScreen, ViewModel, UiState
│   │   │   │       ├── onboarding/         # OnboardingScreen & ViewModel
│   │   │   │       ├── photoeditor/        # PhotoEditorScreen, ViewModel, UiState
│   │   │   │       ├── preview/            # PreviewScreen & ViewModel
│   │   │   │       ├── project/            # ProjectDetailsScreen & ViewModel
│   │   │   │       ├── settings/           # SettingsScreen & ViewModel
│   │   │   │       └── texteditor/         # TextEditorScreen & ViewModel
│   │   │   └── res/
│   │   │       ├── drawable/               # Icons & sample photograph assets
│   │   │       ├── mipmap-.../             # Adaptive launcher icons
│   │   │       └── values/                 # strings.xml, colors.xml, themes.xml
│   │   └── test/java/com/example/
│   │       ├── ExampleUnitTest.kt
│   │       └── ExampleRobolectricTest.kt
│   └── proguard-rules.pro
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
└── metadata.json
```

---

## 🛠️ Tech Stack & Dependencies

| Category | Technology | Version | Purpose |
|---|---|:---:|---|
| **Language** | Kotlin | `2.2.10` | Primary development language |
| **UI Framework** | Jetpack Compose | BOM `2024.09.00` | Declarative UI toolkit |
| **Design System** | Material 3 | Compose M3 | Material Design 3 components & styling |
| **Architecture** | AndroidX Lifecycle | `2.8.7` | ViewModel & Compose lifecycle collection |
| **Navigation** | Navigation Compose | `2.8.9` | Single-activity screen navigation |
| **Concurrency** | Kotlinx Coroutines | `1.10.2` | Reactive StateFlow & background jobs |
| **Image Loading** | Coil Compose | `2.7.0` | Asynchronous image loading & memory caching |
| **Build System** | Android Gradle Plugin (AGP) | `9.1.1` | Gradle Kotlin DSL compilation |
| **Local Persistence** | Room (Configured via KSP) | `2.7.0` | Prepared for local project storage |
| **Unit Testing** | JUnit 4 & Robolectric | `4.13.2` / `4.16.1` | JVM unit tests & Android context testing |

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio**: Android Studio Meerkat (2024.3+) or higher recommended.
- **JDK**: Java 11 or higher.
- **Android SDK**: `minSdk = 24` (Android 7.0), `targetSdk = 36`, `compileSdk = 36`.

### Building and Running
1. Clone the repository:
   ```bash
   git clone https://github.com/[username]/MotionStory.git
   cd MotionStory
   ```
2. Open the project in Android Studio.
3. Sync Gradle dependencies:
   ```bash
   ./gradlew --refresh-dependencies
   ```
4. Run on an emulator or connected device:
   ```bash
   ./gradlew installDebug
   ```

### Running Tests
Execute the local unit and Robolectric test suite:
```bash
./gradlew testDebugUnitTest
```

---

## 🗺️ Roadmap

- [x] **Phase 1: UI & Architecture**
  - [x] Complete UI design system matching dark aesthetic.
  - [x] End-to-end screen navigation with backstack handling.
  - [x] ViewModel & StateFlow bindings with sample datasets.
- [ ] **Phase 2: Media Engine Integration**
  - [ ] Connect Android Photo Picker (`ActivityResultContracts.PickVisualMedia`) for real device photos.
  - [ ] Implement image manipulation engine (ColorMatrix filters, matrix transforms, cropping).
  - [ ] Integrate AndroidX Media3 / ExoPlayer for real-time video previewing.
- [ ] **Phase 3: Video Rendering & Export Pipeline**
  - [ ] Integrate hardware-accelerated video encoder (`MediaCodec` / Media3 Transformer).
  - [ ] Implement transition shaders (GLSL transitions: Dissolve, Wipe, Zoom, Blur).
  - [ ] Multiplex audio soundtrack with timing offset and volume fading.
- [ ] **Phase 4: Persistence & Cloud Sync**
  - [ ] Room database migration for project timeline persistence.
  - [ ] Optional cloud project backup.

---

## 🤝 Contributing

Contributions, feature proposals, and bug reports are welcome!

1. Fork the Project.
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the Branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## 📄 License

*[Add license]* (e.g., Apache 2.0 / MIT)
