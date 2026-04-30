package org.example.project.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.component.HomeComponent

@Composable
fun HomeScreen(component: HomeComponent) {
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Введите текст") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { component.navigateToSecond(text) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Перейти на второй экран")
        }
    }
}