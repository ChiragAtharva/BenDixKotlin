package com.bendix

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.bendix.helper.Foreground
import com.bendix.webservice.APIConstants

class BenDexApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        contextApp = applicationContext
        Foreground.init(this)
        APIConstants.BASE_URL = BuildConfig.app_url
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var contextApp: Context
    }
}