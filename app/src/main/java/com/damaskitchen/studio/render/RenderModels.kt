package com.damaskitchen.studio.render

import android.graphics.Bitmap

/**
 * Supported rendering pipelines for converting the 2D/3D CAD project
 * into a photorealistic architectural photograph.
 */
enum class RenderEngineMode(
    val title: String,
    val subtitle: String
) {
    LOCAL_PBR_PREVIEW(
        title = "Local PBR Studio",
        subtitle = "Instant offline elevation + 3000K LED & floor reflection pass"
    ),
    GEMINI_VISION(
        title = "Gemini Vision / Imagen",
        subtitle = "Google AI REST API using elevation reference + prompt"
    ),
    STABLE_DIFFUSION_CONTROLNET(
        title = "SD ControlNet (Canny + Depth)",
        subtitle = "Automatic1111 / WebUI REST API with dual ControlNet maps"
    )
}

/**
 * Preview layer displayed in the Render Studio viewport.
 */
enum class RenderPreviewLayer(val label: String) {
    PHOTOREALISTIC("AI / PBR Render"),
    ELEVATION_CAD("Elevation Blueprint"),
    CANNY_EDGE("Canny Edge Map"),
    DEPTH_MAP("Depth Map")
}

/**
 * Configuration for external AI rendering endpoints.
 */
data class AiRenderConfig(
    val mode: RenderEngineMode = RenderEngineMode.LOCAL_PBR_PREVIEW,
    val geminiApiKey: String = "",
    val geminiModel: String = "gemini-2.0-flash-exp-image-generation",
    val controlNetBaseUrl: String = "http://10.0.2.2:7860",
    val controlNetCannyWeight: Float = 0.85f,
    val controlNetDepthWeight: Float = 0.65f,
    val inferenceSteps: Int = 28,
    val cfgScale: Float = 7.0f,
    val widthPx: Int = 1024,
    val heightPx: Int = 768
)

/**
 * Packaged architectural maps exported from the live KitchenProject CAD state.
 */
data class CadExportPackage(
    val elevationBitmap: Bitmap,
    val cannyEdgeBitmap: Bitmap,
    val depthBitmap: Bitmap,
    val elevationBase64Png: String,
    val cannyBase64Png: String,
    val depthBase64Png: String
)

/**
 * Final output produced by RealisticRenderEngine.
 */
data class RenderOutput(
    val modeUsed: RenderEngineMode,
    val renderedBitmap: Bitmap,
    val cadPackage: CadExportPackage,
    val promptUsed: String,
    val negativePromptUsed: String,
    val engineNotes: String,
    val elapsedMs: Long
)

/**
 * UI state for the Render Studio screen.
 */
data class RenderStudioUiState(
    val config: AiRenderConfig = AiRenderConfig(),
    val activeLayer: RenderPreviewLayer = RenderPreviewLayer.PHOTOREALISTIC,
    val isRendering: Boolean = false,
    val progressLabel: String = "",
    val generatedPrompt: String = "",
    val negativePrompt: String = "",
    val cadPackage: CadExportPackage? = null,
    val finalOutput: RenderOutput? = null,
    val errorMessage: String? = null,
    val exportSuccessMessage: String? = null
)
