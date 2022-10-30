package com.zekierciyas.fancyfilterapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.random.Random

class CameraPermissionHelper {
    private val permissions = listOf(Manifest.permission.CAMERA)
    private val permissionsRequestCode = Random.nextInt(0, 10000)

    fun requestCameraPermission(
        activity: Activity,
        permissionGranted: () -> Unit,
        permissionDenied: () -> Unit
    ) {
        if (!hasPermissions(activity)) {
            permissionDenied.invoke()
            ActivityCompat.requestPermissions(
                activity, permissions.toTypedArray(), permissionsRequestCode)
        } else permissionGranted.invoke()
    }
    private fun hasPermissions(context: Context) = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}