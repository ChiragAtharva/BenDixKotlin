package com.bendix.utility

import android.os.SystemClock
import android.view.View

abstract class SingleClickListener: View.OnClickListener {
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - Constants.lastClickMillis< Constants.THRESHOLD_MILLIS){
         return;
        }
        Constants.lastClickMillis = SystemClock.elapsedRealtime()
        onClicked(v)
    }

    abstract fun onClicked(view: View?)
}