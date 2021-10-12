package com.bendix.module.product

import ConfirmProduct
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bendix.R
import com.bendix.base.BaseActivity
import com.bendix.databinding.ActivityProductListBinding
import com.bendix.interfaces.Product
import com.bendix.module.confirmOrder.confirmOrderIntent
import com.bendix.module.product.adapter.ProductAdapter
import com.bendix.module.product.viewModel.ProductViewModel
import com.bendix.module.requestQuotation.requestQuotationIntent
import com.bendix.sharedpreference.SPConstants
import com.bendix.sharedpreference.SPHelper
import com.bendix.utility.*
import com.bendix.utility.Fonts.getTypefaceMedium
import com.bendix.utility.Fonts.getTypefaceSemiBold
import com.bendix.utility.Fonts.setFontBold
import com.bendix.webservice.APIConstants
import com.bendix.webservice.interfaces.APIKeys
import com.symbol.emdk.barcode.ScanDataCollection
import com.symbol.emdk.barcode.ScannerResults
import org.json.JSONArray
import java.util.*

class ProductActivity : BaseActivity() {
    private var mContext: Context? = null
    private lateinit var viewModel: ProductViewModel
    private lateinit var activityBinding: ActivityProductListBinding

    private var orderId = 0
    private var customerId = 0
    private var barcode: String? = null

    private val tvTaxAmount: TextView? = null
    private val tvSubTotal: TextView? = null
    private val tvTotal: TextView? = null
    private val rvProducts: RecyclerView? = null

    private var productAdapter: ProductAdapter? = null
    private var updatingProduct: Product? = null
    //private val currentProduct: Product? = null

    private val arrProduct = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this@ProductActivity
        activityBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_product_list)
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        setupObserver()

        val customerName = intent.getStringExtra(APIKeys.Customer.Response.NAME)
        orderId = intent.getIntExtra(APIKeys.Customer.Response.ORDER_ID, 0)
        customerId = intent.getIntExtra(APIKeys.Customer.Response.ID, 0)

        //Set - Name
        activityBinding.tvCompanyName.text = customerName

        activityBinding.llScanBarcode.setOnClickListener { v -> softScan(v) }

        activityBinding.llBuy.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(mContext)
            alertDialogBuilder.setTitle(
                getTypefaceSemiBold(
                    mContext,
                    mContext!!.resources.getString(R.string.app_name)
                )
            )
                .setMessage(
                    getTypefaceMedium(
                        mContext,
                        mContext!!.resources.getString(R.string.product_confirm)
                    )
                )
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton(
                    mContext!!.resources.getString(R.string.yes)
                ) { dialog, which -> confirmOrder() }
                .setNegativeButton(
                    mContext!!.resources.getString(R.string.no)
                ) { dialog, which -> dialog.dismiss() }


            val alertDialog = alertDialogBuilder.show()
            setFontBold(mContext, alertDialog)

            //Set - Layout Manager
            val mLayoutManager = LinearLayoutManager(mContext)
            mLayoutManager.orientation = LinearLayoutManager.VERTICAL

            //Set - RecycleView - Properties
            rvProducts!!.itemAnimator = DefaultItemAnimator()
            rvProducts.layoutManager = mLayoutManager

            //Initialise - Adapters
            productAdapter = ProductAdapter(mContext!!, arrProduct)
            rvProducts.adapter = productAdapter

            activityBinding.llQuotation.setOnClickListener {
                askQuotation()
            }
            activityBinding.viewHeader.ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        val alertDialogBuilder = AlertDialog.Builder(mContext)
        alertDialogBuilder.setTitle(
            getTypefaceSemiBold(
                mContext,
                mContext!!.resources.getString(R.string.app_name)
            )
        )
            .setMessage(
                getTypefaceMedium(
                    mContext,
                    mContext!!.resources.getString(R.string.cancel_purchase)
                )
            )
            .setIcon(R.mipmap.ic_launcher)
            .setPositiveButton(mContext!!.resources.getString(R.string.yes),
                { dialog, which -> finish() })
            .setNegativeButton(mContext!!.resources.getString(R.string.no),
                { dialog, which -> dialog.dismiss() })
        val alertDialog = alertDialogBuilder.show()
        setFontBold(mContext, alertDialog)


    }

    override fun onData(scanDataCollection: ScanDataCollection?) {
        super.onData(scanDataCollection)
        if (scanDataCollection != null && scanDataCollection.result == ScannerResults.SUCCESS) {
            val scanData = scanDataCollection.scanData
            for (data in scanData) {
                scanResult(data.labelType, data.data)
            }
        }
    }

    private fun scanResult(type: ScanDataCollection.LabelType, value: String) {
        LogUtil.displayLogInfo("PA", "scannerBarcodeEvent - type : $type")
        LogUtil.displayLogInfo("PA", "scannerBarcodeEvent - value : $value")
        barcode = value
        getProductInformation(barcode)
    }

    private fun isScanProductExisted(productId: Int): Product? {
        var product: Product? = null
        for (p in arrProduct) {
            if (p.productId == productId) {
                product = p
                break
            }
        }
        return product
    }

    private fun updateProductInformation(productId: Int, saleOrderLineId: Int): Product? {
        var product: Product? = null
        for (p in arrProduct) {
            if (p.productId == productId) {
                product = p
                product.saleOrderLineId = saleOrderLineId
                break
            }
        }
        return product
    }

    private fun getProductInformation(barcode: String?) {
        if (NetUtils.isNetworkAvailable(mContext!!)) {
            viewModel.getProductInformation(
                SPHelper.getString(mContext!!, SPConstants.ACCESS_TOKEN)!!,
                SPHelper.getString(mContext!!, SPConstants.USER_ID)!!,
                barcode!!
            )
        } else {
            AlertDialogClass(mContext!!).showSimpleDialog(
                "",
                mContext!!.resources.getString(R.string.check_internet_connection),
                mContext!!.resources.getString(R.string.OK)
            )
        }
    }

    private fun getProducts(barcode: String) {
        if (NetUtils.isNetworkAvailable(mContext!!)) {
            viewModel.getProductSalesOrder(
                SPHelper.getString(mContext!!, SPConstants.ACCESS_TOKEN)!!,
                SPHelper.getString(mContext!!, SPConstants.USER_ID)!!,
                barcode,
                orderId.toString()
            )
        } else {
            AlertDialogClass(mContext!!).showSimpleDialog(
                "",
                mContext!!.resources.getString(R.string.check_internet_connection),
                mContext!!.resources.getString(R.string.OK)
            )
        }
    }

    fun updateQuantity(product: Product, qty: Int) {
        if (NetUtils.isNetworkAvailable(mContext!!)) {
            updatingProduct = product
            viewModel.updateQuantity(
                SPHelper.getString(mContext!!, SPConstants.ACCESS_TOKEN)!!,
                SPHelper.getString(mContext!!, SPConstants.USER_ID)!!,
                orderId.toString(),
                product.saleOrderLineId,
                qty
            )
        } else {
            AlertDialogClass(mContext!!).showSimpleDialog(
                "",
                mContext!!.resources.getString(R.string.check_internet_connection),
                mContext!!.resources.getString(R.string.OK)
            )
        }
    }

    private fun getPositionOfProduct(product: Product): Int {
        return arrProduct.indexOf(product)
    }

    private fun confirmOrder() {
        viewModel.confirmOrder(
            SPHelper.getString(mContext!!, SPConstants.ACCESS_TOKEN)!!,
            SPHelper.getString(mContext!!, SPConstants.USER_ID)!!,
            orderId.toString()
        )
    }

    private fun askQuotation() {
        viewModel.getQuotation(
            SPHelper.getString(mContext!!, SPConstants.ACCESS_TOKEN)!!,
            SPHelper.getString(mContext!!, SPConstants.USER_ID)!!,
            orderId.toString()
        )
    }

    private fun setupObserver() {
        viewModel.messageEvent.observe(this, {
            //if (!checkGenericEventAction(it)) {
            try {
                if (viewModel.productInfoModel.type != null) {

                    when (it) {
                        Constants.SUCCESS_GET_PRODUCT_INFORMATION -> {
                            val productId =
                                Integer.parseInt(viewModel.productInfoModel.product_id!!)
                            val existedProduct = isScanProductExisted(productId)
                            if (existedProduct == null) {
                                val product = Product()
                                product.productId = productId
                                product.name = viewModel.productInfoModel.name
                                product.price = viewModel.productInfoModel.price!!
                                product.quantity =
                                    Integer.parseInt(viewModel.productInfoModel.quantity!!)
                                arrProduct.add(product)
                                getProducts(barcode!!)

                                //Refresh - UI
                                runOnUiThread {
                                    productAdapter = ProductAdapter(mContext!!, arrProduct)
                                    rvProducts!!.adapter = productAdapter
                                    productAdapter!!.notifyDataSetChanged()
                                }
                            } else {
                                updateQuantity(existedProduct, 1)
                            }

                        }
                        Constants.SUCCESS_GET_PRODUCT_SALES_ORDER -> {
                            val productId =
                                Integer.parseInt(viewModel.productInfoModel.product_id!!)
                            val saleOrderLineId =
                                Integer.parseInt(viewModel.productInfoModel.saleorderline_id!!)
                            val product = updateProductInformation(productId, saleOrderLineId)
                            if (product != null) {
                                val index = getPositionOfProduct(product)
                                arrProduct[index] = product
                            }

                            //Set Amount Info
                            tvSubTotal!!.text = viewModel.productInfoModel.sub_total
                            tvTaxAmount!!.text = viewModel.productInfoModel.tax_amount
                            tvTotal!!.text = viewModel.productInfoModel.total

                            //Refresh - UI
                            runOnUiThread {
                                productAdapter = ProductAdapter(mContext!!, arrProduct)
                                rvProducts!!.adapter = productAdapter
                                productAdapter!!.notifyDataSetChanged()
                            }
                        }
                        Constants.SUCCESS_UPDATE_QUANTITY -> {
                            val position = getPositionOfProduct(updatingProduct!!)
                            LogUtil.displayLogInfo(
                                "PA",
                                "Update Qty - Response - position : $position"
                            )
                            updatingProduct!!.saleOrderLineId =
                                Integer.parseInt(viewModel.productInfoModel.saleorderline_id!!)
                            updatingProduct!!.quantity =
                                Integer.parseInt(viewModel.productInfoModel.qty!!)
                            arrProduct[position] = updatingProduct!!
                            updatingProduct = null

                            //Set Amount Info
                            tvSubTotal!!.text = viewModel.productInfoModel.sub_total
                            tvTaxAmount!!.text = viewModel.productInfoModel.tax_amount!!
                            tvTotal!!.text = viewModel.productInfoModel.total

                            //Refresh - UI
                            runOnUiThread {
                                productAdapter = ProductAdapter(mContext!!, arrProduct)
                                rvProducts!!.adapter = productAdapter
                                productAdapter!!.notifyDataSetChanged()
                            }
                        }
                        Constants.SUCCESS_CONFIRM_ORDER -> {
                            val arrProducts: JSONArray = viewModel.productInfoModel.products!!
                            val arrConfirmProduct = ArrayList<ConfirmProduct>()
                            if (arrProducts != null) {
                                for (index in 0 until arrProducts.length()) {
                                    val jsonProduct = arrProducts.getJSONObject(index)
                                    val name =
                                        jsonProduct.optString(APIKeys.SalesOrder.Response.NAME)
                                    val description =
                                        jsonProduct.optString(APIKeys.SalesOrder.Response.DESCRIPTION)
                                    val imagePath =
                                        APIConstants.URL + jsonProduct.optString(APIKeys.SalesOrder.Response.IMAGE_PATH)
                                    val confirmProduct = ConfirmProduct()
                                    confirmProduct.description = description
                                    confirmProduct.name = name
                                    confirmProduct.imagePath = imagePath
                                    arrConfirmProduct.add(confirmProduct)
                                }
                                DataUtils.instance!!.products = arrConfirmProduct
                            }
                            startActivity(
                                confirmOrderIntent(
                                    viewModel.productInfoModel.order!!,
                                    viewModel.productInfoModel.message!!,
                                    viewModel.productInfoModel.sub_total!!,
                                    viewModel.productInfoModel.tax_amount!!,
                                    viewModel.productInfoModel.total!!
                                )
                            )
                        }
                        Constants.SUCCESS_GET_QUOTATION -> {
                            val arrProducts: JSONArray = viewModel.productInfoModel.products!!
                            val arrConfirmProduct = ArrayList<ConfirmProduct>()
                            if (arrProducts != null) {
                                for (index in 0 until arrProducts.length()) {
                                    val jsonProduct = arrProducts.getJSONObject(index)
                                    val name =
                                        jsonProduct.optString(APIKeys.SalesOrder.Response.NAME)
                                    val description =
                                        jsonProduct.optString(APIKeys.SalesOrder.Response.DESCRIPTION)
                                    val imagePath =
                                        APIConstants.URL + jsonProduct.optString(APIKeys.SalesOrder.Response.IMAGE_PATH)
                                    val confirmProduct = ConfirmProduct()
                                    confirmProduct.description = description
                                    confirmProduct.name = name
                                    confirmProduct.imagePath = imagePath
                                    arrConfirmProduct.add(confirmProduct)
                                }
                                DataUtils.instance!!.products = arrConfirmProduct
                            }
                            startActivity(
                                requestQuotationIntent(
                                    viewModel.productInfoModel.order!!,
                                    viewModel.productInfoModel.message!!,
                                    viewModel.productInfoModel.sub_total!!,
                                    viewModel.productInfoModel.tax_amount!!,
                                    viewModel.productInfoModel.total!!
                                )
                            )
                        }
                        else -> {

                        }
                    }
                } else {
                    //show dialog
                    val dialogClass = AlertDialogClass(mContext!!)
                    if (viewModel.productInfoModel.message != null) {
                        dialogClass.showSimpleDialog(
                            "",
                            viewModel.productInfoModel.message,
                            mContext!!.getString(R.string.OK)
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

fun Context.productIntent(id: String, orderId: String, name: String): Intent {
    return Intent(this, ProductActivity::class.java).apply {
        putExtra(APIKeys.Customer.Response.ID, id)
        putExtra(APIKeys.Customer.Response.ORDER_ID, orderId)
        putExtra(APIKeys.Customer.Response.NAME, name)
    }
}