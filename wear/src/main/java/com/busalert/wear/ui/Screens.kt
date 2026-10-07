package com.busalert.wear.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.busalert.shared.domain.NavigationInstruction
import com.busalert.wear.theme.*

// ── Fondos ──────────────────────────────────────────────────
val PrimaryBackground = Brush.radialGradient(
    colors = listOf(Color(0xFFFFF0F3), Color(0xFFFFE0E6), Color(0xFFF5C8D0))
)
val DarkBackground = Brush.radialGradient(
    colors = listOf(Color(0xFF5C0018), Color(0xFF3A000F), Color(0xFF200008))
)

// ── HOME SCREEN ─────────────────────────────────────────────
@Composable
fun HomeScreen(onMicClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.22f,
        animationSpec = infiniteRepeatable(
            tween(1400, easing = EaseInOutSine), RepeatMode.Reverse
        ), label = "ring"
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            tween(1400, easing = EaseInOutSine), RepeatMode.Reverse
        ), label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Título
            Text(
                text = "BusAlert",
                color = Vinotinto,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "PASTO",
                color = VinotintoLight,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Botón micrófono con anillo animado
            Box(contentAlignment = Alignment.Center) {
                // Anillo exterior pulsante
                Box(
                    modifier = Modifier
                        .size(78.dp)
                        .scale(ringScale)
                        .clip(CircleShape)
                        .background(Vinotinto.copy(alpha = ringAlpha))
                )
                // Botón
                Button(
                    onClick = onMicClick,
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.primaryButtonColors(backgroundColor = Vinotinto)
                ) {
                    Text(text = "🎤", fontSize = 24.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

// ── LISTENING SCREEN ────────────────────────────────────────
@Composable
fun ListeningScreen() {
    val infiniteTransition = rememberInfiniteTransition(label = "listen")

    val scales = listOf(
        infiniteTransition.animateFloat(
            initialValue = 0.5f, targetValue = 1.5f,
            animationSpec = infiniteRepeatable(tween(520, 0, EaseInOutSine), RepeatMode.Reverse),
            label = "s1"
        ),
        infiniteTransition.animateFloat(
            initialValue = 0.5f, targetValue = 1.5f,
            animationSpec = infiniteRepeatable(tween(520, 175, EaseInOutSine), RepeatMode.Reverse),
            label = "s2"
        ),
        infiniteTransition.animateFloat(
            initialValue = 0.5f, targetValue = 1.5f,
            animationSpec = infiniteRepeatable(tween(520, 350, EaseInOutSine), RepeatMode.Reverse),
            label = "s3"
        )
    )

    Box(
        modifier = Modifier.fillMaxSize().background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🎤",
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Te escucho...",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(14.dp))
            // Tres puntos animados vino tinto
            Row(
                horizontalArrangement = Arrangement.spacedBy(9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                scales.forEach { scaleState ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .scale(scaleState.value)
                            .clip(CircleShape)
                            .background(VinotintoLight)
                    )
                }
            }
        }
    }
}

// ── PROCESSING SCREEN ───────────────────────────────────────
@Composable
fun ProcessingScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator(
                indicatorColor = VinotintoLight,
                trackColor = Color.White.copy(alpha = 0.12f),
                modifier = Modifier.size(50.dp),
                strokeWidth = 5.dp
            )
            Text(
                text = "Calculando ruta...",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ── RESULT SCREEN ───────────────────────────────────────────
@Composable
fun ResultScreen(result: NavigationInstruction, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // Chip de ruta
            result.recommendedRouteId?.let { routeId ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Vinotinto)
                        .padding(horizontal = 14.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = routeId,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            // Título
            Text(
                text = result.displayTitle,
                color = Vinotinto,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 17.sp
            )

            // Subtítulo
            Text(
                text = result.displaySubtitle,
                color = TextMedium,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 13.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Botón cerrar
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(28.dp),
                colors = ButtonDefaults.secondaryButtonColors(
                    backgroundColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
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
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ── ERROR SCREEN ────────────────────────────────────────────
@Composable
fun ErrorScreen(message: String?, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "⚠️", fontSize = 22.sp)
            Text(
                text = "Sin conexión",
                color = Vinotinto,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = message ?: "Verifica que el servidor esté encendido.",
                color = TextMedium,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 13.sp
            )
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth(0.6f).height(28.dp),
                colors = ButtonDefaults.primaryButtonColors(backgroundColor = Vinotinto),
                shape = RoundedCornerShape(50)
            ) {
                Text("Reintentar", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ── TYPING SCREEN (entrada manual de texto) ─────────────────
@Composable
fun TypingScreen(
    inputText: String,
    onTextChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "¿A dónde vas?",
                color = Vinotinto,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            // Campo de texto simple
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(1.5.dp, Vinotinto, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (inputText.isEmpty()) {
                    Text("Ej: quiero ir al Lorenzo", color = TextLight, fontSize = 10.sp)
                } else {
                    Text(inputText, color = TextDark, fontSize = 10.sp)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.secondaryButtonColors(backgroundColor = Color.LightGray)
                ) { Text("✕", fontSize = 12.sp, color = TextDark) }
                Button(
                    onClick = onSubmit,
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.primaryButtonColors(backgroundColor = Vinotinto)
                ) { Text("✓", fontSize = 12.sp, color = Color.White) }
            }
        }
    }
}
