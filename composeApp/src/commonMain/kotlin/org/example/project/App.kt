package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.coroutines.launch
import org.example.project.ui.theme.getApplicationColorScheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun App() {
    // Используем нашу кроссплатформенную схему цветов
    MaterialTheme(colorScheme = getApplicationColorScheme()) {
        val shoppingList = remember { mutableStateListOf<Pair<String, Boolean>>() }
        var inputText by rememberSaveable { mutableStateOf("") }
        var showDeleteDialog by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        // Определяем класс размера окна для адаптивного дизайна
        val adaptiveInfo = currentWindowAdaptiveInfo()
        val isCompact = adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Список покупок") },
                    actions = {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Очистить всё")
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    if (inputText.isNotBlank()) {
                        shoppingList.add(inputText.trim() to false)
                        inputText = ""
                    } else {
                        // Показ снекбара через корутину
                        scope.launch {
                            snackbarHostState.showSnackbar("Введите название товара")
                        }
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .padding(horizontal = 4.dp) // Минимальный боковой отступ
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    label = { Text("Новый продукт") },
                    singleLine = true
                )

                // Адаптивная разметка: список для телефонов, сетка для больших экранов
                if (isCompact) {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        itemsIndexed(shoppingList) { index, item ->
                            ShoppingItemRow(
                                item = item,
                                onCheckedChange = { isChecked ->
                                    shoppingList[index] = item.first to isChecked
                                },
                                onDelete = { shoppingList.removeAt(index) }
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.weight(1f)
                    ) {
                        itemsIndexed(shoppingList) { index, item ->
                            ShoppingItemRow(
                                item = item,
                                onCheckedChange = { isChecked ->
                                    shoppingList[index] = item.first to isChecked
                                },
                                onDelete = { shoppingList.removeAt(index) }
                            )
                        }
                    }
                }
            }

            // Диалог подтверждения для необратимых действий
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            shoppingList.clear()
                            showDeleteDialog = false
                            scope.launch { snackbarHostState.showSnackbar("Список очищен") }
                        }) { Text("Очистить") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) { Text("Отмена") }
                    },
                    title = { Text("Удалить всё?") },
                    text = { Text("Вы уверены, что хотите полностью очистить список покупок?") },
                    icon = { Icon(Icons.Default.Warning, contentDescription = null) }
                )
            }
        }
    }
}

@Composable
fun ShoppingItemRow(
    item: Pair<String, Boolean>,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = item.second, onCheckedChange = onCheckedChange)
        Text(
            text = item.first,
            modifier = Modifier.weight(1f).padding(start = 8.dp),
            style = LocalTextStyle.current.copy(
                textDecoration = if (item.second) TextDecoration.LineThrough else TextDecoration.None
            )
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.Red)
        }
    }
}