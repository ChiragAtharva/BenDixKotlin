package com.bendix.utility

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.bendix.R
import com.bendix.webservice.APIConstants
import com.bendix.webservice.APIURLHelper
import java.lang.Exception

class AlertDialogClass(private val mContext: Context) {
    private var alertDialog: AlertDialog? = null
    fun showDialogAndFinish(title: String, message: String?, positiveText: String?) {
        var title = title
        if (title.isEmpty()) title = mContext.getString(R.string.app_name)
        try {
            val builder = AlertDialog.Builder(mContext) //set title
                .setTitle(title) //set message
                .setMessage(message) //set positive button
                .setPositiveButton(
                    positiveText
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                    (mContext as Activity).finish()
                }
                .setCancelable(false)
            builder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showSimpleDialog(title: String, message: String?, positiveText: String?) {
        var title = title
        if (title.isEmpty()) title = mContext.getString(R.string.app_name)
        try {
            val alertDialogBuilder = AlertDialog.Builder(mContext) //set title
                .setTitle(title) //set message
                .setMessage(message) //set positive button
                .setPositiveButton(
                    positiveText
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                .setCancelable(false)
            alertDialogBuilder.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun changeURLDialog(mContext: Context, previousValue: String, isShowTitle: Boolean) {
        val input = EditText(mContext)
        val alertMessage = mContext.resources.getString(R.string.change_url_dialog_message)
        val txtPositiveButton = mContext.resources.getString(R.string.yes)
        val txtNegativeButton = mContext.resources.getString(R.string.no)
        val alertDialogBuilder = AlertDialog.Builder(mContext)
        if (isShowTitle) {
            alertDialogBuilder.setTitle(
                Fonts.getTypefaceSemiBold(
                    mContext,
                    mContext.resources.getString(R.string.app_name)
                )
            )
            alertDialogBuilder.setIcon(R.mipmap.ic_launcher)
        }

        //Display Alert View
        val container = LinearLayout(mContext)
        container.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(
            Common.dpToPx(mContext, 20),
            0,
            Common.dpToPx(mContext, 20),
            Common.dpToPx(mContext, 3)
        )
        input.layoutParams = lp
        Fonts.setFontRegular(mContext, input)
        input.gravity = Gravity.TOP or Gravity.START
        input.maxLines = 10
        input.setText(previousValue.trim { it <= ' ' })
        input.textSize = 15f
        input.setSelection(input.text.length)
        input.isSingleLine = true
        input.setLineSpacing(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                2.0f,
                mContext.resources.displayMetrics
            ), 1.0f
        )
        if (input.parent != null) (input.parent as ViewGroup).removeView(input)
        if (container.parent != null) (container.parent as ViewGroup).removeView(container)
        container.addView(input, lp)
        alertDialogBuilder.setView(container)
        alertDialogBuilder
            .setMessage(Fonts.getTypefaceMedium(mContext, alertMessage))
            .setCancelable(false)
            .setPositiveButton(txtPositiveButton,
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    Common.hideKeyboard((mContext as Activity))
                    val enteredText = input.text.toString()
                    LogUtil.displayLogInfo("Alert", "Entered URL: $enteredText")
                    APIConstants.URL = enteredText
                    APIURLHelper.updateBaseURL()
                    LogUtil.displayLogError("Alert", "New URL: " + APIConstants.URL)
                    LogUtil.displayLogError("Alert", "New Base URL: " + APIConstants.BASE_URL)
                })
            .setNegativeButton(txtNegativeButton,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                    Common.hideKeyboard(mContext)
                })
        alertDialog = alertDialogBuilder.create()
        alertDialog!!.show()
        alertDialog!!.window!!
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        input.requestFocus()
        val window = alertDialog!!.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setGravity(Gravity.CENTER)

        //Set font for dialog buttons
        Fonts.setFontBold(mContext, alertDialog!!)

        //Set message text
        setAlertTextView(mContext, alertMessage, alertDialog)
    }

    companion object {
        //Show text in app theme text
        fun setAlertTextView(context: Context, message: String?, dialog: Dialog?) {
            try {
                val textView = dialog!!.findViewById<TextView>(android.R.id.message)
                textView.setTypeface(Fonts.getFontMedium(context))
                textView.setLineSpacing(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 2.0f,
                        context.resources.displayMetrics
                    ), 1.0f
                )
                textView.text = message
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}