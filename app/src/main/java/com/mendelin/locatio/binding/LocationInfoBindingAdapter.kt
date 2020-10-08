package com.mendelin.locatio.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.mendelin.locatio.utils.ResourceUtils

@BindingAdapter("locationImage")
/* Binding adapter for the location image in locations list */
fun bindLocationImage(imgView: ImageView, imgUrl: String?) {
    ResourceUtils.showImage(imgView, imgUrl)
}