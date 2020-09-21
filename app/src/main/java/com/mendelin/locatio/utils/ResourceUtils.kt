package com.mendelin.locatio.utils

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mendelin.locatio.R
import com.mendelin.locatio.custom_views.AlertBox
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

object ResourceUtils {
    fun showErrorAlert(context: Context, msg: String) {
        val alert = AlertBox()

        alert.setPositiveButtonListener { dialog, _ ->
            dialog.dismiss()
        }

        alert.showAlert(
            context,
            context.getString(R.string.alert_error),
            msg,
            context.getString(R.string.alert_ok),
            null
        )
    }

    fun showImage(imgView: ImageView, imageUrl: String?) {
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
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgView)
        }
    }
}