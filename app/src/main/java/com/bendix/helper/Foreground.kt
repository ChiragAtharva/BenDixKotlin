package com.bendix.helper

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

class Foreground private constructor() : ActivityLifecycleCallbacks {
    var isForeground = false
        private set
    val isBackground: Boolean
        get() = !isForeground

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        isForeground = true
    }

    override fun onActivityPaused(activity: Activity) {
        isForeground = false
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    companion object {
        private var instance: Foreground? = null
        fun init(app: Application) {
            if (instance == null) {
                instance = Foreground()
                app.registerActivityLifecycleCallbacks(instance)
            }
        }

        fun get(): Foreground? {
            return instance
        }
    }
}