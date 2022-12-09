package com.zekierciyas.fancyfilterapp.di

import android.app.Application

object AppModule {

    @Volatile
    lateinit var application: Application

    fun initializeDI(_application: Application) {
        if (!::application.isInitialized) {
            synchronized(this) {
                application = _application
            }
        }
    }

}