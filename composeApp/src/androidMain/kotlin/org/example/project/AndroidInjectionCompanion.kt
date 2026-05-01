package org.example.project

import android.content.Context
import androidx.datastore.core.DataStore
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.example.project.preferences.Preferences
import org.example.project.preferences.createDataStore

object AndroidInjectionCompanion {
    private var dataStore: DataStore<Preferences>? = null

    fun getDataStore(context: Context): DataStore<Preferences> {
        // Если объект уже создан, возвращаем его
        dataStore?.let { return it }

        // Создаем новое хранилище, используя путь к внутренним файлам приложения
        val store = createDataStore(
            fileSystem = FileSystem.SYSTEM,
            producePath = {
                context.filesDir.resolve("preferences.json").toOkioPath()
            }
        )
        dataStore = store
        return store
    }
}