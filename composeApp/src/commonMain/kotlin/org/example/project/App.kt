package org.example.project

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.*
import androidx.compose.runtime.*
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.project.component.Config
import org.example.project.component.RootComponent
// Импорты твоих экранов из папки ui.screen
import org.example.project.ui.screen.HomeScreen
import org.example.project.ui.screen.AboutScreen
import org.example.project.ui.screen.SecondScreen

@Composable
fun App(rootComponent: RootComponent) {
    // Подписываемся на состояние стека навигации
    val childStack by rootComponent.childStack.subscribeAsState()
    val navSuiteState = rememberNavigationSuiteScaffoldState()

    // Логика скрытия навигационной панели на определенных экранах
    LaunchedEffect(childStack.active.configuration) {
        if (childStack.active.configuration is Config.MainScreen) {
            navSuiteState.show()
        } else {
            navSuiteState.hide()
        }
    }

    NavigationSuiteScaffold(
        state = navSuiteState,
        navigationItems = {
            // В версии 1.2.0 используем полное имя NavigationSuiteItem
            NavigationSuiteItem(
                selected = childStack.active.configuration is Config.Home,
                onClick = { rootComponent.navigate(Config.Home) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Главная") },
                label = { Text("Главная") }
            )
            NavigationSuiteItem(
                selected = childStack.active.configuration is Config.About,
                onClick = { rootComponent.navigate(Config.About) },
                icon = { Icon(Icons.Default.Info, contentDescription = "О нас") },
                label = { Text("О нас") }
            )
        }
    ) {
        // Отрисовка текущего активного экрана из стека Decompose
        Children(
            stack = childStack,
            animation = stackAnimation(fade()) // Плавная анимация перехода
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.Home -> HomeScreen(child.component)
                is RootComponent.Child.About -> AboutScreen(child.component)
                is RootComponent.Child.Second -> SecondScreen(child.component)
            }
        }
    }
}