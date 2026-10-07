package com.busalert.wear.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.busalert.shared.domain.NavigationInstruction
import com.busalert.wear.theme.*

// ─────────────────────────────────────────────────────────────
// FONDOS reutilizables
// ─────────────────────────────────────────────────────────────
private val BgLight = Brush.radialGradient(
    colors = listOf(GradientLight1, GradientLight2, GradientLight3)
)

private val BgDark = Brush.radialGradient(
    colors = listOf(SurfaceDark, VinotintoDark, VinotintoDeep)
)

// ─────────────────────────────────────────────────────────────
// HOME SCREEN
// Círculo vino tinto limpio, sin emoji, con tres anillos
// concéntricos que pulsan hacia afuera al ritmo del corazón.
// ─────────────────────────────────────────────────────────────
@Composable
fun HomeScreen(onMicClick: () -> Unit) {

    val inf = rememberInfiniteTransition(label = "home")

    // Tres anillos con delay escalonado
    val ring1 by inf.animateFloat(
        initialValue = 0.75f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1600, 0, EaseOut), RepeatMode.Restart),
        label = "r1"
    )
    val ring1Alpha by inf.animateFloat(
        initialValue = 0.5f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1600, 0, EaseOut), RepeatMode.Restart),
        label = "a1"
    )
    val ring2 by inf.animateFloat(
        initialValue = 0.75f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1600, 500, EaseOut), RepeatMode.Restart),
        label = "r2"
    )
    val ring2Alpha by inf.animateFloat(
        initialValue = 0.5f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1600, 500, EaseOut), RepeatMode.Restart),
        label = "a2"
    )
    val ring3 by inf.animateFloat(
        initialValue = 0.75f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1600, 1000, EaseOut), RepeatMode.Restart),
        label = "r3"
    )
    val ring3Alpha by inf.animateFloat(
        initialValue = 0.5f, targetValue = 0f,
        animationSpec = infiniteRepeatable(tween(1600, 1000, EaseOut), RepeatMode.Restart),
        label = "a3"
    )

    Box(
        modifier = Modifier.fillMaxSize().background(BgLight),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Texto superior
            Text(
                text = "BusAlert",
                color = Vinotinto,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "P A S T O",
                color = VinotintoSoft,
                fontSize = 8.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón con anillos pulsantes
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(100.dp)
            ) {
                // Anillo 3 (más exterior)
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .scale(ring3)
                        .clip(CircleShape)
                        .background(Vinotinto.copy(alpha = ring3Alpha * 0.25f))
                )
                // Anillo 2
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .scale(ring2)
                        .clip(CircleShape)
                        .background(Vinotinto.copy(alpha = ring2Alpha * 0.35f))
                )
                // Anillo 1 (interior)
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .scale(ring1)
                        .clip(CircleShape)
                        .background(Vinotinto.copy(alpha = ring1Alpha * 0.45f))
                )

                // Botón central — círculo sólido sin icono
                Button(
                    onClick = onMicClick,
                    modifier = Modifier.size(58.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.primaryButtonColors(
                        backgroundColor = Vinotinto
                    )
                ) {
                    // Línea decorativa horizontal (símbolo minimalista de "hablar")
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Tres líneas cortas que simulan ondas de voz
                            WaveLine(width = 20.dp, alpha = 1f)
                            WaveLine(width = 14.dp, alpha = 0.75f)
                            WaveLine(width = 10.dp, alpha = 0.5f)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WaveLine(width: Dp, alpha: Float) {
    Box(
        modifier = Modifier
            .width(width)
            .height(2.dp)
            .clip(RoundedCornerShape(50))
            .background(PureWhite.copy(alpha = alpha))
    )
}

// ─────────────────────────────────────────────────────────────
// LISTENING SCREEN
// Visualizador de audio: 7 barras que suben y bajan
// simulando que el reloj está capturando la voz.
// ─────────────────────────────────────────────────────────────
@Composable
fun ListeningScreen() {
    val inf = rememberInfiniteTransition(label = "audio")

    val delays = listOf(0, 120, 240, 60, 300, 180, 90)
    val heights = delays.mapIndexed { i, delay ->
        inf.animateFloat(
            initialValue = 0.2f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(500 + i * 40, delay, EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bar$i"
        )
    }

    Box(
        modifier = Modifier.fillMaxSize().background(BgDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Escuchando",
                color = PureWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Visualizador de barras de audio
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(40.dp)
            ) {
                heights.forEachIndexed { index, heightState ->
                    val barHeight = (40 * heightState.value).dp
                    val isCenter = index == 3
                    Box(
                        modifier = Modifier
                            .width(if (isCenter) 5.dp else 4.dp)
                            .height(barHeight)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isCenter) PureWhite
                                else PureWhite.copy(alpha = 0.6f + heightState.value * 0.4f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Habla ahora...",
                color = WhiteAlpha50,
                fontSize = 10.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// PROCESSING SCREEN
// Arco giratorio elegante con puntos en los extremos.
// ─────────────────────────────────────────────────────────────
@Composable
fun ProcessingScreen() {
    val inf = rememberInfiniteTransition(label = "proc")
    val rotation by inf.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "rot"
    )
    val pulse by inf.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(800, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(
        modifier = Modifier.fillMaxSize().background(BgDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Spinner circular personalizado
            Box(
                modifier = Modifier.size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(56.dp)
                        .scale(pulse),
                    indicatorColor = PureWhite,
                    trackColor = WhiteAlpha20,
                    strokeWidth = 3.dp
                )
                // Punto central
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(VinotintoLight)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Calculando ruta",
                color = PureWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Un momento...",
                color = WhiteAlpha50,
                fontSize = 10.sp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// RESULT SCREEN
// Fondo claro. Chip de ruta vino tinto arriba.
// Línea divisora sutil. Botón cerrar con borde.
// ─────────────────────────────────────────────────────────────
@Composable
fun ResultScreen(result: NavigationInstruction, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(BgLight),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Chip de ruta
            result.recommendedRouteId?.let { id ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Vinotinto)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = id,
                        color = PureWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                }
            }

            // Título
            Text(
                text = result.displayTitle,
                color = Vinotinto,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

            // Línea divisora
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(1.dp)
                    .background(VinotintoSoft.copy(alpha = 0.4f))
            )

            // Subtítulo
            Text(
                text = result.displaySubtitle,
                color = TextSubtle,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 13.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Botón cerrar — borde vino tinto, fondo transparente
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(28.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.secondaryButtonColors(
                    backgroundColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, Vinotinto, RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cerrar",
                        color = Vinotinto,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// ERROR SCREEN
// Minimalista. Círculo con signo de exclamación dibujado
// con Box, sin emojis. Botón reintentar vino tinto.
// ─────────────────────────────────────────────────────────────
@Composable
fun ErrorScreen(message: String?, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(BgLight),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            // Círculo con "!" hecho con Box (sin emoji)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Vinotinto),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(11.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(PureWhite)
                    )
                    Box(
                        modifier = Modifier
                            .size(3.dp)
                            .clip(CircleShape)
                            .background(PureWhite)
                    )
                }
            }

            Text(
                text = "Sin conexión",
                color = Vinotinto,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = message ?: "Verifica que el servidor esté activo.",
                color = TextSubtle,
                fontSize = 9.sp,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )

            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(0.6f).height(28.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.primaryButtonColors(backgroundColor = Vinotinto)
            ) {
                Text(
                    text = "Reintentar",
                    color = PureWhite,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// TYPING SCREEN
// Campo de texto estilo tarjeta blanca con borde vino tinto.
// Botón enviar vino tinto sólido. Sin emojis.
// ─────────────────────────────────────────────────────────────
@Composable
fun TypingScreen(
    inputText: String,
    onTextChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(BgDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "¿A dónde vas?",
                color = PureWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            // Campo de texto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(PureWhite)
                    .border(2.dp, Vinotinto, RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 7.dp)
            ) {
                BasicTextField(
                    value = inputText,
                    onValueChange = onTextChange,
                    textStyle = TextStyle(
                        color = TextOnLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    cursorBrush = SolidColor(Vinotinto),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                ) { inner ->
                    Box {
                        if (inputText.isEmpty()) {
                            Text(
                                text = "Ej: quiero ir al Lorenzo",
                                color = TextSubtle.copy(alpha = 0.6f),
                                fontSize = 11.sp
                            )
                        }
                        inner()
                    }
                }
            }

            // Botones
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Cancelar — círculo con línea diagonal (X sin emoji)
                Button(
                    onClick = onCancel,
                    modifier = Modifier.size(34.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.secondaryButtonColors(
                        backgroundColor = WhiteAlpha20
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✕",
                            color = PureWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Enviar — círculo vino tinto con check
                Button(
                    onClick = onSubmit,
                    modifier = Modifier.size(34.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.primaryButtonColors(
                        backgroundColor = Vinotinto
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✓",
                            color = PureWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
