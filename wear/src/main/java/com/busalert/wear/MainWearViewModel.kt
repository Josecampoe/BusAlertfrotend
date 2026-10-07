package com.busalert.wear

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.busalert.shared.di.SharedDependencies
import com.busalert.shared.domain.NavigationInstruction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppState { IDLE, LISTENING, PROCESSING, RESULT, ERROR, TYPING }

class MainWearViewModel : ViewModel() {
    private val aiRepository = SharedDependencies.aiAssistantRepository

    private val _appState = MutableStateFlow(AppState.IDLE)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    private val _lastResult = MutableStateFlow<NavigationInstruction?>(null)
    val lastResult: StateFlow<NavigationInstruction?> = _lastResult.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _typedText = MutableStateFlow("")
    val typedText: StateFlow<String> = _typedText.asStateFlow()

    fun onMicrophoneClick() {
        _appState.value = AppState.LISTENING
    }

    // Llamado cuando el RecognizerIntent devuelve texto exitosamente
    fun processVoiceInput(spokenText: String, currentLocation: String = "Pasto, Nariño") {
        _appState.value = AppState.PROCESSING
        viewModelScope.launch {
            try {
                val instruction = aiRepository.processVoiceCommand(spokenText, currentLocation)
                val isError = instruction.recommendedRouteId == null && (
                    instruction.displayTitle.contains("Error", ignoreCase = true) ||
                    instruction.displayTitle.contains("Apagado", ignoreCase = true) ||
                    instruction.displayTitle.contains("conexión", ignoreCase = true) ||
                    instruction.displayTitle.contains("conexion", ignoreCase = true)
                )
                if (isError) {
                    _errorMessage.value = instruction.spokenText
                    _appState.value = AppState.ERROR
                } else {
                    _lastResult.value = instruction
                    _appState.value = AppState.RESULT
                }
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo conectar con el servidor. Verifica que el backend esté encendido."
                _appState.value = AppState.ERROR
            }
        }
    }

    // Fallback: el usuario escribe la consulta manualmente (para emulador sin micrófono)
    fun onSpeechFailed() {
        _appState.value = AppState.TYPING
    }

    fun onTypedTextChange(text: String) {
        _typedText.value = text
    }

    fun submitTypedText() {
        val text = _typedText.value.trim()
        if (text.isNotEmpty()) {
            processVoiceInput(text)
            _typedText.value = ""
        }
    }

    fun resetToIdle() {
        _appState.value = AppState.IDLE
        _lastResult.value = null
        _errorMessage.value = null
        _typedText.value = ""
    }
}
