package org.example.project.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
@ReadOnlyComposable
expect fun getApplicationColorScheme(useDarkTheme: Boolean = isSystemInDarkTheme()): ColorScheme