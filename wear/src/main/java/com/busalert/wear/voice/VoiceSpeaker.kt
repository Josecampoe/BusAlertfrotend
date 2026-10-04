package com.busalert.wear.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class VoiceSpeaker(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Intentamos configurar español de Colombia
            val colombiaLocale = Locale("es", "CO")
            val result = tts?.setLanguage(colombiaLocale)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback a español general si no está instalado el paquete colombiano en el reloj
                tts?.setLanguage(Locale("es", "ES"))
            }
            isReady = true
        }
    }

    fun speak(text: String) {
        if (isReady) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "busalert_voice")
        }
    }

    fun stop() {
        tts?.stop()
        tts?.shutdown()
    }
}
