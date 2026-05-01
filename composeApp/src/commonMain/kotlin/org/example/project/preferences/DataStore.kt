package org.example.project.preferences

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage // Важно: правильный импорт
import okio.FileSystem
import okio.Path


fun createDataStore(
    fileSystem: FileSystem,
    producePath: () -> Path
): DataStore<Preferences> = DataStoreFactory.create(
    storage = OkioStorage(
        fileSystem = fileSystem,
        serializer = PreferencesSerializer(),
        producePath = producePath
    )
)