package com.bendix.utility

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import com.bendix.R
import com.bendix.module.login.LoginActivity
import com.bendix.sharedpreference.SPHelper
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object Common {
    //Check - Check string is empty?
    fun isEmptyCheck(string: String?): Boolean {
        return TextUtils.isEmpty(string)
    }

    //Check - Check email id is valid?
    fun isEmailValid(mailAddress: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        pattern = Pattern.compile(EMAIL_PATTERN)
        matcher = pattern.matcher(mailAddress)
        return matcher.matches()
    }

    //Check - Check 2 string are equal or not?
    fun isCompare(str: String, compare: String): Boolean {
        var str = str
        var compare = compare
        str = Isnull(str)
        compare = Isnull(compare)
        return str.toLowerCase().trim { it <= ' ' }
            .equals(compare.toLowerCase().trim { it <= ' ' }, ignoreCase = true)
    }

    //Hide keyboard
    fun hideKeyboard(context: Context) {
        hideKeyboard(context as Activity)
    }

    fun hideKeyboard(activity: Activity) {
        val inputManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // check if no view has focus:
        val view = activity.currentFocus
        if (view != null) {
            inputManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun ShowKeyboard(activity: Activity) {
        val inputManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // check if no view has focus:
        val view = activity.currentFocus
        if (view != null) {
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    //Show - Message - Alert
    fun showAlert(context: Context, title: String, message: String?) {
        var title = title
        try {
            if (title.isEmpty()) title = context.resources.getString(R.string.app_name)
            val alertDialogBuilder = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(
                    context.resources.getString(R.string.OK)
                ) { dialog, which -> //Dismiss Dialog
                    dialog.dismiss()
                }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Show - Message - Alert
    fun showAlertWithLogout(context: Context, title: String, message: String?) {
        var title = title
        try {
            if (title.isEmpty()) title = context.resources.getString(R.string.app_name)
            val alertDialogBuilder = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(
                    context.resources.getString(R.string.OK)
                ) { dialog, which -> //Dismiss Dialog
                    dialog.dismiss()
                    //Clear Preference
                    SPHelper.clearPref(context)
                    //Move to Login screen
                    //Open launch screen
                    val intent = Intent(
                        context,
                        LoginActivity::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Check - Is null?
    fun Isnull(str: String?): String {
        var str = str
        if (str == null || str == "null" || str.toLowerCase().equals("null", ignoreCase = true)) {
            str = ""
        }
        return str
    }

    fun dpToPx(context: Context, dp: Int): Int {
        return Math.round(dp * getPixelScaleFactor(context))
    }

    fun pxToDp(context: Context, px: Int): Int {
        return Math.round(px / getPixelScaleFactor(context))
    }

    private fun getPixelScaleFactor(context: Context): Float {
        //float displayMetrics = context.getResources().getDisplayMetrics().density;    // display metrics in density of current device based on dpi
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT
    }

    fun isNumeric(strNum: String?): Boolean {
        if (strNum == null) {
            return false
        }
        try {
            val d = strNum.toInt()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun isJSONValid(test: String?): Boolean {
        try {
            JSONObject(test)
        } catch (ex: JSONException) {
            // e.g. in case JSONArray is valid as well...
            try {
                JSONArray(test)
            } catch (exp: JSONException) {
                return false
            }
        }
        return true
    }

    fun unableToProcessRequest(mContext: Context, message: String) {
        try {
            ProgressDialogUtil.getInstance()!!.hideProgressBar(mContext)
            if (message.isNotEmpty())
                showAlert(
                    mContext,
                    mContext.getString(R.string.app_name),
                    message
                ) else
                showAlert(
                    mContext,
                    mContext.getString(R.string.app_name),
                    mContext.getString(R.string.check_internet_connection)
                )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isValidURL(urlString: String?): Boolean {
        try {
            val url = URL(urlString)
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()
        } catch (ignored: MalformedURLException) {
        }
        return false
    }
}
