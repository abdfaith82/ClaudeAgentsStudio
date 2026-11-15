package com.damaskitchen.studio.domain.model

enum class ModuleCategory(val labelEn: String, val labelAr: String) {
    TALL_TOWERS("Tall Towers & Vitrines", "أبراج وفترينات"),
    UPPER_TIERS("Two-Tier & Louvered Uppers", "خزائن علوية دبل"),
    BASE_AND_ISLAND("Base & Fluted Islands", "قواعد وجزر بار"),
    APPLIANCES_AND_SINK("Appliances, Hood & Sink", "أجهزة ومغاسل"),
    ARCHITECTURAL_DECOR("CNC Valances & Windows", "ديكور ليزر ونوافذ")
}

enum class CabinetModuleType(
    val category: ModuleCategory,
    val titleEn: String,
    val titleAr: String,
    val defaultWidthCm: Int,
    val defaultHeightCm: Int,
    val defaultDepthCm: Int,
    val defaultTierTwoDepthCm: Int,
    val defaultElevationCm: Int,
    val defaultLed3000K: Boolean,
    val promptPhrase: String
) {
    TALL_VITRINE_TOWER(
        category = ModuleCategory.TALL_TOWERS,
        titleEn = "Tall Glass Vitrine Tower",
        titleAr = "وحدة فترينة زجاجية مضيئة",
        defaultWidthCm = 70,
        defaultHeightCm = 250,
        defaultDepthCm = 50,
        defaultTierTwoDepthCm = 0,
        defaultElevationCm = 0,
        defaultLed3000K = true,
        promptPhrase = "full-height black-framed smoked glass display vitrine with 5 glass shelves and warm 3000K LED strip lighting"
    ),
    LIT_NICHE_PILLAR(
        category = ModuleCategory.TALL_TOWERS,
        titleEn = "Decorative Lit Niche Pillar",
        titleAr = "عمود نيشات ديكورية مضاءة",
        defaultWidthCm = 45,
        defaultHeightCm = 250,
        defaultDepthCm = 55,
        defaultTierTwoDepthCm = 0,
        defaultElevationCm = 0,
        defaultLed3000K = true,
        promptPhrase = "recessed square decorative display niche column with matte black inner frames and warm spotlights"
    ),
    TWO_TIER_UPPER(
        category = ModuleCategory.UPPER_TIERS,
        titleEn = "Two-Tier Ceiling-Flush Upper",
        titleAr = "خزائن علوية دبل للسقف",
        defaultWidthCm = 90,
        defaultHeightCm = 115,
        defaultDepthCm = 35,
        defaultTierTwoDepthCm = 55,
        defaultElevationCm = 135,
        defaultLed3000K = true,
        promptPhrase = "two-tier ceiling-flush handleless upper cabinets with stepped-forward top bridge section"
    ),
    LOUVERED_UPPER(
        category = ModuleCategory.UPPER_TIERS,
        titleEn = "Horizontal Slatted/Louvered Upper",
        titleAr = "درف شرائح ألمنيوم مخططة",
        defaultWidthCm = 80,
        defaultHeightCm = 90,
        defaultDepthCm = 35,
        defaultTierTwoDepthCm = 55,
        defaultElevationCm = 140,
        defaultLed3000K = false,
        promptPhrase = "horizontal louvered aluminum slatted upper doors with crisp black accent grooves"
    ),
    BASE_DRAWER_UNIT(
        category = ModuleCategory.BASE_AND_ISLAND,
        titleEn = "Handleless Base Drawer Cabinet",
        titleAr = "وحدة سفلية بأدراج J-Pull",
        defaultWidthCm = 80,
        defaultHeightCm = 85,
        defaultDepthCm = 60,
        defaultTierTwoDepthCm = 0,
        defaultElevationCm = 0,
        defaultLed3000K = false,
        promptPhrase = "handleless J-pull base drawer cabinets on a recessed matte black toe-kick"
    ),
    CORNER_L_BASE(
        category = ModuleCategory.BASE_AND_ISLAND,
        titleEn = "Corner L-Turn Base Unit",
        titleAr = "وحدة زاوية سفلية",
        defaultWidthCm = 90,
        defaultHeightCm = 85,
        defaultDepthCm = 90,
        defaultTierTwoDepthCm = 0,
        defaultElevationCm = 0,
        defaultLed3000K = false,
        promptPhrase = "seamless corner base cabinet with continuous mitered stone countertop lip"
    ),
    FLUTED_ISLAND_BAR(
        category = ModuleCategory.BASE_AND_ISLAND,
        titleEn = "Fluted Island Bar",
        titleAr = "بار شرائح مضلعة مضيء",
        defaultWidthCm = 180,
        defaultHeightCm = 92,
        defaultDepthCm = 80,
        defaultTierTwoDepthCm = 0,
        defaultElevationCm = 0,
        defaultLed3000K = true,
        promptPhrase = "dark charcoal vertical fluted island bar with recessed warm under-counter LED glow"
    ),
    FRIDGE_HOUSING(
        category = ModuleCategory.APPLIANCES_AND_SINK,
        titleEn = "Refrigerator Niche + Slatted Bridge",
        titleAr = "بيت ثلاجة مع تهوية علوية",
        defaultWidthCm = 95,
        defaultHeightCm = 250,
        defaultDepthCm = 65,
        defaultTierTwoDepthCm = 65,
        defaultElevationCm = 0,
        defaultLed3000K = false,
        promptPhrase = "integrated wide stainless double-door refrigerator housing topped by horizontal slatted ventilation bridge cabinets"
    ),
    OVEN_TOWER(
        category = ModuleCategory.APPLIANCES_AND_SINK,
        titleEn = "Built-in Black Glass Oven Tower",
        titleAr = "برج أفران زجاج أسود",
        defaultWidthCm = 60,
        defaultHeightCm = 250,
        defaultDepthCm = 60,
        defaultTierTwoDepthCm = 60,
        defaultElevationCm = 0,
        defaultLed3000K = false,
        promptPhrase = "tall appliance tower with flush built-in black glass oven and microwave"
    ),
    COOKTOP_HOOD_UNIT(
        category = ModuleCategory.APPLIANCES_AND_SINK,
        titleEn = "Cooktop + Angled Glass Hood",
        titleAr = "غاز مع شفاط زجاج مائل",
        defaultWidthCm = 90,
        defaultHeightCm = 250,
        defaultDepthCm = 60,
        defaultTierTwoDepthCm = 55,
        defaultElevationCm = 0,
        defaultLed3000K = true,
        promptPhrase = "built-in black glass hob beneath an angled black glass chimney range hood"
    ),
    SINK_WINDOW_UNIT(
        category = ModuleCategory.APPLIANCES_AND_SINK,
        titleEn = "Double Sink + Black Sliding Window",
        titleAr = "مغسلة دبل تحت شباك أسود",
        defaultWidthCm = 120,
        defaultHeightCm = 210,
        defaultDepthCm = 60,
        defaultTierTwoDepthCm = 0,
        defaultElevationCm = 0,
        defaultLed3000K = true,
        promptPhrase = "undermount double-bowl stainless steel sink with matte black pull-out mixer centered beneath a black-framed sliding window"
    ),
    CNC_HOOD_VALANCE(
        category = ModuleCategory.ARCHITECTURAL_DECOR,
        titleEn = "CNC Arabesque Laser Valance",
        titleAr = "تاج زخرفة ليزر CNC",
        defaultWidthCm = 120,
        defaultHeightCm = 35,
        defaultDepthCm = 38,
        defaultTierTwoDepthCm = 0,
        defaultElevationCm = 215,
        defaultLed3000K = true,
        promptPhrase = "gold and warm wood CNC laser-cut geometric arabesque valance panel backlit with warm LED"
    )
}

enum class DoorFinishRole {
    PRIMARY_BASE,
    WOOD_OR_ACCENT_UPPER,
    BRIDGE_TOP,
    SMOKED_GLASS_FRAME,
    FLUTED_ISLAND
}

data class PlacedCabinetModule(
    val id: String,
    val type: CabinetModuleType,
    val dimensions: ModuleDimensionsCm,
    val coordinates: ModuleCoordinatesCm,
    val ledEnabled: Boolean = type.defaultLed3000K,
    val ledKelvin: Int = 3000,
    val shelfCount: Int = if (type == CabinetModuleType.TALL_VITRINE_TOWER) 5 else 2,
    val frameGoldAccent: Boolean = false,
    val finishRole: DoorFinishRole = when (type) {
        CabinetModuleType.TALL_VITRINE_TOWER -> DoorFinishRole.SMOKED_GLASS_FRAME
        CabinetModuleType.TWO_TIER_UPPER,
        CabinetModuleType.LOUVERED_UPPER -> DoorFinishRole.WOOD_OR_ACCENT_UPPER
        CabinetModuleType.FLUTED_ISLAND_BAR -> DoorFinishRole.FLUTED_ISLAND
        else -> DoorFinishRole.PRIMARY_BASE
    },
    val customLabel: String = ""
)
