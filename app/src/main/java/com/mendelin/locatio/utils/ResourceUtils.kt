package com.mendelin.locatio.utils

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mendelin.locatio.R
import com.mendelin.locatio.custom_views.AlertBox
import java.io.IOException
import java.util.*

object ResourceUtils {
    fun showErrorAlert(context: Context, msg: String) {
        val alert = AlertBox()

        alert.setPositiveButtonListener { dialog, _ ->
            dialog.dismiss()
        }

        alert.showAlert(context, context.getString(R.string.alert_error), msg, context.getString(R.string.alert_ok), null)
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
                .error(R.drawable.ic_placeholder)
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgView)
        }
    }

    fun getAddressLong(context: Context, lat: Double, lng: Double): String {
        val array = arrayListOf<String>()
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (addresses != null && addresses.size > 0) {
                val obj = addresses[0]
                if (obj != null) {
                    /* Address */
                    if (!obj.getAddressLine(0).isNullOrEmpty())
                        array.add(obj.getAddressLine(0))

                    /* Country */
                    /*if (!obj.countryName.isNullOrEmpty())
                        array.add(obj.countryName)*/

                    /* Country code */
                    if (!obj.countryCode.isNullOrEmpty())
                        array.add(obj.countryCode)

                    /* Admin Area */
                    /*if (!obj.adminArea.isNullOrEmpty())
                        array.add("Adamin "+obj.adminArea)*/

                    /* Postal Code */
                    /*if (!obj.postalCode.isNullOrEmpty())
                        array.add(obj.postalCode)*/

                    /* Sub Admin Area */
                    if (!obj.subAdminArea.isNullOrEmpty())
                        array.add(obj.subAdminArea)

                    /* Locality */
                    /*if (!obj.locality.isNullOrEmpty())
                        array.add(obj.locality)*/

                    /* Sub Locality */
                    /*if (!obj.subLocality.isNullOrEmpty())
                        array.add(obj.subLocality)*/

                    /* Sub Thoroughfare */
                    /*if (!obj.subThoroughfare.isNullOrEmpty())
                        array.add(obj.subThoroughfare)*/

                    return array.joinToString(separator = ", ")
                }
            } else {
                return ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getDistanceBetweenLocations(lat: Double, lng: Double, lastSentLocation: Location): Float {
        val location = Location("l1")
        location.latitude = lat
        location.longitude = lng
        val lastLocation = Location("l2")
        lastLocation.latitude = lastSentLocation.latitude
        lastLocation.longitude = lastSentLocation.longitude
        return location.distanceTo(lastLocation) / 1000.0f
    }
}