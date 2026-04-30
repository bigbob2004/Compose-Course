package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import org.example.project.component.RootComponentImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Создаем контекст Decompose, привязанный к жизненному циклу Activity
        val root = RootComponentImpl(
            componentContext = defaultComponentContext()
        )

        setContent {
            // 2. Передаем созданный компонент в главную функцию приложения
            App(rootComponent = root)
        }
    }
}