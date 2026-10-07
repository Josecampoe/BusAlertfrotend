package com.busalert.wear

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.busalert.wear.theme.BusAlertTheme
import com.busalert.wear.ui.*
import com.busalert.wear.voice.VoiceSpeaker

class MainActivity : ComponentActivity() {
    private val viewModel: MainWearViewModel by viewModels()
    private lateinit var voiceSpeaker: VoiceSpeaker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        voiceSpeaker = VoiceSpeaker(this)

        setContent {
            BusAlertTheme {
                val appState     by viewModel.appState.collectAsState()
                val lastResult   by viewModel.lastResult.collectAsState()
                val errorMessage by viewModel.errorMessage.collectAsState()
                val typedText    by viewModel.typedText.collectAsState()

                // ── Launcher de permiso de micrófono ──────────────────────
                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { granted ->
                    if (granted) launchSpeechRecognizer()
                    else viewModel.onSpeechFailed()   // sin permiso → modo escritura
                }

                // ── Launcher del reconocimiento de voz del sistema ────────
                val speechLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val text = result.data
                            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                            ?.firstOrNull()
                        if (!text.isNullOrEmpty()) {
                            viewModel.processVoiceInput(text)
                        } else {
                            // STT no devolvió texto → fallback a escritura
                            viewModel.onSpeechFailed()
                        }
                    } else {
                        // El emulador no tiene STT instalado → fallback a escritura
                        viewModel.onSpeechFailed()
                    }
                }

                // Guarda el launcher para usarlo desde launchSpeechRecognizer()
                val speechLauncherRef = remember { mutableStateOf(speechLauncher) }
                speechLauncherRef.value = speechLauncher

                // ── TTS: solo se dispara una vez cuando llega un resultado ─
                LaunchedEffect(lastResult) {
                    if (appState == AppState.RESULT) {
                        lastResult?.spokenText?.let { voiceSpeaker.speak(it) }
                    }
                }

                // ── Navegación por pantallas ───────────────────────────────
                when (appState) {
                    AppState.IDLE -> HomeScreen(onMicClick = {
                        val hasPerm = ContextCompat.checkSelfPermission(
                            this@MainActivity, Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED

                        val hasSpeechService = SpeechRecognizer.isRecognitionAvailable(this@MainActivity)

                        if (!hasPerm) {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        } else if (!hasSpeechService) {
                            // Emulador sin Google STT → modo escritura directo
                            viewModel.onSpeechFailed()
                        } else {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-CO")
                                putExtra(RecognizerIntent.EXTRA_PROMPT, "¿A dónde quieres ir?")
                                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                            }
                            try {
                                viewModel.onMicrophoneClick()
                                speechLauncherRef.value.launch(intent)
                            } catch (e: Exception) {
                                viewModel.onSpeechFailed()
                            }
                        }
                    })

                    AppState.LISTENING  -> ListeningScreen()
                    AppState.PROCESSING -> ProcessingScreen()

                    AppState.RESULT -> lastResult?.let { result ->
                        ResultScreen(
                            result   = result,
                            onDismiss = { viewModel.resetToIdle() }
                        )
                    }

                    AppState.ERROR -> ErrorScreen(
                        message = errorMessage,
                        onRetry = { viewModel.resetToIdle() }
                    )

                    // Fallback para emulador: el usuario escribe la consulta
                    AppState.TYPING -> TypingScreen(
                        inputText    = typedText,
                        onTextChange = { viewModel.onTypedTextChange(it) },
                        onSubmit     = { viewModel.submitTypedText() },
                        onCancel     = { viewModel.resetToIdle() }
                    )
                }
            }
        }
    }

    private fun launchSpeechRecognizer() {
        // No-op: el launcher se ejecuta desde la composición
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceSpeaker.stop()
    }
}
