package com.zekierciyas.fancyfilterapp.di

import com.zekierciyas.fancyfilterapp.repository.FilterProcessorImp
import com.zekierciyas.fancyfilterapp.util.MediaStorageHelper
import com.zekierciyas.fancyfilterlib.FancyFilter

object LibraryModule {

    val filterProcessorImp by lazy {
        FilterProcessorImp(
            fancyFilter = provideFancyFilter(),
            mediaStorageHelper = MediaStorageHelper()
        )
    }

    private fun provideFancyFilter() : FancyFilter.Builder {
        return FancyFilter
                .Builder()
                .withContext(AppModule.application.applicationContext)
    }
}