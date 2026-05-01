package org.example.project.model

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingItem(
    val id: String,          // Уникальный ID (понадобится для удаления и редактирования)
    val name: String,        // Название товара (например, "Лампочки для Cobalt")
    val isChecked: Boolean = false // Состояние: куплено или нет
)