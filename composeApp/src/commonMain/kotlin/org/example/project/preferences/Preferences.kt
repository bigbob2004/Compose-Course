package org.example.project.preferences

import kotlinx.serialization.Serializable
import org.example.project.model.ShoppingItem
import androidx.compose.runtime.staticCompositionLocalOf

@Serializable
enum class ThemeConfig {
    SYSTEM, LIGHT, DARK
}

@Serializable
data class Preferences(
    val theme: ThemeConfig = ThemeConfig.SYSTEM,
    val shoppingList: List<ShoppingItem> = emptyList()
)

// Единственное место, где это объявлено
val LocalPreferences = staticCompositionLocalOf { Preferences() }