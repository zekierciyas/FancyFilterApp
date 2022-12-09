package com.zekierciyas.fancyfilterapp.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zekierciyas.fancyfilterapp.model.SelectableEffects
import com.zekierciyas.fancyfilterapp.repository.FilterProcessorImp
import com.zekierciyas.fancyfilterapp.util.resizeTheBitmap
import com.zekierciyas.fancyfilterapp.util.rotateHorizontallyIfNeeded
import com.zekierciyas.fancyfilterapp.util.uriToBitmap
import com.zekierciyas.fancyfilterlib.FancyFilters
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.logging.Filter

class FilterSelectorViewModel constructor(
    private val filterRepository: FilterProcessorImp ) : ViewModel() {

    /** List of selectable filters as statically
     * This section is mutable, when its added/changed the screen UI will be affected
     * Currently UI show all these filters as selectable to user */
    private var filterList = listOf(
        FancyFilters.NO_10,
        FancyFilters.NO_11,
        FancyFilters.NO_34,
        FancyFilters.NO_17,
        FancyFilters.NO_26,
        FancyFilters.NO_42,
        FancyFilters.NO_29,
        FancyFilters.NO_57
    )
    private var job: Job = Job()
    private var scope = CoroutineScope(Dispatchers.IO + job)

    private var _filteredBitmap : Bitmap? = null

    private var _uiState : MutableLiveData<FilterSelectorUiState?> = MutableLiveData()
    val uiState: LiveData<FilterSelectorUiState?> get() = _uiState

    private var bitmapOrj: Bitmap? = null

    fun applySelectableFilters(uri: Uri?, context: Context) {
        scope.launch {
            applyFilters(uri = uri, context = context, jobDone = {
                val listOfAppliedFilter = mutableListOf<SelectableEffects>()
                it.forEach { filteredBitmap ->
                    listOfAppliedFilter.add(SelectableEffects(filteredBitmap))
                }
                _uiState.postValue(FilterSelectorUiState.OnSuccess(FilterSelectorEvent.ListOfFilters(listOfAppliedFilter)))
            })
        }
    }

    private suspend fun applyFilters(uri: Uri?, context: Context, jobDone: (List<Bitmap?>) -> Unit ){
        /* Converting URI to bitmap
         * Rotating bitmap if needed, sometimes taken photo from device is coming with rotated exif data
         * as 90F degrees, to make it normal we applying it.
         * Lastly, reducing the bitmap size to make faster process.  */
        bitmapOrj = uri!!.uriToBitmap(context = context )!!
            .rotateHorizontallyIfNeeded()

        val bitmap: Bitmap = bitmapOrj!!.resizeTheBitmap(200)!!.copy(bitmapOrj!!.config, true)

        filterRepository.applyFilters(
            bitmap = bitmap,
            filterList,
            onComplete = {
                jobDone(it)
            }
        )
    }

    fun applySelectedFilter(position: Int) {
        scope.launch {
            applySelectedFilter(position){
                it?.let {
                    _filteredBitmap = it
                    _uiState.postValue(FilterSelectorUiState.OnSuccess(FilterSelectorEvent.FilteredBitmap(it)))
                }?: run{
                    _uiState.postValue(FilterSelectorUiState.OnError("Filter could not applied"))
                }
            }
        }
    }

    fun saveImage(context: Context) {
        scope.launch {
            _uiState.postValue(FilterSelectorUiState.Loading)
            saveBitmapToGallery(context = context)
        }
    }

   private suspend fun applySelectedFilter(position: Int, jobDone: (Bitmap?) -> Unit) {
       try {
           filterRepository.applyFilter(
               bitmapOrj,
               filterList[position],
               onComplete = {
                   jobDone.invoke(it)
               }
           )
       } catch (e: ArrayIndexOutOfBoundsException) {
           Timber.e("Multiple selected filter caused error of ${e.message}")
       }
    }

    private suspend fun saveBitmapToGallery(context: Context) {
        _filteredBitmap?.let {
            filterRepository.saveBitmapToGallery(
                context,
                it,
                onComplete = {
                    _uiState.postValue(FilterSelectorUiState.OnSuccess(FilterSelectorEvent.AppliedFilterSaved))

                },
                onError = { error ->
                    _uiState.postValue(FilterSelectorUiState.OnError(error.localizedMessage!!))
                })
        }
    }
}