package com.bendix.module.scanCustomerBarcode.viewModel

import android.content.Context
import com.bendix.R
import com.bendix.base.BaseViewModel
import com.bendix.module.scanCustomerBarcode.ScanCustomerBarcodeActivity
import com.bendix.module.scanCustomerBarcode.model.SendTokenBody
import com.bendix.module.scanCustomerBarcode.model.ScanCustomerBarcodeModel
import com.bendix.module.scanCustomerBarcode.response.TokenResponse
import com.bendix.utility.Common
import com.bendix.webservice.APIClient
import com.bendix.webservice.BaseCallback
import com.bendix.webservice.interfaces.APICallListener

class ScanCustomerBarcodeViewModel : BaseViewModel() {
    lateinit var barcodeModel: ScanCustomerBarcodeModel
    fun validateLogin(
        mContext: Context,
        uid: String,
        token: String,
        apiCallListener: APICallListener
    ) {
        val sendTokenBody = SendTokenBody(uid, token)

        APIClient.getApi()!!.validateToken(sendTokenBody)
            .enqueue(
                BaseCallback(object : BaseCallback.OnCallback<TokenResponse> {
                    override fun status(response: TokenResponse?, message: String) {
                        if (response != null) {
                            barcodeModel = ScanCustomerBarcodeModel(response)
                            when {
                                Common.isJSONValid(response.toString()) -> apiCallListener.getResponse(
                                    response.toString()
                                )
                                mContext is ScanCustomerBarcodeActivity -> (mContext as ScanCustomerBarcodeActivity).handleSplashNavigation()
                                else -> Common.showAlert(
                                    mContext,
                                    mContext.getString(R.string.error),
                                    mContext.getString(R.string.invalid_json_response)
                                )
                            }
                            //messageEvent.value = ApplicationConstants.SUCCESS_SEND_OTP
                        } else
                            if (mContext is ScanCustomerBarcodeActivity)
                                mContext.handleSplashNavigation()
                        messageEvent.value = message
                    }
                })
            )
    }
}