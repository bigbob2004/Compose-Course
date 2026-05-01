package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import okio.Path.Companion.toPath
import org.example.project.component.RootComponentImpl
import org.example.project.preferences.createDataStore
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Создаем путь к файлу настроек во внутреннем хранилище Android
        val dataStoreFile = File(applicationContext.filesDir, "shopping_list.preferences_pb")

        // 2. Инициализируем DataStore для Android
        val dataStore = createDataStore(
            fileSystem = okio.FileSystem.SYSTEM,
            producePath = { dataStoreFile.absolutePath.toPath() }
        )

        // 3. Передаем созданный dataStore в компонент
        val root = RootComponentImpl(
            componentContext = defaultComponentContext(),
            dataStore = dataStore
        )

        setContent {
            App(root)
        }
    }
}