package com.bendix.module.scanCustomerBarcode

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bendix.R
import com.bendix.base.BaseActivity
import com.bendix.databinding.ActivityScanCustomerBarcodeBinding
import com.bendix.databinding.ActivitySplashBinding
import com.bendix.module.login.LoginActivity
import com.bendix.module.scanCustomerBarcode.viewModel.ScanCustomerBarcodeViewModel
import com.bendix.sharedpreference.SPConstants
import com.bendix.sharedpreference.SPHelper
import com.bendix.utility.AlertDialogClass
import com.bendix.utility.Constants
import com.bendix.utility.LanguageUtils.changeLanguage
import com.bendix.utility.LogUtil
import com.bendix.utility.NetUtils
import com.bendix.webservice.interfaces.APICallListener
import com.bendix.webservice.interfaces.APIKeys
import com.symbol.emdk.barcode.ScanDataCollection
import com.symbol.emdk.barcode.ScanDataCollection.ScanData
import com.symbol.emdk.barcode.ScannerResults
import org.json.JSONObject
import java.lang.Exception


class ScanCustomerBarcodeActivity : BaseActivity() {
    private lateinit var mContext: Context
    private lateinit var viewModel: ScanCustomerBarcodeViewModel
    private lateinit var activityBinding: ActivityScanCustomerBarcodeBinding
    private var scannedBarcode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_scan_customer_barcode)
        viewModel = ViewModelProvider(this).get(ScanCustomerBarcodeViewModel::class.java)
        mContext = this

        activityBinding.llScanBarcode.setOnClickListener { v -> softScan(v) }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyScanner()
    }

    override fun onData(scanDataCollection: ScanDataCollection?) {
        super.onData(scanDataCollection)
        if (scanDataCollection != null && scanDataCollection.result == ScannerResults.SUCCESS) {
            val scanData = scanDataCollection.scanData
            for (data in scanData) {
                validateBarcode(data.labelType, data.data)
            }
        }
    }

    private fun validateBarcode(type: ScanDataCollection.LabelType, value: String) {
        LogUtil.displayLogInfo("SCBA", "scannerBarcodeEvent - type : $type")
        LogUtil.displayLogInfo("SCBA", "scannerBarcodeEvent - value : $value")
        scannedBarcode = value
        getCustomerInfo()
    }

    private fun getCustomerInfo() {
        if (NetUtils.isNetworkAvailable(mContext)) {
            LogUtil.displayLogInfo(
                "SCBA",
                "getCustomerInfo - Token :" + SPHelper.getString(mContext, SPConstants.ACCESS_TOKEN)
            )
            LogUtil.displayLogInfo(
                "SCBA",
                "getCustomerInfo - Uid :" + SPHelper.getString(mContext, SPConstants.USER_ID)
            )
            /*Customer(mContext, customerAPICallListener).getCustomer(
                SPHelper.getString(
                    mContext,
                    SPConstants.ACCESS_TOKEN
                ), SPHelper.getString(mContext, SPConstants.USER_ID), scannedBarcode
            )*/
        } else {
            runOnUiThread {
                AlertDialogClass(mContext).showSimpleDialog(
                    "",
                    mContext.resources.getString(R.string.check_internet_connection),
                    mContext.resources.getString(R.string.OK)
                )
            }
        }
    }

    private var validateTokenListener: APICallListener = object : APICallListener {
        override fun getResponse(response: String) {
            try {
                val responseObject = JSONObject(response)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}