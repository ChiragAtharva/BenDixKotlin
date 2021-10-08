package com.bendix.utility

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import com.bendix.sharedpreference.SPConstants
import com.bendix.sharedpreference.SPHelper
import java.lang.Exception
import java.util.*

object LanguageUtils {
    fun changeLanguage(mContext: Context) {
        val deviceLanguage = getDeviceLanguage(mContext)
        if (deviceLanguage != null) {
            val myLocale = Locale(deviceLanguage)
            val res = mContext.resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = myLocale
            Locale.setDefault(myLocale)
            res.updateConfiguration(conf, dm)
        }
    }

    fun getDeviceLanguage(mContext: Context?): String? {
        try {
            var storedLocale = SPHelper.getString(mContext!!, SPConstants.USER_LOCALE, null)
            if (storedLocale == null) {
                //Get device default language
                val locale: Locale
                locale =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) Resources.getSystem().configuration.locales[0] else Resources.getSystem().configuration.locale
                val deviceLanguage = locale.language
                val languages = arrayOf<String>(
                    Constants.LANGUAGE_CODE_ENGLISH,
                    Constants.LANGUAGE_CODE_SWEDISH
                )
                for (sLanguage in languages) {
                    if (sLanguage.equals(deviceLanguage, ignoreCase = true)) {
                        storedLocale = deviceLanguage
                        SPHelper.setString(mContext, SPConstants.USER_LOCALE, storedLocale)
                        break
                    }
                }
            }
            if (storedLocale == null) storedLocale = Constants.LANGUAGE_CODE_SWEDISH
            return storedLocale
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Constants.LANGUAGE_CODE_SWEDISH
    }
}