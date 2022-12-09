package com.zekierciyas.fancyfilterapp.ui

import android.graphics.Bitmap
import com.zekierciyas.fancyfilterapp.model.SelectableEffects

sealed class FilterSelectorUiState  {
    object Loading : FilterSelectorUiState()
    data class OnSuccess(val event: FilterSelectorEvent) : FilterSelectorUiState()
    data class OnError(val errorMessage: String) : FilterSelectorUiState()
}

sealed class FilterSelectorEvent {
    data class FilteredBitmap(val filteredBitmap: Bitmap) : FilterSelectorEvent()
    data class ListOfFilters(val listOfFilters: List<SelectableEffects>): FilterSelectorEvent()
    object AppliedFilterSaved : FilterSelectorEvent()
}
