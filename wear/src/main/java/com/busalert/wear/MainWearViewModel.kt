package com.busalert.wear

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.busalert.shared.di.SharedDependencies
import com.busalert.shared.domain.NavigationInstruction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppState { IDLE, LISTENING, PROCESSING, RESULT }

class MainWearViewModel : ViewModel() {
    private val aiRepository = SharedDependencies.aiAssistantRepository

    private val _appState = MutableStateFlow(AppState.IDLE)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    private val _lastResult = MutableStateFlow<NavigationInstruction?>(null)
    val lastResult: StateFlow<NavigationInstruction?> = _lastResult.asStateFlow()

    fun onMicrophoneClick() {
        _appState.value = AppState.LISTENING
    }

    // Simulamos la entrada de voz ya procesada a texto
    fun processVoiceInput(spokenText: String, currentLocation: String = "La Boyacá") {
        _appState.value = AppState.PROCESSING
        viewModelScope.launch {
            try {
                val instruction = aiRepository.processVoiceCommand(spokenText, currentLocation)
                _lastResult.value = instruction
                _appState.value = AppState.RESULT
            } catch (e: Exception) {
                // Fallback en caso de error
                _lastResult.value = NavigationInstruction(
                    "Error de conexión.", "Error", "Intenta de nuevo"
                )
                _appState.value = AppState.RESULT
            }
        }
    }

    fun resetToIdle() {
        _appState.value = AppState.IDLE
    }
}
