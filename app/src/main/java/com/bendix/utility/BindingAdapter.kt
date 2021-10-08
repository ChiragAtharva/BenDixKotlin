package com.bendix.utility

import android.graphics.drawable.Drawable
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BindingAdapter {
    companion object {
        @BindingAdapter("data")
        @JvmStatic
        fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, items: List<T>) {
            if (recyclerView.adapter is BindableAdapter<*>) {
                (recyclerView.adapter as BindableAdapter<T>).setData(items)
            }
        }

        @BindingAdapter(value = ["bind:setImage", "bind:placeHolder"], requireAll = false)
        @JvmStatic
        fun loadImage(view: ImageView, imageUrl: String?, placeHolderId: Drawable) {
            Glide.with(view.context)
                .load(imageUrl)
                //.transition(withCrossFade())
                .placeholder(placeHolderId)
                .into(view)
        }
    }
}

interface BindableAdapter<T> {
    fun setData(items: List<T>)
    fun changedPositions(positions: Set<Int>)
}