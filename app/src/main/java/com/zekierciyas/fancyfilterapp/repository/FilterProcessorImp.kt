package com.zekierciyas.fancyfilterapp.repository

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import com.zekierciyas.fancyfilterapp.util.MediaStorageHelper
import com.zekierciyas.fancyfilterlib.FancyFilter
import com.zekierciyas.fancyfilterlib.FancyFilters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class FilterProcessorImp constructor(
    private val fancyFilter: FancyFilter.Builder,
    private val mediaStorageHelper: MediaStorageHelper) : FilterProcessor{

    @WorkerThread
    override suspend fun applyFilters(
        bitmap: Bitmap?,
        effect: List<Int>,
        onComplete: (effectedBitmap: List<Bitmap?>) -> Unit
    ) {
        fancyFilter.filters(effect)
            .bitmap(bitmap)
            .applyFilters {
                onComplete.invoke(it)
            }
    }

    @WorkerThread
    override suspend fun applyFilter(
        bitmap: Bitmap?,
        effect: Int,
        onComplete: (effectedBitmap: Bitmap) -> Unit
    ) {
        fancyFilter.filter(effect)
            .bitmap(bitmap)
            .applyFilter {
                onComplete.invoke(it)
            }
    }

    @WorkerThread
    override suspend fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        onComplete: () -> Unit,
        onError: (e: Exception) -> Unit
    ) {
        mediaStorageHelper.saveMediaToStorage(
            context = context,
            bitmap = bitmap,
            onComplete = onComplete,
            onError = onError
        )
    }
}