package com.zekierciyas.fancyfilterapp.repository

import android.content.Context
import android.graphics.Bitmap
import com.zekierciyas.fancyfilterlib.FancyFilter
import com.zekierciyas.fancyfilterlib.FancyFilters
import java.lang.Exception
import java.util.concurrent.Flow

interface FilterProcessor {

    suspend fun applyFilters(
        bitmap: Bitmap?,
        effect: List<Int>,
        onComplete: (effectedBitmap: List<Bitmap?>) -> Unit
    )

    suspend fun applyFilter(
        bitmap: Bitmap?,
        effect: Int,
        onComplete: (effectedBitmap: Bitmap) -> Unit
    )

    suspend fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        onComplete: () -> Unit,
        onError: (e: Exception) -> Unit
    )
}