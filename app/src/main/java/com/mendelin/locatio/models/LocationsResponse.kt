package com.mendelin.locatio.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class LocationsResponse(
    val status: String,
    val locations: List<LocationInfoObject>
) : Parcelable