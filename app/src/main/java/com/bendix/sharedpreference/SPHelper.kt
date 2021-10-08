package com.bendix.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class SPHelper private constructor(context: Context) {
    private val mPreferences: SharedPreferences =
        context.getSharedPreferences("app_name", Context.MODE_PRIVATE)
    private val mEditor: SharedPreferences.Editor = mPreferences.edit()

    companion object {
        var sAppPreference: SPHelper? = null
        fun getInstance(context: Context): SPHelper {
            if (sAppPreference == null) sAppPreference = SPHelper(context)
            return sAppPreference!!
        }

        fun getPrefs(context: Context): SharedPreferences {
            return context.getSharedPreferences("app_name", Context.MODE_PRIVATE)
        }

        fun getString(context: Context, key: String?): String? {
            return getString(context, key, null)
        }

        fun getString(context: Context, key: String?, value: String?): String? {
            return getPrefs(context).getString(key, value)
        }

        fun setString(context: Context, key: String, value: String) {
            getPrefs(context).edit().putString(key, value).apply()
        }

        fun getBoolean(context: Context, key: String): Boolean {
            return getBoolean(context, key, false)
        }

        fun getBoolean(context: Context, key: String, value: Boolean): Boolean {
            return getPrefs(context).getBoolean(key, value)
        }

        fun setBoolean(context: Context, key: String, value: Boolean) {
            getPrefs(context).edit().putBoolean(key, value).apply()
        }

        fun getInt(context: Context, key: String): Int {
            return getInt(context, key, 0)
        }

        fun getInt(context: Context, key: String, value: Int): Int {
            return getPrefs(context).getInt(key, value)
        }

        fun setInt(context: Context, key: String, value: Int) {
            getPrefs(context).edit().putInt(key, value).apply()
        }

        fun clearPref(context: Context) {
            val editor = getPrefs(context).edit()
            editor.clear()
            editor.apply()
        }
    }
}