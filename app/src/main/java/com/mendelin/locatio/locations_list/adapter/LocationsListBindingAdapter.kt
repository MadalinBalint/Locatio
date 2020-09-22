package com.mendelin.locatio.locations_list.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.mendelin.locatio.utils.ResourceUtils

@BindingAdapter("imageUrl")
/* Binding adapter for the location image in locations list */
fun bindImage(imgView: ImageView, imgUrl: String?) {
    ResourceUtils.showImage(imgView, imgUrl)
}