package com.zekierciyas.fancyfilterapp.util

import android.R.drawable
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewGroup.VISIBLE
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.zekierciyas.fancyfilterapp.R
import java.io.FileDescriptor
import java.io.IOException


fun Uri.uriToBitmap(context: Context): Bitmap? {
    return try {
        val parcelFileDescriptor: ParcelFileDescriptor? =
            context.contentResolver.openFileDescriptor(this, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        image
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun Bitmap.rotateHorizontallyIfNeeded(): Bitmap {
    return if (this.height < this.width) {
        this.rotate(90F)
    } else this
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

/**
 * reduces the size of the image
 * @param image
 * @param maxSize
 * @return
 */
fun Bitmap.resizeTheBitmap(maxSize: Int): Bitmap? {
    var width = this.width
    var height = this.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(this, width, height, true)
}

fun Fragment.runUIThread() = kotlin.runCatching {
    this.requireActivity().runOnUiThread {

    }
}

fun View.show() {
    if (this.visibility == View.VISIBLE) return
    this.visibility = View.VISIBLE
}

fun View.hide() {
    if (this.visibility == View.INVISIBLE) return
    this.visibility = View.INVISIBLE
}

fun View.disableClickable() {
    if (!this.isClickable) return
    this.isClickable = false
}

fun View.enableClickable() {
    if (this.isClickable) return
    this.isClickable = true
}
