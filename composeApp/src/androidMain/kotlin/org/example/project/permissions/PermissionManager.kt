package org.example.project.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class AndroidPermissionManager(
    private val context: android.content.Context,
    private val launcher: androidx.activity.result.ActivityResultLauncher<String>,
    private val onResult: (Boolean) -> Unit
) : PermissionManager {

    private fun mapPermission(permission: PermissionType): String {
        return when (permission) {
            PermissionType.CAMERA -> Manifest.permission.CAMERA
            PermissionType.MICROPHONE -> Manifest.permission.RECORD_AUDIO
            PermissionType.LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
        }
    }

    override fun askPermission(permission: PermissionType) {
        launcher.launch(mapPermission(permission))
    }

    override fun checkPermission(permission: PermissionType): PermissionState {
        val status = ContextCompat.checkSelfPermission(context, mapPermission(permission))
        return if (status == PackageManager.PERMISSION_GRANTED) PermissionState.GRANTED else PermissionState.DENIED
    }

    @Composable
    override fun isPermissionGranted(permission: PermissionType): Boolean {
        return checkPermission(permission) == PermissionState.GRANTED
    }
}

@Composable
actual fun rememberPermissionManager(onPermissionResult: (Boolean) -> Unit): PermissionManager {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onPermissionResult(isGranted)
    }
    return remember { AndroidPermissionManager(context, launcher, onPermissionResult) }
}