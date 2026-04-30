package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.example.project.component.RootComponentImpl

fun main() = application {
    // 1. Создаем жизненный цикл
    val lifecycle = LifecycleRegistry()

    // 2. Создаем контекст и корневой компонент
    val root = RootComponentImpl(
        componentContext = DefaultComponentContext(lifecycle = lifecycle)
    )

    Window(onCloseRequest = ::exitApplication, title = "My Shopping List") {
        App(rootComponent = root) // ТЕПЕРЬ МЫ ПЕРЕДАЕМ ПАРАМЕТР
    }
}