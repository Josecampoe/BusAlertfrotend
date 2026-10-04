package com.busalert.wear.theme

import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BusAlertColors = Colors(
    primary = Vinotinto,
    primaryVariant = VinotintoDark,
    secondary = Vinotinto,
    secondaryVariant = VinotintoDark,
    background = SoftBackground,
    surface = SoftBackground,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun BusAlertTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = BusAlertColors,
        content = content
    )
}
