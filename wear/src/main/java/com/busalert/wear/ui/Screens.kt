package com.busalert.wear.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.busalert.shared.domain.NavigationInstruction
import com.busalert.wear.theme.Vinotinto
import com.busalert.wear.theme.TextDark
import com.busalert.wear.theme.GradientEnd

// Fondo degradado radial muy elegante de blanco a un vinotinto ultraclaro en los bordes
val BeautifulBackground = Brush.radialGradient(
    colors = listOf(Color.White, GradientEnd)
)

@Composable
fun HomeScreen(onMicClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(BeautifulBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "BusAlert", 
            color = Vinotinto, 
            style = MaterialTheme.typography.title1,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onMicClick,
            modifier = Modifier.size(76.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor = Vinotinto)
        ) {
            Text("🎤", style = MaterialTheme.typography.title1)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Toca para hablar", color = TextDark, style = MaterialTheme.typography.body2)
    }
}

@Composable
fun ListeningScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(BeautifulBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            indicatorColor = Vinotinto, 
            trackColor = Color.LightGray,
            modifier = Modifier.size(64.dp),
            strokeWidth = 6.dp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Te escucho...", color = Vinotinto, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProcessingScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(BeautifulBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            indicatorColor = Vinotinto,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Calculando ruta...", color = TextDark, style = MaterialTheme.typography.body2)
    }
}

@Composable
fun ResultScreen(result: NavigationInstruction, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(BeautifulBackground).padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = result.displayTitle, 
            color = Vinotinto, 
            style = MaterialTheme.typography.title2,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = result.displaySubtitle, 
            color = TextDark, 
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onDismiss,
            modifier = Modifier.size(42.dp),
            colors = ButtonDefaults.secondaryButtonColors(backgroundColor = Color.LightGray)
        ) {
            Text("X", color = TextDark, fontWeight = FontWeight.Bold)
        }
    }
}
