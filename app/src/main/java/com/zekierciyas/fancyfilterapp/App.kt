package com.zekierciyas.fancyfilterapp

import android.app.Application
import com.zekierciyas.fancyfilterapp.BuildConfig
import com.zekierciyas.fancyfilterapp.di.AppModule
import timber.log.Timber.*
import timber.log.Timber.Forest.plant


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }

        AppModule.initializeDI(_application = this)
    }
}