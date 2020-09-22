package com.mendelin.locatio.models

import android.os.Parcelable
import androidx.annotation.Keep
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
open class LocationInfoRealmObject : RealmObject(), Parcelable {
    @PrimaryKey
    var id: Long = 0
    var lat: Double = 0.0
    var lng: Double = 0.0
    var label: String? = null
    var address: String? = null
    var image: String? = null

    @Ignore
    var distance: Float = 0.0f
}