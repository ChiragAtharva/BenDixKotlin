package com.bendix.module.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bendix.R
import com.bendix.databinding.ActivityLoginBinding
import com.bendix.module.login.viewModel.LoginViewModel
import com.bendix.module.scanCustomerBarcode.scanCustomerBarcodeIntent
import com.bendix.sharedpreference.SPConstants
import com.bendix.sharedpreference.SPHelper
import com.bendix.utility.*
import com.bendix.webservice.APIConstants

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
        setupObserver()
    }

    private fun initView() {
        activityBinding.etUsername.setText("info@groning.fi")
        activityBinding.etPassword.setText("atharva999")
        activityBinding.btnLogin.setOnClickListener(object : SingleClickListener() {
            override fun onClicked(view: View?) {
                validateData()
            }
        })

        activityBinding.tvChangeUrl.setOnClickListener(object : SingleClickListener() {
            override fun onClicked(view: View?) {
                changeUrl()
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
            )
        } else {
            val dialogClass = AlertDialogClass(mContext)
            dialogClass.showSimpleDialog(
                "",
                mContext.resources.getString(R.string.check_internet_connection),
                mContext.resources.getString(R.string.OK)
            )
        }
    }

    private fun changeUrl() {
        val alertDialogClass = AlertDialogClass(mContext)
        alertDialogClass.changeURLDialog(mContext, APIConstants.URL, true)
    }

    private fun setupObserver() {
        viewModel.messageEvent.observe(this, {
            //if (!checkGenericEventAction(it)) {
            try {
                if (viewModel.loginModel.type != null) {

                    when (it) {
                        Constants.SUCCESS_LOGIN -> {
                            LogUtil.displayLogInfo(
                                "LA",
                                "Token ==>" + viewModel.loginModel.access_token
                            )
                            SPHelper.setString(
                                mContext, SPConstants.ACCESS_TOKEN,
                                viewModel.loginModel.access_token!!
                            )
                            SPHelper.setString(
                                mContext, SPConstants.PARTNER_ID,
                                viewModel.loginModel.partner_id!!
                            )
                            SPHelper.setString(
                                mContext, SPConstants.COMPANY_ID,
                                viewModel.loginModel.company_id!!
                            )
                            SPHelper.setString(
                                mContext, SPConstants.USER_ID,
                                viewModel.loginModel.uid!!
                            )
                            startActivity(scanCustomerBarcodeIntent())
                            finishAffinity()
                        }
                        else -> {

                        }
                    }
                } else {
                    //show dialog
                    val dialogClass = AlertDialogClass(mContext)
                    if (viewModel.loginModel.message != null) {
                        dialogClass.showSimpleDialog(
                            "",
                            viewModel.loginModel.message,
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

fun Context.loginIntent(): Intent {
    return Intent(this, LoginActivity::class.java)
}