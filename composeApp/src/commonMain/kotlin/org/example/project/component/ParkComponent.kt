package org.example.project.component

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.request.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.example.project.ParkData

object NetworkClient {
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
}

class ParkComponent {
    private val componentScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun loadParkData(onResult: (String) -> Unit) {
        componentScope.launch {
            try {
                // Временный URL для проверки (вернет моковые данные)
                val response: HttpResponse = NetworkClient.httpClient.get("https://raw.githubusercontent.com/jhyot/fake-api/main/park.json")

                if (response.status.value in 200..299) {
                    val data = response.body<ParkData>()
                    onResult(data.info)
                } else {
                    onResult("Сервер ответил кодом: ${response.status.value}")
                }
            } catch (e: Exception) {
                // Выводим более подробное описание ошибки
                onResult("Ошибка: ${e.message ?: "Неизвестная ошибка сети"}")
            }
        }
    }
}