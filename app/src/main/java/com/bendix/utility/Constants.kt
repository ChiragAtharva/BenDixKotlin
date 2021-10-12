package com.bendix.utility

object Constants {
    //Single click listener
    const val THRESHOLD_MILLIS: Long = 1000
    var lastClickMillis: Long = 0
    const val SPLASH_DISPLAY_LENGTH: Long = 2000
    const val PRODUCT_DISPLAY_LENGTH: Long = 60000

    const val LANGUAGE_CODE_ENGLISH = "en"
    const val LANGUAGE_CODE_SWEDISH = "SV"

    //API response handling and callback using observer using their event name declare as below
    const val SUCCESS_TOKEN = "successToken"
    const val SUCCESS_LOGIN = "successLogin"
    const val SUCCESS_GET_CUSTOMER = "successGetCustomer"
    const val SUCCESS_GET_PRODUCT_INFORMATION = "successGetProductInformation"
    const val SUCCESS_GET_PRODUCT_SALES_ORDER = "successGetProductSalesOrder"
    const val SUCCESS_UPDATE_QUANTITY = "successUpdateQuantity"
    const val SUCCESS_CONFIRM_ORDER = "successConfirmOrder"
    const val SUCCESS_GET_QUOTATION= "successGetQuotation"

    const val TOKEN_EXPIRED_CODE = 403
}