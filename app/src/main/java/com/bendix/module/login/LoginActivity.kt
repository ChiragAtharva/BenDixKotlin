package com.bendix.module.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bendix.R
import com.bendix.databinding.ActivityLoginBinding
import com.bendix.module.login.viewModel.LoginViewModel
import com.bendix.module.scanCustomerBarcode.ScanCustomerBarcodeActivity
import com.bendix.sharedpreference.SPConstants
import com.bendix.sharedpreference.SPHelper
import com.bendix.utility.*
import com.bendix.webservice.interfaces.APICallListener
import com.bendix.webservice.interfaces.APIKeys
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private lateinit var mContext: Context
    private lateinit var viewModel: LoginViewModel
    private lateinit var activityBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        mContext = this
        initView()
    }

    private fun initView() {
        activityBinding.etUsername.setText("info@groning.fi")
        activityBinding.etPassword.setText("atharva999")
        val btnLogin: Button = findViewById(R.id.btn_login)
        btnLogin.setOnClickListener(object : SingleClickListener() {
            override fun onClicked(view: View?) {
                validateData()
            }
        })
    }

    private fun validateData() {
        if (activityBinding.etUsername.text.trim().isEmpty()) {
            activityBinding.etUsername.error = getString(R.string.errEnterUserName)
            activityBinding.etUsername.requestFocus()
        } else if (activityBinding.etPassword.text.trim().isEmpty()) {
            activityBinding.etPassword.error = getString(R.string.errPasswordLength)
            activityBinding.etPassword.requestFocus()
        } else {
            Common.hideKeyboard(this@LoginActivity)
            submitData()
        }
    }

    private fun submitData() {
        if (NetUtils.isNetworkAvailable(mContext)) {
            viewModel.callLogin(
                mContext,
                activityBinding.etUsername.text.toString(),
                activityBinding.etPassword.text.toString(),
                loginListener
            )
        } else {
            val dialogClass = AlertDialogClass(mContext)
            dialogClass.showSimpleDialog(
                mContext,
                "",
                mContext.resources.getString(R.string.check_internet_connection),
                mContext.resources.getString(R.string.OK)
            )
        }
    }

    private var loginListener: APICallListener = object : APICallListener {
        override fun getResponse(response: String) {
            try {
                val responseObject: JSONObject = JSONObject(response)
                if (!responseObject.has(APIKeys.ApiResponse.TYPE)) {
                    LogUtil.displayLogInfo(
                        "LA",
                        "Token ==>" + responseObject.getString(APIKeys.Login.Response.ACCESS_TOKEN)
                    )
                    SPHelper.setString(
                        mContext, SPConstants.ACCESS_TOKEN,
                        responseObject.getString(APIKeys.Login.Response.ACCESS_TOKEN)
                    )
                    SPHelper.setString(
                        mContext, SPConstants.PARTNER_ID,
                        responseObject.getString(APIKeys.Login.Response.PARTNER_ID)
                    )
                    SPHelper.setString(
                        mContext, SPConstants.COMPANY_ID,
                        responseObject.getString(APIKeys.Login.Response.COMPANY_ID)
                    )
                    SPHelper.setString(
                        mContext, SPConstants.USER_ID,
                        responseObject.getString(APIKeys.Login.Response.USER_ID)
                    )
                    val intent = Intent(mContext, ScanCustomerBarcodeActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    val dialogClass = AlertDialogClass(this@LoginActivity)
                    if (responseObject.has(APIKeys.ApiResponse.MESSAGE)) {
                        dialogClass.showSimpleDialog(
                            mContext,
                            "",
                            responseObject.optString(APIKeys.ApiResponse.MESSAGE),
                            mContext.getString(R.string.OK)
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}