# Damas Kitchen CAD & Photorealistic Studio — Architecture & File Manifest

## Layering (Clean MVVM + Unidirectional StateFlow)
- **Domain Layer (`com.damaskitchen.studio.domain`)**
  - `model/KitchenDimensions.kt`: Metric (cm) coordinates, wall segments, clearances, and snap-grid constraints.
  - `model/CabinetModule.kt`: Structural kitchen modules (Tall Glass Vitrine Tower, Decorative Lit Niche Pillar, Two-Tier Ceiling-Flush Uppers, Louvered Doors, CNC Hood Valance, Fluted Island Bar, Integrated Appliances).
  - `model/MaterialPreset.kt`: PBR material descriptors (Damas Duo-Tone, Rose-Cappuccino High-Gloss, Anthracite Luxury, Classic Aluminum Slats, Champagne Gold CNC) + countertops, backsplashes, flooring, and ceiling lighting.
  - `model/KitchenProject.kt`: Immutable project state, layout type (L-Shape, U-Shape, Galley + Island, Straight), room dimensions, placed modules, and active style preset.
  - `catalog/DamasCatalog.kt`: Hardcoded architectural module & material specifications.
  - `layout/LayoutTemplateEngine.kt`: Magnetic snapping in centimeters and parametric L-Shape / U-Shape / Island generator.
  - `render/RenderPromptBuilder.kt`: Deterministic photorealistic prompt generator using the architectural specification template.
- **Data Layer (`com.damaskitchen.studio.data`)**
  - `render/ElevationSnapshotGenerator.kt`: Generates elevation color bitmap + Canny edge/depth guidance bitmap for ControlNet.
  - `render/RealisticRenderEngine.kt`: Async engine combining Gemini Vision / Stable Diffusion ControlNet REST API with an offline high-detail PBR ray-traced fallback preview.
- **Presentation Layer (`com.damaskitchen.studio.presentation`)**
  - `viewmodel/KitchenStudioViewModel.kt`: Central `StateFlow<KitchenStudioUiState>` coordinator.
  - `cad/CadGeometry.kt`: Cm-to-pixel transforms, magnetic snap math, and hit-testing.
  - `cad/KitchenCanvasRenderer.kt`: 2D architectural plan & interactive isometric/elevation 3D preview painter.
  - `cad/KitchenCanvasScreen.kt`: Pan/zoom (`detectTransformGestures`), module palette, drag-and-drop placement, and template switcher.
  - `style/StyleStudioSheet.kt`: One-tap 5 core reference presets & custom finish overrides.
  - `style/ModuleInspectorSheet.kt`: Per-module width/height/depth, door style, and LED warmth controls.
  - `render/RenderStudioScreen.kt`: Prompt preview, ControlNet strength controls, API key config, and photorealistic output viewer.
  - `theme/DamasTheme.kt`: Luxury Middle-Eastern architectural Material 3 palette.

## Complete File Manifest
1. `gradle/libs.versions.toml`
2. `gradle/wrapper/gradle-wrapper.properties`
3. `gradle.properties`
4. `settings.gradle.kts`
5. `build.gradle.kts`
6. `app/build.gradle.kts`
7. `app/proguard-rules.pro`
8. `app/src/main/AndroidManifest.xml`
9. `.github/workflows/android-build.yml`
10. `app/src/main/java/com/damaskitchen/studio/domain/model/KitchenDimensions.kt`
11. `app/src/main/java/com/damaskitchen/studio/domain/model/CabinetModule.kt`
12. `app/src/main/java/com/damaskitchen/studio/domain/model/MaterialPreset.kt`
13. `app/src/main/java/com/damaskitchen/studio/domain/model/KitchenProject.kt`
14. `app/src/main/java/com/damaskitchen/studio/domain/catalog/DamasCatalog.kt`
15. `app/src/main/java/com/damaskitchen/studio/domain/layout/LayoutTemplateEngine.kt`
16. `app/src/main/java/com/damaskitchen/studio/presentation/viewmodel/KitchenStudioViewModel.kt`
17. `app/src/main/java/com/damaskitchen/studio/presentation/cad/CadGeometry.kt`
18. `app/src/main/java/com/damaskitchen/studio/presentation/cad/KitchenCanvasRenderer.kt`
19. `app/src/main/java/com/damaskitchen/studio/presentation/cad/KitchenCanvasScreen.kt`
20. `app/src/main/java/com/damaskitchen/studio/presentation/style/StyleStudioSheet.kt`
21. `app/src/main/java/com/damaskitchen/studio/presentation/style/ModuleInspectorSheet.kt`
22. `app/src/main/java/com/damaskitchen/studio/domain/render/RenderPromptBuilder.kt`
23. `app/src/main/java/com/damaskitchen/studio/data/render/ElevationSnapshotGenerator.kt`
24. `app/src/main/java/com/damaskitchen/studio/data/render/RealisticRenderEngine.kt`
25. `app/src/main/java/com/damaskitchen/studio/presentation/render/RenderStudioScreen.kt`
26. `app/src/main/res/values/strings.xml`
27. `app/src/main/res/values/themes.xml`
28. `app/src/main/res/xml/file_paths.xml`
29. `app/src/main/java/com/damaskitchen/studio/presentation/theme/DamasTheme.kt`
30. `app/src/main/java/com/damaskitchen/studio/MainActivity.kt`
31. `app/src/test/java/com/damaskitchen/studio/DamasCatalogAndPromptTest.kt`
