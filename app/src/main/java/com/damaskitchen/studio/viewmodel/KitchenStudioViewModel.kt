package com.damaskitchen.studio.viewmodel

import androidx.lifecycle.ViewModel
import com.damaskitchen.studio.domain.catalog.DamasCatalog
import com.damaskitchen.studio.domain.layout.LayoutTemplateEngine
import com.damaskitchen.studio.domain.model.*
import com.damaskitchen.studio.domain.model.ModuleCoordinatesCm
import com.damaskitchen.studio.domain.model.ModuleDimensionsCm
import com.damaskitchen.studio.domain.model.PlacedCabinetModule
import com.damaskitchen.studio.domain.model.RoomDimensionsCm
import com.damaskitchen.studio.domain.model.WallSide
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * UI state wrapper that exposes both the underlying [KitchenProject] and the
 * transient studio selections (style preset, layout, lighting toggles, etc.).
 */
data class StudioUiState(
    val selectedStylePreset: CabinetStylePreset = DamasCatalog.stylePresets.first(),
    val selectedCountertop: CountertopMaterial = CountertopMaterial.entries.first(),
    val selectedBacksplash: BacksplashMaterial = BacksplashMaterial.entries.first(),
    val selectedFlooring: FlooringMaterial = FlooringMaterial.entries.first(),
    val selectedLayoutType: KitchenLayoutType = KitchenLayoutType.L_SHAPE,
    val warmCoveLightingEnabled: Boolean = true,
    val vitrineLed3000kEnabled: Boolean = true,
    val goldHardwareEnabled: Boolean = false,
    val selectedModuleId: String? = null,
    val statusBanner: String? = null
)

class KitchenStudioViewModel : ViewModel() {

    // ---------- Default data ---------------------------------------------------
    private val defaultRoom = RoomDimensionsCm(
        widthCm = 440,
        depthCm = 360,
        ceilingHeightCm = 285
    )

    private val defaultPreset = DamasCatalog.stylePresets.first()

    private val initialModules = LayoutTemplateEngine.generateTemplate(
        layoutType = KitchenLayoutType.L_SHAPE,
        room = defaultRoom,
        goldAccents = defaultPreset.hasGoldHardware
    )

    // ---------- Project flow ---------------------------------------------------
    private val _kitchenProjectFlow = MutableStateFlow(
        KitchenProject(
            id = "damas_project_01",
            name = "Damas Bespoke Studio",
            roomDimensions = defaultRoom,
            layoutType = KitchenLayoutType.L_SHAPE,
            materialConfig = MaterialConfiguration(
                stylePreset = defaultPreset,
                countertop = CountertopMaterial.entries.first(),
                backSplash = BacksplashMaterial.entries.first(),
                flooring = FlooringMaterial.entries.first(),
                warmCoveLighting = true,
                vitrine3000kLed = true
            ),
            modules = initialModules
        )
    )
    val kitchenProjectFlow: StateFlow<KitchenProject> = _kitchenProjectFlow.asStateFlow()

    // ---------- UI state flow --------------------------------------------------
    private val _studioUiState = MutableStateFlow(
        StudioUiState(
            selectedStylePreset = defaultPreset,
            selectedCountertop = CountertopMaterial.entries.first(),
            selectedBacksplash = BacksplashMaterial.entries.first(),
            selectedFlooring = FlooringMaterial.entries.first(),
            selectedLayoutType = KitchenLayoutType.L_SHAPE,
            goldHardwareEnabled = defaultPreset.hasGoldHardware,
            warmCoveLightingEnabled = true,
            vitrineLed3000kEnabled = true
        )
    )
    val studioUiState: StateFlow<StudioUiState> = _studioUiState.asStateFlow()

    // Simple incremental ID generator for newly added modules
    private var moduleSequence = 100

    // ---------- Public actions -------------------------------------------------
    fun applyStylePreset(preset: CabinetStylePreset) {
        _studioUiState.update {
            it.copy(
                selectedStylePreset = preset,
                goldHardwareEnabled = preset.hasGoldHardware,
                statusBanner = "Style preset applied: ${preset.name}"
            )
        }
        // Update project material configuration
        _kitchenProjectFlow.update { proj ->
            proj.copy(
                materialConfig = proj.materialConfig.copy(
                    stylePreset = preset,
                    // Preserve other material choices
                    warmCoveLighting = it.warmCoveLightingEnabled,
                    vitrine3000kLed = it.vitrineLed3000kEnabled
                )
            )
        }
    }

    fun selectLayout(layout: KitchenLayoutType) {
        _studioUiState.update { it.copy(selectedLayoutType = layout, statusBanner = "Layout: $layout") }
        // Regenerate modules based on the new layout
        val newModules = LayoutTemplateEngine.generateTemplate(
            layoutType = layout,
            room = _kitchenProjectFlow.value.roomDimensions,
            goldAccents = _studioUiState.value.goldHardwareEnabled
        )
        _kitchenProjectFlow.update { proj -> proj.copy(layoutType = layout, modules = newModules) }
    }

    fun toggleWarmCoveLighting(enabled: Boolean) {
        _studioUiState.update { it.copy(warmCoveLightingEnabled = enabled) }
        _kitchenProjectFlow.update { proj ->
            proj.copy(materialConfig = proj.materialConfig.copy(warmCoveLighting = enabled))
        }
    }

    fun toggleVitrineLed(enabled: Boolean) {
        _studioUiState.update { it.copy(vitrineLed3000kEnabled = enabled) }
        _kitchenProjectFlow.update { proj ->
            proj.copy(materialConfig = proj.materialConfig.copy(vitrine3000kLed = enabled))
        }
    }

    fun selectModule(moduleId: String?) {
        _studioUiState.update { it.copy(selectedModuleId = moduleId) }
    }

    /** Adds a new cabinet module at the given coordinates (in cm). */
    fun addCabinetModule(
        type: CabinetModuleType,
        wallSide: WallSide,
        origin: ModuleCoordinatesCm,
        dimensions: ModuleDimensionsCm
    ) {
        val newId = "mod_${moduleSequence++}"
        val newModule = PlacedCabinetModule(
            id = newId,
            type = type,
            wallSide = wallSide,
            originCm = origin,
            dimensionsCm = dimensions,
            rotationDeg = 0f
        )
        _kitchenProjectFlow.update { proj ->
            proj.copy(modules = proj.modules + newModule)
        }
        _studioUiState.update { it.copy(statusBanner = "Added ${type.name} module") }
    }

    /** Moves an existing module to a new origin (magnet‑snapped elsewhere). */
    fun moveModule(moduleId: String, newOrigin: ModuleCoordinatesCm) {
        _kitchenProjectFlow.update { proj ->
            val updatedModules = proj.modules.map { m ->
                if (m.id == moduleId) m.copy(originCm = newOrigin) else m
            }
            proj.copy(modules = updatedModules)
        }
        _studioUiState.update { it.copy(statusBanner = "Moved module $moduleId") }
    }

    /** Deletes a module from the project. */
    fun deleteModule(moduleId: String) {
        _kitchenProjectFlow.update { proj ->
            proj.copy(modules = proj.modules.filterNot { it.id == moduleId })
        }
        _studioUiState.update { it.copy(selectedModuleId = null, statusBanner = "Deleted module $moduleId") }
    }

    /** Updates the selected material choices (countertop, backsplash, flooring). */
    fun updateMaterialSelection(
        countertop: CountertopMaterial? = null,
        backsplash: BacksplashMaterial? = null,
        flooring: FlooringMaterial? = null
    ) {
        _studioUiState.update { state ->
            state.copy(
                selectedCountertop = countertop ?: state.selectedCountertop,
                selectedBacksplash = backsplash ?: state.selectedBacksplash,
                selectedFlooring = flooring ?: state.selectedFlooring,
                statusBanner = "Material selection updated"
            )
        }
        _kitchenProjectFlow.update { proj ->
            val cfg = proj.materialConfig
            proj.copy(
                materialConfig = cfg.copy(
                    countertop = countertop ?: cfg.countertop,
                    backSplash = backsplash ?: cfg.backSplash,
                    flooring = flooring ?: cfg.flooring
                )
            )
        }
    }
}
