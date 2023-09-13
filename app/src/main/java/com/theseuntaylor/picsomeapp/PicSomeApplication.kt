package com.theseuntaylor.picsomeapp

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PicSomeApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.e("Application", "Application OnCreate")
    }
}