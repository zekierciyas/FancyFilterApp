package com.zekierciyas.fancyfilterapp.ui

import android.app.Application
import com.zekierciyas.fancyfilterapp.BuildConfig
import timber.log.Timber.*
import timber.log.Timber.Forest.plant


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
    }
}