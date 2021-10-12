package com.bendix.module.product.viewModel

import com.bendix.base.BaseViewModel
import com.bendix.module.product.model.*
import com.bendix.module.product.response.ProductInfoResponse
import com.bendix.utility.Constants
import com.bendix.webservice.APIClient
import com.bendix.webservice.BaseCallback

class ProductViewModel : BaseViewModel() {
    lateinit var productInfoModel: ProductInfoModel

    fun getProductInformation(token: String, userId: String, barcode: String) {
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

    fun getProductSalesOrder(token: String, userId: String, barcode: String, orderId: String) {
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
        token: String,
        uid: String,
        orderId: String
    ) {
        val confirmOrderRequestBody = ConfirmOrderRequestBody(
            token,
            uid,
            orderId
        )
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

    fun getQuotation(token: String, uid: String, orderId: String) {
        val askQuotationRequestBody = AskQuotationRequestBody(
            token,
            uid,
            orderId
        )
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