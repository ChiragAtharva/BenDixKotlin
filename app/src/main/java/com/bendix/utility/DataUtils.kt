package com.bendix.utility

import ConfirmProduct
import java.util.ArrayList

class DataUtils {
    var products: ArrayList<ConfirmProduct>? = null

    companion object {
        private var dataUtils: DataUtils? = null
        val instance: DataUtils?
            get() {
                if (dataUtils == null) dataUtils = DataUtils()
                return dataUtils
            }
    }
}