package com.bendix.utility

import android.content.Context
import android.util.Log

class LogUtil {
    companion object {
        private val isLogEnabled = true

        //Display log
        fun displayLog(tag: String, Message: String) {
            if (isLogEnabled) {
                Log.d(tag, Message)
            }
        }

        //Display error log
        fun displayLogError(tag: String, Message: String) {
            if (isLogEnabled) {
                Log.e(tag, Message)
            }
        }

        //Display verbose log
        fun displayLogVerbose(tag: String, Message: String) {
            if (isLogEnabled) {
                Log.v(tag, Message)
            }
        }

        //Display info log
        fun displayLogInfo(tag: String, Message: String) {
            if (isLogEnabled) {
                Log.i(tag, Message)
            }
        }

        //Display warning log
        fun displayLogWarning(tag: String, Message: String) {
            if (isLogEnabled) {
                Log.w(tag, Message)
            }
        }

        //Display log
        fun displayLog(mContext: Context, message: String) {
            if (isLogEnabled) {
                Log.i(mContext.javaClass.simpleName, message)
            }
        }
    }
}