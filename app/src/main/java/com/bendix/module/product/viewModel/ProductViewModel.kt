package com.bendix.module.product.viewModel

import android.content.Context
import com.bendix.base.BaseViewModel
import com.bendix.module.product.model.*
import com.bendix.module.product.response.ProductInfoResponse
import com.bendix.utility.Constants
import com.bendix.utility.ProgressDialogUtil
import com.bendix.webservice.APIClient
import com.bendix.webservice.BaseCallback

class ProductViewModel : BaseViewModel() {
    var productInfoModel = ProductInfoModel()

    fun getProductInformation(mContext: Context, token: String, userId: String, barcode: String) {
        ProgressDialogUtil.getInstance()!!.showThreadedProgressBar(mContext)
        val productInfoRequestBody = ProductInfoRequestBody(token, userId, barcode)
        APIClient.getApi()!!.getProductInformation(productInfoRequestBody)
            .enqueue(
                BaseCallback(object : BaseCallback.OnCallback<ProductInfoResponse> {
                    override fun status(response: ProductInfoResponse?, message: String) {

                        if (response != null) {
                            productInfoModel = ProductInfoModel(response)
                            messageEvent.value = Constants.SUCCESS_GET_PRODUCT_INFORMATION
                        } else
                            messageEvent.value = message
                    }
                })
            )
    }

    fun getProductSalesOrder(
        mContext: Context,
        token: String,
        userId: String,
        barcode: String,
        orderId: String
    ) {
        ProgressDialogUtil.getInstance()!!.showThreadedProgressBar(mContext)
        val productSalesOrderRequestBody =
            ProductSalesOrderRequestBody(token, userId, barcode, orderId)
        APIClient.getApi()!!.getProductSalesOrder(productSalesOrderRequestBody)
            .enqueue(
                BaseCallback(object : BaseCallback.OnCallback<ProductInfoResponse> {
                    override fun status(response: ProductInfoResponse?, message: String) {

                        if (response != null) {
                            productInfoModel = ProductInfoModel(response)
                            messageEvent.value = Constants.SUCCESS_GET_PRODUCT_SALES_ORDER
                        } else
                            messageEvent.value = message
                    }
                })
            )
    }

    fun updateQuantity(
        mContext: Context,
        token: String,
        uid: String,
        orderId: String,
        saleOrderLineId: Int,
        qty: Int
    ) {
        val updateQuantityRequestBody = UpdateQuantityRequestBody(
            token,
            uid,
            saleOrderLineId.toString(),
            orderId,
            qty.toString()
        )
        ProgressDialogUtil.getInstance()!!.showThreadedProgressBar(mContext)
        APIClient.getApi()!!.getUpdateOrderQuantity(updateQuantityRequestBody)
            .enqueue(
                BaseCallback(object : BaseCallback.OnCallback<ProductInfoResponse> {
                    override fun status(response: ProductInfoResponse?, message: String) {

                        if (response != null) {
                            productInfoModel = ProductInfoModel(response)
                            messageEvent.value = Constants.SUCCESS_UPDATE_QUANTITY
                        } else
                            messageEvent.value = message
                    }
                })
            )
    }

    fun confirmOrder(
        mContext: Context?,
        token: String,
        uid: String,
        orderId: String
    ) {
        val confirmOrderRequestBody = ConfirmOrderRequestBody(
            token,
            uid,
            orderId
        )
        ProgressDialogUtil.getInstance()!!.showThreadedProgressBar(mContext)
        APIClient.getApi()!!.getConfirmOrder(confirmOrderRequestBody)
            .enqueue(
                BaseCallback(object : BaseCallback.OnCallback<ProductInfoResponse> {
                    override fun status(response: ProductInfoResponse?, message: String) {

                        if (response != null) {
                            productInfoModel = ProductInfoModel(response)
                            messageEvent.value = Constants.SUCCESS_CONFIRM_ORDER
                        } else
                            messageEvent.value = message
                    }
                })
            )
    }

    fun getQuotation(mContext: Context?, token: String, uid: String, orderId: String) {
        val askQuotationRequestBody = AskQuotationRequestBody(
            token,
            uid,
            orderId
        )
        ProgressDialogUtil.getInstance()!!.showThreadedProgressBar(mContext)
        APIClient.getApi()!!.getQuotation(askQuotationRequestBody)
            .enqueue(
                BaseCallback(object : BaseCallback.OnCallback<ProductInfoResponse> {
                    override fun status(response: ProductInfoResponse?, message: String) {

                        if (response != null) {
                            productInfoModel = ProductInfoModel(response)
                            messageEvent.value = Constants.SUCCESS_GET_QUOTATION
                        } else
                            messageEvent.value = message
                    }
                })
            )
    }
}