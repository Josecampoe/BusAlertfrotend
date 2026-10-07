package com.busalert.wear.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

val BusAlertColors = Colors(
    primary          = Vinotinto,
    primaryVariant   = VinotintoDark,
    secondary        = VinotintoLight,
    secondaryVariant = VinotintoDark,
    background       = SoftWhite,
    surface          = PureWhite,
    onPrimary        = PureWhite,
    onSecondary      = PureWhite,
    onBackground     = TextOnLight,
    onSurface        = TextOnLight,
    error            = Color(0xFFB71C1C),
    onError          = PureWhite
)

@Composable
fun BusAlertTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors  = BusAlertColors,
        content = content
    )
}
