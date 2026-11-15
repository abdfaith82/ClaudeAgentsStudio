package com.damaskitchen.studio.render

import com.damaskitchen.studio.domain.model.KitchenProject
import com.damaskitchen.studio.viewmodel.StudioUiState
import com.damaskitchen.studio.domain.model.CabinetStylePreset

/**
 * Builds the exact prompt required by the AI rendering services.
 * All placeholders are populated from the live [KitchenProject] and optional [StudioUiState].
 */
object RenderPromptBuilder {

    const val DEFAULT_NEGATIVE_PROMPT: String =
        "cgi, 3d render, blender, unreal engine, cartoon, illustration, lowres, blurry, " +
                "warped cabinets, crooked doors, missing handles where expected, floating appliances, " +
                "overexposed windows, messy clutter, plastic textures, watermark, text"

    /**
     * Constructs the exact required prompt template populated with live CAD state.
     */
    fun buildPrompt(
        project: KitchenProject,
        uiState: StudioUiState? = null
    ): String {
        val layoutDescription = mapLayoutType(project.layoutType.name)
        val cabinetStyleDescription = buildCabinetStyleDescription(project, uiState)
        val countertopDescription = mapCountertopMaterial(
            uiState?.selectedCountertop?.name ?: project.materialConfig.countertop.name
        )
        val backsplashDescription = mapBacksplashMaterial(
            uiState?.selectedBacksplash?.name ?: project.materialConfig.backSplash.name
        )
        val flooringDescription = mapFlooringMaterial(
            uiState?.selectedFlooring?.name ?: project.materialConfig.flooring.name
        )

        val vitrinePhrase = if (project.materialConfig.vitrine3000kLed) {
            "a full-height black-framed smoked glass display vitrine on the end with glowing warm 3000K LED interior lights"
        } else {
            "a full-height black-framed smoked glass display vitrine on the end"
        }

        val ceilingLightingPhrase = if (project.materialConfig.warmCoveLighting) {
            "gypsum ceiling with recessed linear LED light strips and warm cove lighting"
        } else {
            "gypsum ceiling with recessed linear LED light strips"
        }

        return "Real-world interior smartphone photography of a newly installed luxury Middle-Eastern modern kitchen, " +
                "$layoutDescription, " +
                "featuring $cabinetStyleDescription, " +
                "$vitrinePhrase, " +
                "two-tier ceiling-high handleless cabinets, " +
                "$countertopDescription countertop with black toe-kick base, " +
                "$backsplashDescription wall tiles, " +
                "angled black glass range hood, " +
                "$ceilingLightingPhrase, " +
                "$flooringDescription reflecting the kitchen lights, " +
                "natural indoor lighting, shot on smartphone wide lens 24mm, ultra-authentic textures, zero CGI look, 8k photorealistic."
    }

    private fun mapLayoutType(enumName: String): String = when {
        enumName.contains("L_SHAPE", ignoreCase = true) -> "L-shaped ergonomic architectural layout"
        enumName.contains("U_SHAPE", ignoreCase = true) -> "expansive U-shaped architectural layout"
        enumName.contains("ISLAND", ignoreCase = true) -> "modern layout with central fluted island bar"
        enumName.contains("GALLEY", ignoreCase = true) || enumName.contains("PARALLEL", ignoreCase = true) ->
            "parallel galley luxury layout"
        else -> "custom single‑wall architectural layout"
    }

    private fun buildCabinetStyleDescription(
        project: KitchenProject,
        uiState: StudioUiState?
    ): String {
        val preset = uiState?.selectedStylePreset ?: project.materialConfig.stylePreset
        // Simple human‑readable description based on preset name; can be expanded later.
        return when (preset.name.lowercase()) {
            "damas duo‑tone" -> "Damas Duo‑Tone matte cream & light‑oak wood grain cabinets"
            "rose‑cappuccino high‑gloss" -> "Rose‑Cappuccino high‑gloss mirror acrylic cabinets (#C8A290) with integrated black J‑pull shadow gaps"
            "anthracite luxury" -> "Anthracite luxury matte charcoal cabinets with smoked black mirror glass and gold accents"
            "classic aluminum slats" -> "Classic aluminum slatted panels with off‑white finish and glossy black framing"
            else -> "custom cabinet style"
        }
    }

    // ---------- Material mapping helpers ----------
    private fun mapCountertopMaterial(name: String): String = when (name.lowercase()) {
        "black granite", "absolute black granite" -> "Absolute Black Granite"
        "calacatta marble" -> "White Calacatta Marble"
        "speckled grey granite" -> "Speckled Grey Granite"
        else -> "premium countertop"
    }

    private fun mapBacksplashMaterial(name: String): String = when (name.lowercase()) {
        "calacatta marble" -> "White Calacatta Marble"
        "ceramic white" -> "White ceramic"
        else -> "designer backsplash"
    }

    private fun mapFlooringMaterial(name: String): String = when (name.lowercase()) {
        "high‑gloss porcelain", "porcelain tiles" -> "high‑gloss porcelain floor tiles"
        else -> "elegant flooring"
    }
}
