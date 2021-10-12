package com.bendix.module.confirmOrder

import ConfirmProduct
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bendix.R
import com.bendix.databinding.ActivityConfirmOrderBinding
import com.bendix.module.confirmOrder.adapter.ConfirmProductAdapter
import com.bendix.module.scanCustomerBarcode.ScanCustomerBarcodeActivity
import com.bendix.utility.Constants
import com.bendix.utility.DataUtils
import com.bendix.webservice.interfaces.APIKeys
import java.util.*

class ConfirmOrderActivity : AppCompatActivity() {
    private var arrConfirmProduct: ArrayList<ConfirmProduct> = ArrayList<ConfirmProduct>()

    //private lateinit var viewModel: ProductViewModel
    private lateinit var activityBinding: ActivityConfirmOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mContext: Context = this@ConfirmOrderActivity
        activityBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_confirm_order)

        val order = intent.getStringExtra(APIKeys.SalesOrder.Response.ORDER)
        val orderMessage: String =
            mContext.resources.getString(R.string.order_number) + " " + order
        activityBinding.tvOrderNumber.text = orderMessage
        activityBinding.tvSubTotal.text =
            intent.getStringExtra(APIKeys.SalesOrder.Response.SUB_TOTAL)
        activityBinding.tvTaxAmount.text =
            intent.getStringExtra(APIKeys.SalesOrder.Response.TAX_AMOUNT)
        activityBinding.tvTotal.text = intent.getStringExtra(APIKeys.SalesOrder.Response.TOTAL)

        if (DataUtils.instance!!.products != null)
            arrConfirmProduct = DataUtils.instance!!.products!!

        //Set - Layout Manager
        val mLayoutManager = LinearLayoutManager(mContext)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL

        //Set - RecycleView - Properties
        activityBinding.rvConfirmProduct.itemAnimator = DefaultItemAnimator()
        activityBinding.rvConfirmProduct.layoutManager = mLayoutManager

        //Initialise - Adapters
        val confirmProductAdapter = ConfirmProductAdapter(mContext, arrConfirmProduct)
        activityBinding.rvConfirmProduct.adapter = confirmProductAdapter

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(
                this@ConfirmOrderActivity,
                ScanCustomerBarcodeActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }, Constants.PRODUCT_DISPLAY_LENGTH)

    }

    override fun onDestroy() {
        super.onDestroy()
        DataUtils.instance!!.products = null
    }
}

fun Context.confirmOrderIntent(
    order: String,
    message: String,
    subTotal: String,
    taxAmount: String,
    total: String
): Intent {
    return Intent(this, ConfirmOrderActivity::class.java).apply {
        putExtra(APIKeys.SalesOrder.Response.ORDER, order)
        putExtra(APIKeys.SalesOrder.Response.MESSAGE, message)
        putExtra(APIKeys.SalesOrder.Response.SUB_TOTAL, subTotal)
        putExtra(APIKeys.SalesOrder.Response.TAX_AMOUNT, taxAmount)
        putExtra(APIKeys.SalesOrder.Response.TOTAL, total)
    }
}

