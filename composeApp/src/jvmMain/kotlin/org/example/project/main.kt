package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.project.ui.App // Добавлен импорт, так как App теперь в папке ui

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "My Shopping List",
    ) {
        // Вызываем App и передаем null, так как мы добавили этот параметр для совместимости
        App(rootComponent = null)
    }
}