package com.bendix.webservice

class APIConstants {
    companion object {
        //Base - URL
        var URL = "http://3.21.172.132:9091"
        var API = "/api"
        var URL_VERSION = ""
        var BASE_URL = "$URL$API$URL_VERSION/"

        //Module - name
        const val MODULE_AUTH = "auth"
        const val MODULE_CUSTOMER = "customer"
        const val MODULE_SALE_ORDER = "saleorder"
        const val MODULE_PRODUCT = "product"

        //Service - name
        const val SERVICE_TOKEN = "token"
        const val SERVICE_PRODUCT = "product"
        const val SERVICE_UPDATE = "update"
        const val SERVICE_CONFIRM = "confirm"
        const val SERVICE_INFO = "info"
        const val SERVICE_QUOTATION = "quotation"
        const val SERVICE_TOKEN_VALIDATION = "tokenValidation"

        //Sub Service - name
        const val SUB_SERVICE_QUANTITY = "qty"

        //Constant
        const val TOKEN_EXPIRED_CODE = 403

        //Error
        const val ERROR_TOKEN_EXPIRED = "Token is invalid or exprired!"
    }
}