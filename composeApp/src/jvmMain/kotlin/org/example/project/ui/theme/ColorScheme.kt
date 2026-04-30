package org.example.project.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
@ReadOnlyComposable
actual fun getApplicationColorScheme(useDarkTheme: Boolean): ColorScheme {
    return if (useDarkTheme) darkColorScheme() else lightColorScheme()
}