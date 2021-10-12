package com.bendix.module.splashScreen.viewModel

import android.content.Context
import com.bendix.base.BaseViewModel
import com.bendix.module.splashScreen.SplashActivity
import com.bendix.module.splashScreen.model.SendTokenBody
import com.bendix.module.splashScreen.model.SplashModel
import com.bendix.module.splashScreen.response.TokenResponse
import com.bendix.utility.Constants
import com.bendix.webservice.APIClient
import com.bendix.webservice.BaseCallback

class SplashScreenViewModel : BaseViewModel() {
    lateinit var splashModel: SplashModel
    fun validateLogin(
        mContext: Context,
        uid: String,
        token: String,
    ) {
        val sendTokenBody = SendTokenBody(uid, token)

        APIClient.getApi()!!.validateToken(sendTokenBody)
            .enqueue(
                BaseCallback(object : BaseCallback.OnCallback<TokenResponse> {
                    override fun status(response: TokenResponse?, message: String) {
                        when {
                            response != null -> {
                                splashModel = SplashModel(response)
                                messageEvent.value = Constants.SUCCESS_TOKEN
                            }
                            mContext is SplashActivity -> mContext.handleSplashNavigation()
                            else -> messageEvent.value = message
                        }
                    }
                })
            )
    }
}