package com.application.storelabs

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StoreLabsApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.e("Application", "Application OnCreate")
    }
}