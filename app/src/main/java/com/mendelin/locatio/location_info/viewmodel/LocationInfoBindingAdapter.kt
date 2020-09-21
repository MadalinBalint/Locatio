package com.mendelin.locatio.location_info.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.mendelin.locatio.utils.ResourceUtils

@BindingAdapter("locationImage")
/* Binding adapter for the location image in locations list */
fun bindImage(imgView: ImageView, imgUrl: String?) {
    ResourceUtils.showImage(imgView, imgUrl)
}