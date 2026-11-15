package com.damaskitchen.studio.domain.model

enum class SurfaceFinishType(val labelEn: String, val labelAr: String) {
    MATTE_ALUMINUM_ACRYLIC("Matte Aluminum / Acrylic", "ألمنيوم / أكريليك مطفي"),
    HIGH_GLOSS_ACRYLIC("Mirror High-Gloss Acrylic", "أكريليك هاي غلوس لامع"),
    WOODGRAIN_CLADDING("Natural Woodgrain Cladding", "تكسيات خشب طبيعي"),
    HORIZONTAL_SLATS("Horizontal Louvered Slats", "شرائح ألمنيوم مفرغة/مخططة"),
    VERTICAL_FLUTED("Vertical Fluted Panels", "ألواح مضلعة عمودية")
}

data class CabinetStylePreset(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val subtitleEn: String,
    val baseColorHex: Long,
    val middleUpperColorHex: Long,
    val topBridgeColorHex: Long,
    val frameAccentColorHex: Long,
    val islandFlutedColorHex: Long,
    val toeKickColorHex: Long,
    val finishType: SurfaceFinishType,
    val hasJpullBlackGap: Boolean,
    val hasGoldHardware: Boolean,
    val hasCrownMolding: Boolean,
    val promptStyleDescriptor: String
)

enum class CountertopMaterial(
    val titleEn: String,
    val titleAr: String,
    val primaryHex: Long,
    val veinOrSpeckleHex: Long,
    val hasRaisedWaterLip: Boolean,
    val promptDescriptor: String
) {
    ABSOLUTE_BLACK_GALAXY(
        titleEn = "Absolute Black Galaxy Granite",
        titleAr = "غرانيت جلاكسي أسود بحافة مائية",
        primaryHex = 0xFF151618,
        veinOrSpeckleHex = 0xFF5A5240,
        hasRaisedWaterLip = true,
        promptDescriptor = "Absolute Black Galaxy granite with a raised anti-spill perimeter lip"
    ),
    WHITE_CALACATTA_QUARTZ(
        titleEn = "White Calacatta Gold-Vein",
        titleAr = "كالاكاتا أبيض بعروق ذهبية",
        primaryHex = 0xFFF3EFE8,
        veinOrSpeckleHex = 0xFFC5A875,
        hasRaisedWaterLip = true,
        promptDescriptor = "polished White Calacatta stone with subtle warm grey and gold veins and raised edge lip"
    ),
    SPECKLED_GREY_GRANITE(
        titleEn = "Speckled Grey Granite",
        titleAr = "غرانيت رمادي منقط",
        primaryHex = 0xFF7B7F84,
        veinOrSpeckleHex = 0xFF393C40,
        hasRaisedWaterLip = true,
        promptDescriptor = "speckled architectural grey granite"
    )
}

enum class BacksplashMaterial(
    val titleEn: String,
    val titleAr: String,
    val baseHex: Long,
    val veinHex: Long,
    val promptDescriptor: String
) {
    CALACATTA_MARBLE_SLAB(
        titleEn = "Calacatta Marble Warm Veins",
        titleAr = "رخام كالاكاتا أبيض",
        baseHex = 0xFFF5F2EC,
        veinHex = 0xFFB8A99A,
        promptDescriptor = "large-format White Calacatta marble slab with warm grey and gold veining"
    ),
    CREAM_TRAVERTINE_SLAB(
        titleEn = "Warm Cream Porcelain Slab",
        titleAr = "بورسلان كريمي دافئ",
        baseHex = 0xFFEAE1D5,
        veinHex = 0xFFC9B9A5,
        promptDescriptor = "seamless matte cream travertine porcelain"
    ),
    SMOKED_BRONZE_MIRROR(
        titleEn = "Smoked Bronze Glass Backsplash",
        titleAr = "زجاج عاكس برونزي مدخن",
        baseHex = 0xFF3F352E,
        veinHex = 0xFF6A5B50,
        promptDescriptor = "tinted smoked bronze tempered glass"
    )
}

enum class FlooringMaterial(
    val titleEn: String,
    val titleAr: String,
    val tileHex: Long,
    val groutHex: Long,
    val tileSizeCm: Int,
    val glossiness: Float,
    val promptDescriptor: String
) {
    GLOSS_WARM_BEIGE_PORCELAIN(
        titleEn = "60x60 Gloss Beige Porcelain",
        titleAr = "بورسلان بيج لامع 60×60",
        tileHex = 0xFFD9D1C7,
        groutHex = 0xFFB9AFA3,
        tileSizeCm = 60,
        glossiness = 0.88f,
        promptDescriptor = "60x60cm high-gloss warm beige porcelain tiles reflecting the cabinetry LEDs"
    ),
    GLOSS_LIGHT_GREY_PORCELAIN(
        titleEn = "60x60 Gloss Cool Grey Porcelain",
        titleAr = "بورسلان رمادي لامع 60×60",
        tileHex = 0xFFC4C7CA,
        groutHex = 0xFF9EA2A7,
        tileSizeCm = 60,
        glossiness = 0.90f,
        promptDescriptor = "60x60cm mirror-polished light grey porcelain floor tiles with sharp ceiling reflections"
    )
}

data class CeilingLightingSpec(
    val hasRecessedLinearLed: Boolean = true,
    val hasWarmCoveLighting: Boolean = true,
    val hasDoubleBlackSpotlights: Boolean = true,
    val coveKelvin: Int = 3000
)

data class MaterialConfiguration(
    val preset: CabinetStylePreset,
    val countertop: CountertopMaterial = CountertopMaterial.ABSOLUTE_BLACK_GALAXY,
    val backsplash: BacksplashMaterial = BacksplashMaterial.CALACATTA_MARBLE_SLAB,
    val flooring: FlooringMaterial = FlooringMaterial.GLOSS_WARM_BEIGE_PORCELAIN,
    val ceilingLighting: CeilingLightingSpec = CeilingLightingSpec()
)
