package com.bendix

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.bendix.helper.Foreground

class BenDexApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        contextApp = applicationContext
        Foreground.init(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var contextApp: Context
    }
}