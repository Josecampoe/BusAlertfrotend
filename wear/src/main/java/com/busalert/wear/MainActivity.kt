package com.busalert.wear

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.busalert.wear.theme.BusAlertTheme
import com.busalert.wear.ui.HomeScreen
import com.busalert.wear.ui.ListeningScreen
import com.busalert.wear.ui.ProcessingScreen
import com.busalert.wear.ui.ResultScreen
import com.busalert.wear.voice.VoiceSpeaker

class MainActivity : ComponentActivity() {
    private val viewModel: MainWearViewModel by viewModels()
    private lateinit var voiceSpeaker: VoiceSpeaker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        voiceSpeaker = VoiceSpeaker(this)
        
        setContent {
            BusAlertTheme {
                val appState by viewModel.appState.collectAsState()
                val lastResult by viewModel.lastResult.collectAsState()
                
                // Lanzador para encender el micrófono REAL del reloj/celular
                val speechLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
                        if (!spokenText.isNullOrEmpty()) {
                            viewModel.processVoiceInput(spokenText)
                        } else {
                            viewModel.resetToIdle()
                        }
                    } else {
                        viewModel.resetToIdle() // El usuario canceló o falló
                    }
                }
                
                when (appState) {
                    AppState.IDLE -> {
                        HomeScreen(onMicClick = { 
                            // Abrimos la pantalla nativa de reconocimiento de voz de Google
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-CO")
                                putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora...")
                            }
                            try {
                                speechLauncher.launch(intent)
                                viewModel.onMicrophoneClick()
                            } catch (e: Exception) {
                                // Error si el emulador no tiene instalada el app de Google
                                viewModel.resetToIdle()
                            }
                        })
                    }
                    AppState.LISTENING -> ListeningScreen()
                    AppState.PROCESSING -> ProcessingScreen()
                    AppState.RESULT -> {
                        lastResult?.let { result ->
                            voiceSpeaker.speak(result.spokenText)
                            
                            ResultScreen(
                                result = result, 
                                onDismiss = { viewModel.resetToIdle() }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceSpeaker.stop()
    }
}
