package com.mendelin.locatio.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mendelin.locatio.R
import com.mendelin.locatio.utils.ResourceUtils

@BindingAdapter("locationImage")
/* Binding adapter for the location image in locations list */
fun bindLocationImage(imgView: ImageView, imageUrl: String?) {
    imageUrl?.let {
        val circularProgressDrawable = CircularProgressDrawable(imgView.context)
        circularProgressDrawable.strokeWidth = 6f
        circularProgressDrawable.centerRadius = 50f
        circularProgressDrawable.start()

        Glide.with(imgView.context)
            .applyDefaultRequestOptions(
                RequestOptions()
                    .format(DecodeFormat.PREFER_RGB_565)
                    .disallowHardwareConfig()
            )
            .load(imageUrl)
            .optionalCenterCrop()
            .error(R.drawable.ic_placeholder)
            .placeholder(circularProgressDrawable)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgView)
    }
}