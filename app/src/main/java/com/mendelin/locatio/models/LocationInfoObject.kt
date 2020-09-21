package com.mendelin.locatio.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class LocationInfoObject(
    @SerializedName(value="lat", alternate = ["latitude"])
    val lat: Float,

    @SerializedName(value="lng", alternate = ["longitude"])
    val lng: Float,

    val label: String,
    val address: String,
    val image: String?
) : Parcelable