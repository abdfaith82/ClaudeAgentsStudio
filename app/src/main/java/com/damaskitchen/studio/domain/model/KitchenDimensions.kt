package com.damaskitchen.studio.domain.model

import kotlin.math.roundToInt

/**
 * Metric (centimeter) room geometry, grid snapping rules, and 3D bounding coordinates.
 */
data class RoomDimensionsCm(
    val widthCm: Int = 420,
    val depthCm: Int = 340,
    val ceilingHeightCm: Int = 270,
    val gypsumDropCm: Int = 20,
    val snapGridCm: Int = 5
) {
    val usableCabinetHeightCm: Int
        get() = (ceilingHeightCm - gypsumDropCm).coerceAtLeast(220)
}

enum class WallSide(val labelAr: String, val labelEn: String) {
    NORTH("الجدار الرئيسي (الشمالي)", "North Wall (Main)"),
    WEST("الجدار الجانبي (الغربي)", "West Wall (Left)"),
    EAST("الجدار الجانبي (الشرقي)", "East Wall (Right)"),
    ISLAND("جزيرة / بار وسطي", "Center Island / Bar")
}

enum class KitchenLayoutType(
    val displayNameEn: String,
    val displayNameAr: String,
    val promptDescription: String
) {
    L_SHAPE(
        displayNameEn = "L-Shape Luxury",
        displayNameAr = "حرف L فاخر",
        promptDescription = "an ergonomic L-shaped layout wrapping two adjoining walls"
    ),
    U_SHAPE(
        displayNameEn = "U-Shape Grand",
        displayNameAr = "حرف U متكامل",
        promptDescription = "a spacious U-shaped perimeter layout maximizing storage and prep zones"
    ),
    STRAIGHT_WITH_ISLAND(
        displayNameEn = "Linear + Fluted Island",
        displayNameAr = "مستقيم مع بار جزيرة",
        promptDescription = "a sleek straight wall run paired with a front fluted island bar"
    ),
    L_SHAPE_WITH_ISLAND(
        displayNameEn = "L-Shape + Fluted Island",
        displayNameAr = "حرف L مع جزيرة بار",
        promptDescription = "an L-shaped kitchen layout anchored by a freestanding fluted island bar"
    )
}

data class ModuleCoordinatesCm(
    val xCm: Int,
    val yCm: Int,
    val elevationCm: Int = 0,
    val rotationDeg: Int = 0,
    val wallSide: WallSide = WallSide.NORTH
) {
    fun snapped(gridCm: Int): ModuleCoordinatesCm {
        val safeGrid = gridCm.coerceAtLeast(1)
        val sx = ((xCm.toFloat() / safeGrid).roundToInt()) * safeGrid
        val sy = ((yCm.toFloat() / safeGrid).roundToInt()) * safeGrid
        return copy(xCm = sx, yCm = sy)
    }
}

data class ModuleDimensionsCm(
    val widthCm: Int,
    val heightCm: Int,
    val depthCm: Int,
    val tierTwoDepthCm: Int = 0,
    val toeKickCm: Int = 10
)
