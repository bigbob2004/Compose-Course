package org.example.project

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import org.example.project.component.RootComponent
import org.example.project.component.HomeComponent
import org.example.project.component.SettingsComponent
import org.example.project.preferences.LocalPreferences
import org.example.project.preferences.Preferences
import org.example.project.preferences.ThemeConfig
import org.example.project.permissions.*

@Composable
fun App(rootComponent: RootComponent) {
    val preferences by rootComponent.preferences.collectAsState(initial = Preferences())

    val darkTheme = when (preferences.theme) {
        ThemeConfig.SYSTEM -> isSystemInDarkTheme()
        ThemeConfig.LIGHT -> false
        ThemeConfig.DARK -> true
    }

    CompositionLocalProvider(LocalPreferences provides preferences) {
        MaterialTheme(
            colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                Children(
                    stack = rootComponent.stack,
                    animation = stackAnimation(slide())
                ) { child ->
                    when (val instance = child.instance) {
                        is RootComponent.Child.Home -> HomeScreen(instance.component)
                        is RootComponent.Child.Settings -> SettingsScreen(instance.component)
                        is RootComponent.Child.Permissions -> PermissionScreen(instance.component)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(component: HomeComponent) {
    val preferences = LocalPreferences.current
    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Мои покупки") },
                actions = {
                    // Исправлено: используем новые методы навигации
                    IconButton(onClick = { component.navigateToPermissions() }) {
                        Icon(Icons.Default.Info, contentDescription = "Разрешения")
                    }
                    IconButton(onClick = { component.navigateToSettings() }) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Что купить?") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(onClick = {
                    if (text.isNotBlank()) {
                        component.addItem(text)
                        text = ""
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                }
            }
            LazyColumn {
                items(preferences.shoppingList) { item ->
                    ListItem(
                        headlineContent = { Text(item.name) },
                        leadingContent = {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = { component.toggleItem(item.id) }
                            )
                        },
                        trailingContent = {
                            IconButton(onClick = { component.removeItem(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Удалить")
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionScreen(component: SettingsComponent) {
    var lastResult by remember { mutableStateOf("Ожидание запроса") }
    val permissionManager = rememberPermissionManager { isGranted ->
        lastResult = if (isGranted) "Разрешено!" else "Отказано"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Разрешения") },
                navigationIcon = {
                    IconButton(onClick = { component.goBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Результат: $lastResult", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { permissionManager.askPermission(PermissionType.CAMERA) }, modifier = Modifier.fillMaxWidth()) {
                Text("Запросить камеру")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { permissionManager.askPermission(PermissionType.LOCATION) }, modifier = Modifier.fillMaxWidth()) {
                Text("Запросить GPS")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { permissionManager.askPermission(PermissionType.MICROPHONE) }, modifier = Modifier.fillMaxWidth()) {
                Text("Запросить микрофон")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(component: SettingsComponent) {
    val currentPreferences = LocalPreferences.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = { component.goBack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Тема", style = MaterialTheme.typography.titleLarge)
            ThemeOption("Система", ThemeConfig.SYSTEM, currentPreferences.theme) { component.updateTheme(it) }
            ThemeOption("Светлая", ThemeConfig.LIGHT, currentPreferences.theme) { component.updateTheme(it) }
            ThemeOption("Темная", ThemeConfig.DARK, currentPreferences.theme) { component.updateTheme(it) }
        }
    }
}

@Composable
fun ThemeOption(label: String, option: ThemeConfig, selected: ThemeConfig, onSelect: (ThemeConfig) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp).selectable(
            selected == option,
            onClick = { onSelect(option) }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected == option, onClick = { onSelect(option) })
        Text(label, modifier = Modifier.padding(start = 16.dp))
    }
}