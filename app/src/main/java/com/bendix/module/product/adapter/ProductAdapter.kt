package com.bendix.module.product.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.recyclerview.widget.RecyclerView
import com.bendix.databinding.RowProductBinding
import com.bendix.interfaces.Product
import com.bendix.module.product.ProductActivity
import java.lang.String
import java.util.*

class ProductAdapter(private val mContext: Context, private val arrProduct: ArrayList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductVH>() {
    private val context = mContext
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        val binding =
            RowProductBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductVH(binding)
    }

    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        try {
            holder.binding.tvName.text = arrProduct[position].name
            holder.binding.tvPrice.text = arrProduct[position].price
            holder.binding.tvQuantity.setText(String.valueOf(arrProduct[position].quantity))
            holder.binding.tvQuantity.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.e(
                        "PA",
                        "PA - Adapter - tvQuantity : " + holder.binding.tvQuantity.text.toString()
                    )
                    (mContext as ProductActivity).updateQuantity(
                        arrProduct[position],
                        holder.binding.tvQuantity.text.toString().toInt()
                    )
                    return@OnEditorActionListener true
                }
                false
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return arrProduct.size
    }

    inner class ProductVH(val binding: RowProductBinding) : RecyclerView.ViewHolder(binding.root)

}