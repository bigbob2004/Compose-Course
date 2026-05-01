package org.example.project.preferences

import androidx.datastore.core.okio.OkioSerializer
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

class PreferencesSerializer : OkioSerializer<Preferences> {
    override val defaultValue: Preferences = Preferences()

    override suspend fun readFrom(source: BufferedSource): Preferences {
        return try {
            Json.decodeFromString(
                deserializer = Preferences.serializer(),
                string = source.readUtf8()
            )
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: Preferences, sink: BufferedSink) {
        // Записываем JSON напрямую в поток без принудительного закрытия .use
        val jsonString = Json.encodeToString(
            serializer = Preferences.serializer(),
            value = t
        )
        sink.writeUtf8(jsonString)
    }
}