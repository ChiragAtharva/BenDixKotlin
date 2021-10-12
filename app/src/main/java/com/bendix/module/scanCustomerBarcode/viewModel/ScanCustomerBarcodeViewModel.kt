package com.bendix.module.scanCustomerBarcode.viewModel

import com.bendix.base.BaseViewModel
import com.bendix.module.scanCustomerBarcode.model.ScanCustomerBarcodeModel
import com.bendix.module.scanCustomerBarcode.model.SendCustomerBody
import com.bendix.module.scanCustomerBarcode.response.CustomerResponse
import com.bendix.utility.Constants
import com.bendix.webservice.APIClient
import com.bendix.webservice.BaseCallback

class ScanCustomerBarcodeViewModel : BaseViewModel() {
    lateinit var barcodeModel: ScanCustomerBarcodeModel

    fun getCustomer(
        token: String,
        uid: String,
        barcode: String
    ) {
        val sendCustomerBody = SendCustomerBody(uid, token, barcode)

        APIClient.getApi()!!.getCustomer(sendCustomerBody)
            .enqueue(
                BaseCallback(object : BaseCallback.OnCallback<CustomerResponse> {
                    override fun status(response: CustomerResponse?, message: String) {

                        if (response != null) {
                            barcodeModel = ScanCustomerBarcodeModel(response)
                            //apiCallListener.getResponse(response.toString())
                            messageEvent.value = Constants.SUCCESS_GET_CUSTOMER
                        } else
                            messageEvent.value = message
                    }
                })
            )
    }
}