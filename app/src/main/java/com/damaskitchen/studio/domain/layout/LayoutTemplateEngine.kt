package com.damaskitchen.studio.domain.layout

import com.damaskitchen.studio.domain.model.CabinetModuleType
import com.damaskitchen.studio.domain.model.KitchenLayoutType
import com.damaskitchen.studio.domain.model.ModuleCoordinatesCm
import com.damaskitchen.studio.domain.model.ModuleDimensionsCm
import com.damaskitchen.studio.domain.model.PlacedCabinetModule
import com.damaskitchen.studio.domain.model.RoomDimensionsCm
import com.damaskitchen.studio.domain.model.WallSide
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Generates parametric Middle-Eastern kitchen layouts and enforces centimeter-accurate
 * magnetic snapping to walls and adjacent cabinet edges.
 */
object LayoutTemplateEngine {

    fun generateTemplate(
        layoutType: KitchenLayoutType,
        room: RoomDimensionsCm,
        goldAccents: Boolean = false
    ): List<PlacedCabinetModule> {
        var seq = 1
        fun nextId(prefix: String): String = "${prefix}_${seq++}"

        fun buildModule(
            type: CabinetModuleType,
            xCm: Int,
            yCm: Int,
            wallSide: WallSide,
            widthOverride: Int? = null,
            rotationDeg: Int = 0
        ): PlacedCabinetModule {
            val w = widthOverride ?: type.defaultWidthCm
            return PlacedCabinetModule(
                id = nextId(type.name.lowercase()),
                type = type,
                dimensions = ModuleDimensionsCm(
                    widthCm = w,
                    heightCm = type.defaultHeightCm,
                    depthCm = type.defaultDepthCm,
                    tierTwoDepthCm = type.defaultTierTwoDepthCm
                ),
                coordinates = ModuleCoordinatesCm(
                    xCm = xCm,
                    yCm = yCm,
                    elevationCm = type.defaultElevationCm,
                    rotationDeg = rotationDeg,
                    wallSide = wallSide
                ),
                frameGoldAccent = goldAccents
            )
        }

        val modules = mutableListOf<PlacedCabinetModule>()

        // North main wall (Left -> Right)
        modules += buildModule(CabinetModuleType.FRIDGE_HOUSING, xCm = 0, yCm = 0, wallSide = WallSide.NORTH)
        modules += buildModule(CabinetModuleType.OVEN_TOWER, xCm = 95, yCm = 0, wallSide = WallSide.NORTH)
        modules += buildModule(CabinetModuleType.BASE_DRAWER_UNIT, xCm = 155, yCm = 0, wallSide = WallSide.NORTH, widthOverride = 75)
        modules += buildModule(CabinetModuleType.TWO_TIER_UPPER, xCm = 155, yCm = 0, wallSide = WallSide.NORTH, widthOverride = 75)
        modules += buildModule(CabinetModuleType.COOKTOP_HOOD_UNIT, xCm = 230, yCm = 0, wallSide = WallSide.NORTH)
        modules += buildModule(CabinetModuleType.CNC_HOOD_VALANCE, xCm = 215, yCm = 0, wallSide = WallSide.NORTH, widthOverride = 120)
        modules += buildModule(CabinetModuleType.LIT_NICHE_PILLAR, xCm = 320, yCm = 0, wallSide = WallSide.NORTH)
        modules += buildModule(
            CabinetModuleType.TALL_VITRINE_TOWER,
            xCm = 365,
            yCm = 0,
            wallSide = WallSide.NORTH,
            widthOverride = (room.widthCm - 365).coerceIn(55, 75)
        )

        when (layoutType) {
            KitchenLayoutType.L_SHAPE -> {
                modules += buildModule(CabinetModuleType.CORNER_L_BASE, xCm = 0, yCm = 65, wallSide = WallSide.WEST, rotationDeg = 90)
                modules += buildModule(CabinetModuleType.SINK_WINDOW_UNIT, xCm = 0, yCm = 155, wallSide = WallSide.WEST, rotationDeg = 90)
                modules += buildModule(CabinetModuleType.LOUVERED_UPPER, xCm = 0, yCm = 155, wallSide = WallSide.WEST, widthOverride = 120, rotationDeg = 90)
            }

            KitchenLayoutType.U_SHAPE -> {
                modules += buildModule(CabinetModuleType.CORNER_L_BASE, xCm = 0, yCm = 65, wallSide = WallSide.WEST, rotationDeg = 90)
                modules += buildModule(CabinetModuleType.SINK_WINDOW_UNIT, xCm = 0, yCm = 155, wallSide = WallSide.WEST, rotationDeg = 90)
                modules += buildModule(CabinetModuleType.BASE_DRAWER_UNIT, xCm = (room.widthCm - 60).coerceAtLeast(300), yCm = 70, wallSide = WallSide.EAST, widthOverride = 90, rotationDeg = 270)
                modules += buildModule(CabinetModuleType.TWO_TIER_UPPER, xCm = (room.widthCm - 55).coerceAtLeast(305), yCm = 70, wallSide = WallSide.EAST, widthOverride = 90, rotationDeg = 270)
                modules += buildModule(CabinetModuleType.TALL_VITRINE_TOWER, xCm = (room.widthCm - 60).coerceAtLeast(300), yCm = 160, wallSide = WallSide.EAST, widthOverride = 70, rotationDeg = 270)
            }

            KitchenLayoutType.STRAIGHT_WITH_ISLAND -> {
                modules += buildModule(
                    CabinetModuleType.FLUTED_ISLAND_BAR,
                    xCm = ((room.widthCm - 180) / 2).coerceAtLeast(80),
                    yCm = 185,
                    wallSide = WallSide.ISLAND,
                    widthOverride = 180
                )
            }

            KitchenLayoutType.L_SHAPE_WITH_ISLAND -> {
                modules += buildModule(CabinetModuleType.CORNER_L_BASE, xCm = 0, yCm = 65, wallSide = WallSide.WEST, rotationDeg = 90)
                modules += buildModule(CabinetModuleType.SINK_WINDOW_UNIT, xCm = 0, yCm = 155, wallSide = WallSide.WEST, rotationDeg = 90)
                modules += buildModule(
                    CabinetModuleType.FLUTED_ISLAND_BAR,
                    xCm = 150,
                    yCm = 190,
                    wallSide = WallSide.ISLAND,
                    widthOverride = 190
                )
            }
        }

        return modules
    }

    /**
     * Magnetic snap in centimeters: snaps to room walls (0 / max) and nearby module edges
     * within [snapThresholdCm], then aligns to [RoomDimensionsCm.snapGridCm].
     */
    fun snapModulePosition(
        target: PlacedCabinetModule,
        rawXCm: Int,
        rawYCm: Int,
        existingModules: List<PlacedCabinetModule>,
        room: RoomDimensionsCm,
        snapThresholdCm: Int = 12
    ): ModuleCoordinatesCm {
        val grid = room.snapGridCm.coerceAtLeast(1)
        val effectiveW = if (target.coordinates.rotationDeg % 180 == 0) target.dimensions.widthCm else target.dimensions.depthCm
        val effectiveD = if (target.coordinates.rotationDeg % 180 == 0) target.dimensions.depthCm else target.dimensions.widthCm

        val maxX = (room.widthCm - effectiveW).coerceAtLeast(0)
        val maxY = (room.depthCm - effectiveD).coerceAtLeast(0)

        var snappedX = rawXCm.coerceIn(0, maxX)
        var snappedY = rawYCm.coerceIn(0, maxY)

        // Snap to North/West/East walls magnetically
        if (snappedY <= snapThresholdCm) {
            snappedY = 0
        }
        if (snappedX <= snapThresholdCm) {
            snappedX = 0
        }
        if (abs(snappedX - maxX) <= snapThresholdCm) {
            snappedX = maxX
        }

        // Magnetic edge-to-edge snapping against neighboring modules
        for (other in existingModules) {
            if (other.id == target.id) continue
            val otherW = if (other.coordinates.rotationDeg % 180 == 0) other.dimensions.widthCm else other.dimensions.depthCm
            val otherD = if (other.coordinates.rotationDeg % 180 == 0) other.dimensions.depthCm else other.dimensions.widthCm

            val otherLeft = other.coordinates.xCm
            val otherRight = other.coordinates.xCm + otherW
            val otherTop = other.coordinates.yCm
            val otherBottom = other.coordinates.yCm + otherD

            // Snap left edge of target to right edge of neighbor
            if (abs(snappedX - otherRight) <= snapThresholdCm) {
                snappedX = otherRight
            } else if (abs((snappedX + effectiveW) - otherLeft) <= snapThresholdCm) {
                snappedX = (otherLeft - effectiveW).coerceAtLeast(0)
            } else if (abs(snappedX - otherLeft) <= snapThresholdCm) {
                snappedX = otherLeft
            }

            // Snap top edge of target to bottom or top edge of neighbor
            if (abs(snappedY - otherBottom) <= snapThresholdCm) {
                snappedY = otherBottom
            } else if (abs((snappedY + effectiveD) - otherTop) <= snapThresholdCm) {
                snappedY = (otherTop - effectiveD).coerceAtLeast(0)
            } else if (abs(snappedY - otherTop) <= snapThresholdCm) {
                snappedY = otherTop
            }
        }

        // Quantize to centimeter grid
        snappedX = (((snappedX.toFloat() / grid).roundToInt()) * grid).coerceIn(0, maxX)
        snappedY = (((snappedY.toFloat() / grid).roundToInt()) * grid).coerceIn(0, maxY)

        val inferredWall = when {
            snappedY == 0 -> WallSide.NORTH
            snappedX == 0 -> WallSide.WEST
            snappedX >= maxX - 5 -> WallSide.EAST
            else -> WallSide.ISLAND
        }

        return target.coordinates.copy(
            xCm = snappedX,
            yCm = snappedY,
            wallSide = inferredWall
        )
    }
}
