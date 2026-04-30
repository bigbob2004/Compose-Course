package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// НОВЫЙ ИМПОРТ ТУТ:
import myshoppinglist.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

data class ShoppingListItem(val description: String, val bought: Boolean = false)

@Composable
fun App() {
    val shoppingList = remember { mutableStateListOf(ShoppingListItem("Молоко"), ShoppingListItem("Мука")) }
    var newItemDesc by remember { mutableStateOf("") }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                OutlinedTextField(
                    value = newItemDesc,
                    onValueChange = { newItemDesc = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    label = { Text(stringResource(Res.string.input_placeholder)) },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (newItemDesc.isNotBlank()) {
                                shoppingList.add(ShoppingListItem(newItemDesc.trim()))
                                newItemDesc = ""
                            }
                        }) {
                            Icon(Icons.Default.Add, contentDescription = stringResource(Res.string.add_button))
                        }
                    }
                )

                LazyColumn {
                    itemsIndexed(shoppingList) { i, item ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                            Checkbox(checked = item.bought, onCheckedChange = { shoppingList[i] = item.copy(bought = it) })
                            Text(item.description, Modifier.weight(1f))
                            IconButton(onClick = { shoppingList.removeAt(i) }) {
                                Icon(Icons.Default.Delete, contentDescription = stringResource(Res.string.delete_button))
                            }
                        }
                    }
                }
            }
        }
    }
}