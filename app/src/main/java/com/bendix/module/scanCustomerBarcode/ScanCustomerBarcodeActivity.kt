package com.bendix.module.scanCustomerBarcode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bendix.R
import com.bendix.base.BaseActivity
import com.bendix.databinding.ActivityScanCustomerBarcodeBinding
import com.bendix.module.product.productIntent
import com.bendix.module.scanCustomerBarcode.viewModel.ScanCustomerBarcodeViewModel
import com.bendix.sharedpreference.SPConstants
import com.bendix.sharedpreference.SPHelper
import com.bendix.utility.AlertDialogClass
import com.bendix.utility.Constants
import com.bendix.utility.LogUtil
import com.bendix.utility.NetUtils
import com.symbol.emdk.barcode.ScanDataCollection
import com.symbol.emdk.barcode.ScannerResults

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
        setupObserver()
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
            viewModel.getCustomer(
                mContext,
                SPHelper.getString(mContext, SPConstants.ACCESS_TOKEN)!!,
                SPHelper.getString(mContext, SPConstants.USER_ID)!!,
                scannedBarcode!!,
                //customerAPICallListener
            )
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

    private fun setupObserver() {
        viewModel.messageEvent.observe(this, {
            //if (!checkGenericEventAction(it)) {
            try {
                if (viewModel.barcodeModel.type != null) {

                    when (it) {
                        Constants.SUCCESS_GET_CUSTOMER -> {
                            startActivity(
                                productIntent(
                                    viewModel.barcodeModel.id,
                                    viewModel.barcodeModel.order_id,
                                    viewModel.barcodeModel.name
                                )
                            )
                        }
                        else -> {

                        }
                    }
                } else {
                    //show dialog
                    val dialogClass = AlertDialogClass(mContext)
                    if (viewModel.barcodeModel.message != null) {
                        dialogClass.showSimpleDialog(
                            "",
                            viewModel.barcodeModel.message,
                            mContext.getString(R.string.OK)
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //}
        })
    }
}

fun Context.scanCustomerBarcodeIntent(): Intent {
    return Intent(this, ScanCustomerBarcodeActivity::class.java)
}