package com.busalert.shared.data

import com.busalert.shared.data.api.VoiceApi
import com.busalert.shared.data.api.VoiceRequest
import com.busalert.shared.domain.AIAssistantRepository
import com.busalert.shared.domain.NavigationInstruction

class RemoteAIAssistantRepositoryImpl(
    private val voiceApi: VoiceApi
) : AIAssistantRepository {

    override suspend fun processVoiceCommand(
        voiceText: String,
        currentLocationName: String
    ): NavigationInstruction {
        return try {
            val request = VoiceRequest(voiceText, currentLocationName)
            // Hacemos la llamada real HTTP por Retrofit al backend
            voiceApi.processVoiceCommand(request)
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback en caso de que el servidor Node.js esté apagado o sin red
            NavigationInstruction(
                spokenText = "No pude conectarme al cerebro del sistema. Asegúrate de que el servidor Node está encendido.",
                displayTitle = "Backend Apagado",
                displaySubtitle = "Error de red",
                estimatedMinutes = null,
                recommendedRouteId = null
            )
        }
    }
}
