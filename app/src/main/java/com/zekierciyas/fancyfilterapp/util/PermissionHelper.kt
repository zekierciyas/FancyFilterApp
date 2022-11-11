package com.zekierciyas.fancyfilterapp.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.random.Random

class PermissionHelper {

    private val permissionsForCamera = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val permissionsRequestCode = Random.nextInt(0, 10000)

    fun requestCameraPermission(
        activity: Activity,
        permissionGranted: () -> Unit,
        permissionDenied: () -> Unit
    ) {
        if (!hasPermissions(activity)) {
            permissionDenied.invoke()
            ActivityCompat.requestPermissions(
                activity, permissionsForCamera.toTypedArray(), permissionsRequestCode)
        } else permissionGranted.invoke()
    }
    private fun hasPermissions(context: Context) = permissionsForCamera.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}