package com.busalert.shared.di

import com.busalert.shared.data.RemoteAIAssistantRepositoryImpl
import com.busalert.shared.data.MockTransitRepositoryImpl
import com.busalert.shared.data.api.VoiceApi
import com.busalert.shared.domain.AIAssistantRepository
import com.busalert.shared.domain.TransitRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SharedDependencies {
    
    // 10.0.2.2 es el "localhost" desde el punto de vista del emulador de Android
    private const val BACKEND_BASE_URL = "http://10.0.2.2:3000/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val voiceApi: VoiceApi by lazy {
        retrofit.create(VoiceApi::class.java)
    }

    // Ahora usamos el Repositorio Remoto (Backend real) en lugar del Simulado (Mock)
    val aiAssistantRepository: AIAssistantRepository by lazy {
        RemoteAIAssistantRepositoryImpl(voiceApi)
    }
    
    val transitRepository: TransitRepository by lazy {
        MockTransitRepositoryImpl()
    }
}
