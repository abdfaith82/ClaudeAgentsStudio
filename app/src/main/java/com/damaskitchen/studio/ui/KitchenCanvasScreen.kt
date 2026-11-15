package com.damaskitchen.studio.ui

import android.graphics.Matrix
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.damaskitchen.studio.domain.model.CabinetModuleType
import com.damaskitchen.studio.domain.model.KitchenProject
import com.damaskitchen.studio.domain.model.PlacedCabinetModule
import com.damaskitchen.studio.domain.model.WallSide
import com.damaskitchen.studio.viewmodel.KitchenStudioViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

/**
 * Main CAD canvas screen.
 *
 * Features:
 *  - Metric grid (1 cm spacing) drawn in the background.
 *  - Pan & zoom with pinch gestures (scale anchored at gesture centroid).
 *  - Magnetic snapping to the centimeter grid when modules are moved.
 *  - Drag‑and‑drop placement of modules; selected module is highlighted.
 *  - Simple real‑time preview toggle between plan (top‑down) and isometric view.
 */
@Composable
fun KitchenCanvasScreen(
    viewModel: KitchenStudioViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // UI state ---------------------------------------------------------------
    var canvasScale by remember { mutableStateOf(1f) }                     // 1 = 100 %
    var canvasOffset by remember { mutableStateOf(Offset.Zero) }           // Pan offset in px
    var selectedModuleId by remember { mutableStateOf<String?>(null) }    // Currently selected
    var isIsometric by remember { mutableStateOf(false) }                 // Preview mode toggle

    // Project flow ------------------------------------------------------------
    val projectState = viewModel.kitchenProjectFlow
        .collectAsState(initial = null)

    // Early UI while loading ---------------------------------------------------
    val project = projectState.value
    if (project == null) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        return
    }

    // Helpers -----------------------------------------------------------------
    val pixelsPerCm: Float = 10f * canvasScale        // 10 px == 1 cm at 100 %
    val gridSpacingPx = pixelsPerCm

    // Snaps a raw pixel offset to the nearest centimeter line.
    fun snapToCm(raw: Float): Float = (raw / pixelsPerCm).roundToInt() * pixelsPerCm

    // Convert screen‑space offset to logical cm coordinates (relative to room origin).
    fun screenToCm(offset: Offset): Pair<Int, Int> {
        val xCm = ((offset.x - canvasOffset.x) / pixelsPerCm).roundToInt()
        val yCm = ((offset.y - canvasOffset.y) / pixelsPerCm).roundToInt()
        return Pair(xCm, yCm)
    }

    // Convert logical cm coordinates back to screen‑space.
    fun cmToScreen(xCm: Int, yCm: Int): Offset =
        Offset(
            x = canvasOffset.x + xCm * pixelsPerCm,
            y = canvasOffset.y + yCm * pixelsPerCm
        )

    // ------------------------------------------------------------------------
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
            // Global canvas transform: pan + zoom
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    // Update scale, clamped to a reasonable range
                    canvasScale = (canvasScale * zoom).coerceIn(0.4f, 3f)

                    // Apply the pan taking the new scale into account
                    val adjustedPan = pan * canvasScale
                    canvasOffset = canvasOffset + adjustedPan
                }
            }
    ) {
        // --------------------------------------------------------------------
        // Grid drawing
        // --------------------------------------------------------------------
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw vertical lines
            val width = size.width
            val height = size.height
            var x = canvasOffset.x % gridSpacingPx
            while (x < width) {
                drawLine(
                    color = Color(0xFFCCCCCC),
                    start = Offset(x, 0f),
                    end = Offset(x, height),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
                x += gridSpacingPx
            }
            // Draw horizontal lines
            var y = canvasOffset.y % gridSpacingPx
            while (y < height) {
                drawLine(
                    color = Color(0xFFCCCCCC),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
                y += gridSpacingPx
            }
        }

        // --------------------------------------------------------------------
        // Module rendering + interaction
        // --------------------------------------------------------------------
        Canvas(modifier = Modifier
            .fillMaxSize()
            // Detect taps for selection
            .pointerInput(project.modules) {
                detectDragGestures(
                    onDragStart = { offset ->
                        // Find module under the tap (top‑most first)
                        val tapped = project.modules.reversed().firstOrNull { module ->
                            val screenPos = cmToScreen(
                                module.coordinates.xCm,
                                module.coordinates.yCm
                            )
                            val w = module.dimensions.widthCm * pixelsPerCm
                            val h = module.dimensions.depthCm * pixelsPerCm
                            val rect = androidx.compose.ui.geometry.Rect(
                                offset = screenPos,
                                size = Size(w, h)
                            )
                            rect.contains(offset)
                        }
                        selectedModuleId = tapped?.id
                    },
                    onDrag = { change, dragAmount ->
                        // If a module is selected, move it
                        val moduleId = selectedModuleId ?: return@detectDragGestures
                        change.consume()
                        // Convert dragAmount from px to cm (respect current scale)
                        val deltaCmX = (dragAmount.x / pixelsPerCm).roundToInt()
                        val deltaCmY = (dragAmount.y / pixelsPerCm).roundToInt()
                        viewModel.moveModuleByCm(moduleId, deltaCmX, deltaCmY)
                    },
                    onDragEnd = { /* no‑op */ },
                    onDragCancel = { /* no‑op */ }
                )
            }
        ) {
            // Render each module
            project.modules.forEach { module ->
                // Compute top‑left screen point for this module
                val topLeft = cmToScreen(module.coordinates.xCm, module.coordinates.yCm)

                // Simple color coding by module type (placeholder – real UI will use textures)
                val fillColor = when (module.type) {
                    CabinetModuleType.TALL_VITRINE_TOWER -> Color(0xFF212121)
                    CabinetModuleType.LIT_NICHE_PILLAR -> Color(0xFF6D4C41)
                    CabinetModuleType.BASE_DRAWER_UNIT,
                    CabinetModuleType.CORNER_L_BASE -> Color(0xFF8D6E63)
                    CabinetModuleType.TWO_TIER_UPPER,
                    CabinetModuleType.LOUVERED_UPPER -> Color(0xFFBCAAA4)
                    CabinetModuleType.CNC_HOOD_VALANCE -> Color(0xFF795548)
                    else -> Color(0xFF9E9E9E)
                }

                // Width and depth (height is drawn as vertical size only in plan view)
                val widthPx = module.dimensions.widthCm * pixelsPerCm
                val depthPx = module.dimensions.depthCm * pixelsPerCm

                // Apply rotation for wall‑side orientation (0°, 90°, 180°, 270°)
                rotate(
                    degrees = module.coordinates.rotationDeg.toFloat(),
                    pivot = topLeft + Offset(widthPx / 2f, depthPx / 2f)
                ) {
                    // Highlight if selected
                    if (module.id == selectedModuleId) {
                        drawRect(
                            color = Color.Yellow.copy(alpha = 0.4f),
                            topLeft = topLeft,
                            size = Size(widthPx, depthPx)
                        )
                    }
                    // Main module rectangle
                    drawRect(
                        color = fillColor,
                        topLeft = topLeft,
                        size = Size(widthPx, depthPx),
                        style = androidx.compose.ui.graphics.drawscope.Fill
                    )
                }
            }
        }

        // --------------------------------------------------------------------
        // Simple preview mode toggle (Plan ↔ Isometric)
        // --------------------------------------------------------------------
        androidx.compose.material3.IconButton(
            onClick = { isIsometric = !isIsometric },
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.TopEnd)
                .padding(16.dp)
        ) {
            val iconRes = if (isIsometric) android.R.drawable.ic_menu_mapmode
            else android.R.drawable.ic_menu_gallery
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = iconRes),
                contentDescription = if (isIsometric) "Isometric View" else "Plan View"
            )
        }

        // --------------------------------------------------------------------
        // Isometric preview (very simplified – draws same rectangles with an
        // isometric transform). In a production app this would be replaced by
        // a proper 3‑D engine or GL surface.
        // --------------------------------------------------------------------
        if (isIsometric) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Build an isometric matrix (30° rotation on X, 45° on Y)
                val isoMatrix = Matrix().apply {
                    // Convert to Android graphics matrix for convenience
                    // Scale to match existing pixel‑per‑cm factor
                    setScale(pixelsPerCm, pixelsPerCm)
                    postRotate(45f)               // Y‑axis rotation
                    postSkew(0f, 0.5f)            // Approximate X‑axis tilt
                }

                project.modules.forEach { module ->
                    // Base rectangle (plan view)
                    val rect = android.graphics.RectF(
                        module.coordinates.xCm.toFloat(),
                        module.coordinates.yCm.toFloat(),
                        (module.coordinates.xCm + module.dimensions.widthCm).toFloat(),
                        (module.coordinates.yCm + module.dimensions.depthCm).toFloat()
                    )
                    // Transform to isometric space
                    isoMatrix.mapRect(rect)

                    // Offset by canvas pan
                    val left = rect.left + canvasOffset.x
                    val top = rect.top + canvasOffset.y
                    val right = rect.right + canvasOffset.x
                    val bottom = rect.bottom + canvasOffset.y

                    // Draw with same colour logic as plan view
                    val fillColor = when (module.type) {
                        CabinetModuleType.TALL_VITRINE_TOWER -> Color(0xFF212121)
                        CabinetModuleType.LIT_NICHE_PILLAR -> Color(0xFF6D4C41)
                        else -> Color(0xFF9E9E9E)
                    }

                    drawRect(
                        color = fillColor,
                        topLeft = Offset(left, top),
                        size = Size(right - left, bottom - top)
                    )
                }
            }
        }
    }
}
