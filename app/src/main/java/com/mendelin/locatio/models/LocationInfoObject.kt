package com.mendelin.locatio.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class LocationInfoObject(
    @SerializedName(value="lat", alternate = ["latitude"])
    var lat: Double,

    @SerializedName(value="lng", alternate = ["longitude"])
    val lng: Double,

    val label: String?,
    val address: String?,
    val image: String?
) : Parcelable