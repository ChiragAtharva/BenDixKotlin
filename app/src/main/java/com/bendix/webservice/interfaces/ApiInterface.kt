package com.bendix.webservice.interfaces

import com.bendix.module.login.model.SendLoginBody
import com.bendix.module.login.response.LoginResponse
import com.bendix.module.product.model.*
import com.bendix.module.product.response.ProductInfoResponse
import com.bendix.module.scanCustomerBarcode.model.SendCustomerBody
import com.bendix.module.scanCustomerBarcode.response.CustomerResponse
import com.bendix.module.splashScreen.model.SendTokenBody
import com.bendix.module.splashScreen.response.TokenResponse
import com.bendix.webservice.APIConstants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @POST("/${APIConstants.MODULE_AUTH}/${APIConstants.SERVICE_TOKEN}")
    fun sendLogin(@Body sendLoginOtpBody: SendLoginBody): Call<LoginResponse>

    @POST("/${APIConstants.SERVICE_TOKEN_VALIDATION}")
    fun validateToken(@Body sendTokenBody: SendTokenBody): Call<TokenResponse>

    @POST("/${APIConstants.MODULE_CUSTOMER}")
    fun getCustomer(@Body sendCustomerBody: SendCustomerBody): Call<CustomerResponse>

    @GET("/${APIConstants.MODULE_PRODUCT}/${APIConstants.SERVICE_INFO}")
    fun getProductInformation(@Body productInfoRequestBody: ProductInfoRequestBody): Call<ProductInfoResponse>

    @GET("/${APIConstants.MODULE_SALE_ORDER}/${APIConstants.SERVICE_PRODUCT}")
    fun getProductSalesOrder(@Body productSalesOrderRequestBody: ProductSalesOrderRequestBody): Call<ProductInfoResponse>

    @GET("/${APIConstants.MODULE_SALE_ORDER}/${APIConstants.SERVICE_UPDATE}/${APIConstants.SUB_SERVICE_QUANTITY}")
    fun getUpdateOrderQuantity(@Body updateQuantityRequestBody: UpdateQuantityRequestBody): Call<ProductInfoResponse>

    @GET("/${APIConstants.MODULE_SALE_ORDER}/${APIConstants.SERVICE_CONFIRM}")
    fun getConfirmOrder(@Body confirmOrderRequestBody: ConfirmOrderRequestBody): Call<ProductInfoResponse>

    @GET("/${APIConstants.MODULE_SALE_ORDER}/${APIConstants.SERVICE_QUOTATION}")
    fun getQuotation(@Body askQuotationRequestBody: AskQuotationRequestBody): Call<ProductInfoResponse>
}