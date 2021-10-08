package com.bendix.utility

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.bendix.R
import java.lang.Exception

object Fonts {
    fun getFontBold(context: Context?): Typeface? {
        try {
            return ResourcesCompat.getFont(context!!, R.font.worksans_bold)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Typeface.DEFAULT_BOLD
    }

    fun getFontMedium(context: Context?): Typeface? {
        try {
            return ResourcesCompat.getFont(context!!, R.font.worksans_medium)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Typeface.DEFAULT
    }

    fun getFontNormal(context: Context?): Typeface? {
        try {
            return ResourcesCompat.getFont(context!!, R.font.worksans_regular)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Typeface.DEFAULT
    }

    fun getFontSemiBold(context: Context?): Typeface? {
        try {
            return ResourcesCompat.getFont(context!!, R.font.worksans_semibold)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Typeface.DEFAULT_BOLD
    }

    fun getTypefaceSemiBold(context: Context?, string: CharSequence): SpannableString {
        return getSpannableString(getFontSemiBold(context), string)
    }

    fun getTypefaceMedium(context: Context?, string: CharSequence): SpannableString {
        return getSpannableString(getFontMedium(context), string)
    }

    private fun getSpannableString(typeface: Typeface?, string: CharSequence): SpannableString {
        val s = SpannableString(string)
        if (typeface != null) {
            s.setSpan(
                CustomTypeFaceSpan("", typeface, Color.BLACK),
                0,
                s.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return s
    }

    //Set font - TextView
    fun setFontBold(context: Context?, textView: TextView) {
        val typeface = getFontBold(context)
        if (typeface != null) {
            textView.typeface = typeface
        }
    }

    fun setFontRegular(context: Context?, textView: TextView) {
        val typeface = getFontNormal(context)
        if (typeface != null) {
            textView.typeface = typeface
        }
    }

    //Set font - AlertDialog Button
    fun setFontBold(context: Context?, dialog: AlertDialog) {
        setFontBold(context, dialog.getButton(AlertDialog.BUTTON_NEGATIVE))
        setFontBold(context, dialog.getButton(AlertDialog.BUTTON_POSITIVE))
        setFontBold(context, dialog.getButton(AlertDialog.BUTTON_NEUTRAL))
    }
}