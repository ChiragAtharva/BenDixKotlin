package com.bendix.utility

import ConfirmProduct
import java.util.ArrayList

class DataUtils {

    private var dataUtils: DataUtils? = null

    var products: ArrayList<ConfirmProduct?>? = null

    val instance: DataUtils?
        get() {
            if (dataUtils == null) dataUtils = DataUtils()
            return dataUtils
        }

}