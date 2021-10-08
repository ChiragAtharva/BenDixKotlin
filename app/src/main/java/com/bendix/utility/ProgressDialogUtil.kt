package com.bendix.utility

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import com.bendix.R
import java.lang.Exception

class ProgressDialogUtil {
    //Show ProgressBar
    fun showProgressBar(mContext: Context) {
        try {
            if (progress == null) {
                progress = ProgressDialog(mContext)
                progress!!.setMessage(mContext.resources.getString(R.string.pleasewait))
                progress!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                progress!!.isIndeterminate = false
                progress!!.setCancelable(false)
            }
            if (!progress!!.isShowing) progress!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Show ProgressBar
    fun showProgressBar(mContext: Context?, message: String?) {
        try {
            if (progress == null) {
                progress = ProgressDialog(mContext)
                progress!!.setMessage(message)
                progress!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                progress!!.isIndeterminate = false
                progress!!.setCancelable(true)
            }
            if (mContext != null && !(mContext as Activity).isFinishing) {
                mContext.runOnUiThread { if (!progress!!.isShowing) progress!!.show() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Show ProgressBar
    fun showThreadedProgressBar(mContext: Context?) {
        try {
            if (mContext != null && !(mContext as Activity).isFinishing) {
                mContext.runOnUiThread {
                    progress = ProgressDialog(mContext)
                    progress!!.setMessage(
                        mContext.getResources().getString(R.string.pleasewait)
                    )
                    progress!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                    progress!!.isIndeterminate = false
                    progress!!.setCancelable(true)
                    progress!!.show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Show ProgressBar
    fun getProgressBar(context: Context): ProgressDialog {
        val progress = ProgressDialog(context)
        progress.setMessage(context.resources.getString(R.string.pleasewait))
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progress.isIndeterminate = false
        progress.setCancelable(false)
        progress.show()
        return progress
    }

    //Hide ProgressBar
    fun hideProgressBar(mContext: Context?) {
        try {
            if (mContext != null && !(mContext as Activity).isFinishing) {
                mContext.runOnUiThread { if (progress != null) progress!!.dismiss() }
            } else hideProgressBar()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Hide ProgressBar
    fun hideProgressBar() {
        if (progress != null) progress!!.dismiss()
    }

    //Hide - ProgressBar
    fun hideProgressBar(progress: ProgressDialog) {
        progress.dismiss()
    }

    //Check - isRunning
    val isProgressBarVisible: Boolean
        get() {
            try {
                if (progress != null) return progress!!.isShowing
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

    companion object {
        private var instance: ProgressDialogUtil? = null
        private var progress: ProgressDialog? = null
        fun getInstance(): ProgressDialogUtil? {
            if (instance == null) instance = ProgressDialogUtil()
            return instance
        }

        fun removeInstance() {
            instance = null
            progress = null
        }
    }
}