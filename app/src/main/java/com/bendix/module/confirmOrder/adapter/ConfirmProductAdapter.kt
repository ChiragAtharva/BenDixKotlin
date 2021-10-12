package com.bendix.module.confirmOrder.adapter

import ConfirmProduct
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bendix.R
import com.bendix.databinding.RowProductInfoBinding
import com.squareup.picasso.Picasso
import java.util.*

class ConfirmProductAdapter(
    private val mContext: Context,
    private val arrConfirmProduct: ArrayList<ConfirmProduct>
) :
    RecyclerView.Adapter<ConfirmProductAdapter.ConfirmProductVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmProductVH {
        val binding =
            RowProductInfoBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ConfirmProductVH(binding)
    }

    override fun onBindViewHolder(holder: ConfirmProductVH, position: Int) {
        try {
            holder.binding.tvName.text = arrConfirmProduct[position].name
            holder.binding.tvDetail.text = arrConfirmProduct[position].description
            val imagePath: String = arrConfirmProduct[position].imagePath!!
            if (imagePath != null)
                Picasso.get()
                    .load(imagePath)
                    .placeholder(R.mipmap.ic_product)
                    .error(R.mipmap.ic_product)
                    .into(holder.binding.ivProduct)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return arrConfirmProduct.size
    }

    inner class ConfirmProductVH(val binding: RowProductInfoBinding) :
        RecyclerView.ViewHolder(binding.root)
}