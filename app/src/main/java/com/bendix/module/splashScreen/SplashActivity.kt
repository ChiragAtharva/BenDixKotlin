package com.bendix.module.splashScreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bendix.R
import com.bendix.databinding.ActivitySplashBinding
import com.bendix.module.login.LoginActivity
import com.bendix.module.scanCustomerBarcode.ScanCustomerBarcodeActivity
import com.bendix.module.scanCustomerBarcode.viewModel.ScanCustomerBarcodeViewModel
import com.bendix.sharedpreference.SPConstants
import com.bendix.sharedpreference.SPHelper
import com.bendix.utility.Constants
import com.bendix.utility.LanguageUtils.changeLanguage
import com.bendix.utility.LogUtil
import com.bendix.utility.NetUtils
import com.bendix.webservice.interfaces.APICallListener
import com.bendix.webservice.interfaces.APIKeys
import org.json.JSONObject
import java.lang.Exception


class SplashActivity : AppCompatActivity() {
    private lateinit var mContext: Context
    private lateinit var viewModel: ScanCustomerBarcodeViewModel
    private lateinit var activityBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProvider(this).get(ScanCustomerBarcodeViewModel::class.java)
        mContext = this

        //Set Language
        changeLanguage(this)
        if (NetUtils.isNetworkAvailable(this)) {
            if (SPHelper.getString(this, SPConstants.ACCESS_TOKEN) != null) {
                viewModel.validateLogin(
                    mContext,
                    SPHelper.getString(this, SPConstants.USER_ID)!!,
                    SPHelper.getString(this, SPConstants.ACCESS_TOKEN)!!,
                    validateTokenListener
                )
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }, Constants.SPLASH_DISPLAY_LENGTH)
            }
        } else {
            handleSplashNavigation()
        }
    }

    private var validateTokenListener: APICallListener = object : APICallListener {
        override fun getResponse(response: String) {
            try {
                val responseObject = JSONObject(response)
                val isExpired = responseObject.optBoolean(APIKeys.ValidateToken.Response.EXPIRED)
                if (isExpired) {
                    //Clear Preference
                    SPHelper.clearPref(this@SplashActivity)
                    //Check - Access Token exist
                    Handler(Looper.getMainLooper()).postDelayed({
                        val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    }, Constants.SPLASH_DISPLAY_LENGTH)
                } else {
                    //Check - Access Token exist
                    Handler(Looper.getMainLooper()).postDelayed({
                        val mainIntent = Intent(
                            this@SplashActivity,
                            ScanCustomerBarcodeActivity::class.java
                        )
                        startActivity(mainIntent)
                        finish()
                    }, Constants.SPLASH_DISPLAY_LENGTH)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
                val mainIntent =
                    Intent(this@SplashActivity, ScanCustomerBarcodeActivity::class.java)
                startActivity(mainIntent)
            } else {
                val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(mainIntent)
            }
            finish()
        }, Constants.SPLASH_DISPLAY_LENGTH)
    }
}