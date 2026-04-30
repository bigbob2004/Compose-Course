package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.example.project.ui.theme.getApplicationColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme(colorScheme = getApplicationColorScheme()) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("My Shopping List") }) }
        ) { contentPadding ->
            val shoppingList = remember { mutableStateListOf<Pair<String, Boolean>>() }
            // rememberSaveable сохраняет ввод при повороте устройства
            var inputText by rememberSaveable { mutableStateOf("") }

            Column(modifier = Modifier.padding(contentPadding).fillMaxSize()) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Продукт") },
                        singleLine = true
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        if (inputText.isNotBlank()) {
                            shoppingList.add(inputText.trim() to false)
                            inputText = ""
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(shoppingList) { index, item ->
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = item.second, onCheckedChange = { shoppingList[index] = item.first to it })
                            Text(
                                text = item.first,
                                modifier = Modifier.weight(1f).padding(start = 8.dp),
                                style = LocalTextStyle.current.copy(
                                    textDecoration = if (item.second) TextDecoration.LineThrough else TextDecoration.None,
                                    color = if (item.second) Color.Gray else Color.Unspecified
                                )
                            )
                            IconButton(onClick = { shoppingList.removeAt(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}