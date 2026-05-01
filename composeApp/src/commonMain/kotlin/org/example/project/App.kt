package org.example.project

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
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

@Composable
fun App(rootComponent: RootComponent) {
    val preferences by rootComponent.preferences.collectAsState(initial = Preferences())

    val darkTheme = when (preferences.theme) {
        ThemeConfig.SYSTEM -> isSystemInDarkTheme()
        ThemeConfig.LIGHT -> false
        ThemeConfig.DARK -> true
    }

    // CompositionLocalProvider требует импорта androidx.compose.runtime.*
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
                    IconButton(onClick = { component.navigateToSecond("Settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Что купить?") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            component.addItem(text)
                            text = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                                Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                    HorizontalDivider(thickness = 0.5.dp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(component: SettingsComponent) {
    val currentPreferences = LocalPreferences.current
    val currentTheme = currentPreferences.theme

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = { component.goBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            Text(text = "Выбор темы", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            ThemeOption("Системная", ThemeConfig.SYSTEM, currentTheme) { component.updateTheme(it) }
            ThemeOption("Светлая", ThemeConfig.LIGHT, currentTheme) { component.updateTheme(it) }
            ThemeOption("Темная", ThemeConfig.DARK, currentTheme) { component.updateTheme(it) }
        }
    }
}

@Composable
fun ThemeOption(
    label: String,
    option: ThemeConfig,
    selectedOption: ThemeConfig,
    onSelect: (ThemeConfig) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = (option == selectedOption),
                onClick = { onSelect(option) }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (option == selectedOption),
            onClick = { onSelect(option) }
        )
        Text(text = label, modifier = Modifier.padding(start = 16.dp))
    }
}