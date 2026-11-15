package com.damaskitchen.studio.domain.catalog

import com.damaskitchen.studio.domain.model.BacksplashMaterial
import com.damaskitchen.studio.domain.model.CabinetModuleType
import com.damaskitchen.studio.domain.model.CabinetStylePreset
import com.damaskitchen.studio.domain.model.CountertopMaterial
import com.damaskitchen.studio.domain.model.FlooringMaterial
import com.damaskitchen.studio.domain.model.MaterialConfiguration
import com.damaskitchen.studio.domain.model.SurfaceFinishType

/**
 * Hardcoded architectural presets matching real-world Middle-Eastern luxury aluminum,
 * acrylic, and wood-cladding kitchen craftsmanship.
 */
object DamasCatalog {

    val stylePresets: List<CabinetStylePreset> = listOf(
        CabinetStylePreset(
            id = "damas_duo_tone",
            nameEn = "Damas Duo-Tone",
            nameAr = "داماس ثنائي اللون (كريمي + خشبي)",
            subtitleEn = "Matte Cream #EAE3DC + Light Oak Woodgrain #B89F86",
            baseColorHex = 0xFFEAE3DC,
            middleUpperColorHex = 0xFFB89F86,
            topBridgeColorHex = 0xFFEAE3DC,
            frameAccentColorHex = 0xFF18181A,
            islandFlutedColorHex = 0xFF3E3A37,
            toeKickColorHex = 0xFF141414,
            finishType = SurfaceFinishType.MATTE_ALUMINUM_ACRYLIC,
            hasJpullBlackGap = true,
            hasGoldHardware = false,
            hasCrownMolding = false,
            promptStyleDescriptor = "Damas Duo-Tone matte cream (#EAE3DC) base and ceiling-flush top bridge cabinets paired with light oak woodgrain (#B89F86) handleless middle upper cabinets and crisp black J-pull channels"
        ),
        CabinetStylePreset(
            id = "rose_cappuccino_gloss",
            nameEn = "Rose-Cappuccino High-Gloss",
            nameAr = "كابتشينو روز هاي غلوس",
            subtitleEn = "Mirror Acrylic #C8A290 + Black J-Pull Shadow Gaps",
            baseColorHex = 0xFFC8A290,
            middleUpperColorHex = 0xFFC8A290,
            topBridgeColorHex = 0xFFEDE6E0,
            frameAccentColorHex = 0xFF121212,
            islandFlutedColorHex = 0xFF4A3B36,
            toeKickColorHex = 0xFF111111,
            finishType = SurfaceFinishType.HIGH_GLOSS_ACRYLIC,
            hasJpullBlackGap = true,
            hasGoldHardware = false,
            hasCrownMolding = false,
            promptStyleDescriptor = "mirror-like Rose-Cappuccino high-gloss acrylic cabinetry (#C8A290) with integrated matte black J-pull shadow gaps and warm cream top bridge units"
        ),
        CabinetStylePreset(
            id = "anthracite_fluted_bar",
            nameEn = "Anthracite Luxury + Fluted Bar",
            nameAr = "أنثراسيت فاخر + بار مضلع",
            subtitleEn = "Charcoal Matte #4A4D50 + Smoked Mirror + Gold Accents",
            baseColorHex = 0xFF4A4D50,
            middleUpperColorHex = 0xFF3A3D40,
            topBridgeColorHex = 0xFF56595C,
            frameAccentColorHex = 0xFFD4AF37,
            islandFlutedColorHex = 0xFF2B2D30,
            toeKickColorHex = 0xFF101012,
            finishType = SurfaceFinishType.VERTICAL_FLUTED,
            hasJpullBlackGap = true,
            hasGoldHardware = true,
            hasCrownMolding = false,
            promptStyleDescriptor = "Anthracite charcoal matte cabinetry (#4A4D50) accented with brushed champagne gold profiles, smoked black mirror glass doors, and a dark bronze vertical fluted island bar"
        ),
        CabinetStylePreset(
            id = "classic_aluminum_slats",
            nameEn = "Classic Aluminum Slats",
            nameAr = "ألمنيوم شرائح كلاسيك + إطار أسود",
            subtitleEn = "Off-White Slats #F0ECE1 + Gloss Black Framing #141414",
            baseColorHex = 0xFFF0ECE1,
            middleUpperColorHex = 0xFFF0ECE1,
            topBridgeColorHex = 0xFFF0ECE1,
            frameAccentColorHex = 0xFF141414,
            islandFlutedColorHex = 0xFF1E1E1E,
            toeKickColorHex = 0xFF141414,
            finishType = SurfaceFinishType.HORIZONTAL_SLATS,
            hasJpullBlackGap = false,
            hasGoldHardware = false,
            hasCrownMolding = true,
            promptStyleDescriptor = "Classic Middle-Eastern horizontal slatted off-white aluminum doors (#F0ECE1) framed in gloss black (#141414) outer profiles and matching black crown molding"
        ),
        CabinetStylePreset(
            id = "champagne_gold_cnc",
            nameEn = "Champagne Gold CNC Arabesque",
            nameAr = "شامبين ذهبي مع زخرفة CNC",
            subtitleEn = "Warm Champagne #D8C8B4 + Walnut + Gold Arabesque",
            baseColorHex = 0xFFD8C8B4,
            middleUpperColorHex = 0xFF7B5E45,
            topBridgeColorHex = 0xFFEAE0D4,
            frameAccentColorHex = 0xFFC9A45C,
            islandFlutedColorHex = 0xFF5C4634,
            toeKickColorHex = 0xFF1A1715,
            finishType = SurfaceFinishType.WOODGRAIN_CLADDING,
            hasJpullBlackGap = true,
            hasGoldHardware = true,
            hasCrownMolding = true,
            promptStyleDescriptor = "Champagne gold and warm walnut woodgrain cabinetry (#D8C8B4 & #7B5E45) featuring an illuminated gold CNC laser-cut arabesque hood valance and brushed gold vitrine frames"
        )
    )

    val defaultMaterialConfiguration: MaterialConfiguration = MaterialConfiguration(
        preset = stylePresets.first(),
        countertop = CountertopMaterial.ABSOLUTE_BLACK_GALAXY,
        backsplash = BacksplashMaterial.CALACATTA_MARBLE_SLAB,
        flooring = FlooringMaterial.GLOSS_WARM_BEIGE_PORCELAIN
    )

    val allModules: List<CabinetModuleType> = CabinetModuleType.entries

    fun presetById(id: String): CabinetStylePreset {
        return stylePresets.firstOrNull { it.id == id } ?: stylePresets.first()
    }
}
