package org.example.project.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.component.ParkComponent


@Composable
fun App(rootComponent: Any? = null) {
    MaterialTheme {
        val component = remember { ParkComponent() }
        var infoText by remember { mutableStateOf("Загрузка данных о парке...") }

        LaunchedEffect(Unit) {
            component.loadParkData { result ->
                infoText = result
            }
        }

        Column(Modifier.fillMaxSize().padding(16.dp)) {
            TextField(
                value = infoText,
                onValueChange = {},
                modifier = Modifier.padding(bottom = 8.dp),
                label = { Text("Информация о парке") },
                readOnly = true
            )

            Button(onClick = { /* Логика навигации */ }) {
                Text("Перейти на второй экран")
            }
        }
    }
}