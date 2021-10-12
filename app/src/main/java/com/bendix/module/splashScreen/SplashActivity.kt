package com.bendix.module.splashScreen

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bendix.R
import com.bendix.databinding.ActivitySplashBinding
import com.bendix.module.login.loginIntent
import com.bendix.module.scanCustomerBarcode.scanCustomerBarcodeIntent
import com.bendix.module.splashScreen.viewModel.SplashScreenViewModel
import com.bendix.sharedpreference.SPConstants
import com.bendix.sharedpreference.SPHelper
import com.bendix.utility.AlertDialogClass
import com.bendix.utility.Constants
import com.bendix.utility.LanguageUtils.changeLanguage
import com.bendix.utility.LogUtil
import com.bendix.utility.NetUtils
import java.lang.Boolean

class SplashActivity : AppCompatActivity() {
    private lateinit var mContext: Context
    private lateinit var viewModel: SplashScreenViewModel
    private lateinit var activityBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProvider(this).get(SplashScreenViewModel::class.java)
        mContext = this

        setupObserver()

        //Set Language
        changeLanguage(this)
        if (NetUtils.isNetworkAvailable(this)) {
            if (SPHelper.getString(this, SPConstants.ACCESS_TOKEN) != null) {
                viewModel.validateLogin(
                    mContext,
                    SPHelper.getString(this, SPConstants.USER_ID)!!,
                    SPHelper.getString(this, SPConstants.ACCESS_TOKEN)!!,
                )
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(loginIntent())
                    finish()
                }, Constants.SPLASH_DISPLAY_LENGTH)
            }
        } else {
            handleSplashNavigation()
        }
    }

    fun handleSplashNavigation() {
        Handler(Looper.getMainLooper()).postDelayed({
            LogUtil.displayLogInfo(
                "SA",
                "Access Token : " + SPHelper.getString(
                    this@SplashActivity,
                    SPConstants.ACCESS_TOKEN
                )
            )
            if (SPHelper.getString(this@SplashActivity, SPConstants.ACCESS_TOKEN) != null) {
                startActivity(scanCustomerBarcodeIntent())
            } else {
                startActivity(loginIntent())
            }
            finish()
        }, Constants.SPLASH_DISPLAY_LENGTH)
    }

    private fun setupObserver() {
        viewModel.messageEvent.observe(this, {
            //if (!checkGenericEventAction(it)) {
            try {
                if (viewModel.splashModel.type != null) {

                    when (it) {
                        Constants.SUCCESS_TOKEN -> {
                            if (Boolean.parseBoolean(viewModel.splashModel.Expired)) {
                                //Clear Preference
                                SPHelper.clearPref(this@SplashActivity)
                                //Check - Access Token exist
                                Handler(Looper.getMainLooper()).postDelayed({
                                    startActivity(loginIntent())
                                    finish()
                                }, Constants.SPLASH_DISPLAY_LENGTH)
                            } else {
                                //Check - Access Token exist
                                Handler(Looper.getMainLooper()).postDelayed({
                                    startActivity(scanCustomerBarcodeIntent())
                                    finish()
                                }, Constants.SPLASH_DISPLAY_LENGTH)
                            }
                        }
                        else -> {

                        }
                    }
                } else {
                    //show dialog
                    val dialogClass = AlertDialogClass(mContext)
                    if (viewModel.splashModel.message != null) {
                        dialogClass.showSimpleDialog(
                            "",
                            viewModel.splashModel.message,
                            mContext.getString(R.string.OK)
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //}
        })
    }
}