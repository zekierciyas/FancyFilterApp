package com.zekierciyas.fancyfilterapp.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.zekierciyas.fancyfilterapp.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class MediaStorageHelper {

    fun saveMediaToStorage(
        context: Context,
        bitmap: Bitmap,
        onComplete: () -> Unit,
        onError: (e: Exception) -> Unit
    ) {
        try {
            val folderName = context.getString(R.string.app_name)

            if (Build.VERSION.SDK_INT >= 29) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
                values.put(MediaStore.Images.Media.IS_PENDING, true)

                val uri: Uri? =
                    context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri), onComplete)
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(uri, values, null, null)
                }
            } else {
                val directory = File(
                    Environment.getExternalStorageDirectory().toString() + File.separator + folderName
                )

                if (!directory.exists()) {
                    directory.mkdirs()
                }
                val fileName = System.currentTimeMillis().toString() + ".png"
                val file = File(directory, fileName)
                saveImageToStream(bitmap, FileOutputStream(file), onComplete)
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

    private fun contentValues(): ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?, onComplete: () -> Unit) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}