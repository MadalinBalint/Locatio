package com.mendelin.locatio.locations_list.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
@JsonClass(generateAdapter = true)
data class LocationInfoResponse(
    val lat: Float,
    val lng: Float,
    val label: String,
    val address: String,
    val image: String
) : Parcelable