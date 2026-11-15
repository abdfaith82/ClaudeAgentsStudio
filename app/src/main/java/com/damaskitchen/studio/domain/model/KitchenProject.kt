package com.damaskitchen.studio.domain.model

enum class WorkspaceViewMode(val labelEn: String, val labelAr: String) {
    PLAN_2D("2D CAD Plan", "مخطط 2D هندسي"),
    ELEVATION_FRONT("Front Elevation", "واجهة تفصيلية"),
    ISOMETRIC_3D("3D Studio View", "منظور ثلاثي الأبعاد")
}

data class KitchenProject(
    val projectId: String = "damas-proj-01",
    val projectName: String = "Damas Villa Kitchen",
    val layoutType: KitchenLayoutType = KitchenLayoutType.L_SHAPE_WITH_ISLAND,
    val roomDimensions: RoomDimensionsCm = RoomDimensionsCm(),
    val modules: List<PlacedCabinetModule> = emptyList(),
    val materials: MaterialConfiguration
) {
    val totalLinearMeters: Float
        get() {
            val totalCm = modules
                .filter { it.type.category != ModuleCategory.UPPER_TIERS && it.type != CabinetModuleType.CNC_HOOD_VALANCE }
                .sumOf { it.dimensions.widthCm }
            return totalCm / 100f
        }
}
