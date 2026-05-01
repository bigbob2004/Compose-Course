package org.example.project.permissions

import androidx.compose.runtime.Composable

enum class PermissionType {
    CAMERA, MICROPHONE, LOCATION
}

enum class PermissionState {
    GRANTED, DENIED, NOT_DETERMINED
}

interface PermissionManager {
    fun askPermission(permission: PermissionType)
    fun checkPermission(permission: PermissionType): PermissionState

    @Composable
    fun isPermissionGranted(permission: PermissionType): Boolean
}

@Composable
expect fun rememberPermissionManager(onPermissionResult: (Boolean) -> Unit): PermissionManager