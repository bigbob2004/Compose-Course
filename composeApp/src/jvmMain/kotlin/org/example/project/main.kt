package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import okio.Path.Companion.toPath
import org.example.project.component.RootComponentImpl
import org.example.project.preferences.createDataStore
import java.io.File

fun main() {
    val lifecycle = LifecycleRegistry()

    // Создаем путь в домашней папке пользователя, чтобы точно были права на запись
    val dataStoreFile = File(System.getProperty("user.home"), "shopping_list.preferences_pb")

    val dataStore = createDataStore(
        fileSystem = okio.FileSystem.SYSTEM,
        producePath = { dataStoreFile.absolutePath.toPath() }
    )

    val root = RootComponentImpl(
        componentContext = DefaultComponentContext(lifecycle),
        dataStore = dataStore
    )

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "My Shopping List"
        ) {
            App(root)
        }
    }
}