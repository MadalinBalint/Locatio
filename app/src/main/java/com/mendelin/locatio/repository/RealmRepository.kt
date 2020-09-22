package com.mendelin.locatio.repository

import com.mendelin.locatio.models.LocationInfoObject
import com.mendelin.locatio.models.LocationInfoRealmObject
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class RealmRepository {
    private val realm = Realm.getDefaultInstance()

    fun countLocations(): Long = realm.where<LocationInfoRealmObject>().count()

    fun createLocationObject(latitude: Double, longitude: Double, locationLabel: String, locationAddress: String, imageUrl: String) {
        realm.executeTransaction {
            val pos =
                realm.createObject<LocationInfoRealmObject>("$latitude, $longitude, $locationLabel, $locationAddress, $imageUrl".hashCode().toLong())
            with(pos) {
                lat = latitude
                lng = longitude
                label = locationLabel
                address = locationAddress
                image = imageUrl
            }
        }
    }

    fun readLocationsList(): List<LocationInfoRealmObject> {
        val results = realm.where<LocationInfoRealmObject>().findAll()
        return results.toList()
    }

    fun updateLocationObject(obj: LocationInfoRealmObject, lat: Double, lng: Double, label: String, address: String, image: String) {
        realm.executeTransaction {
            if (obj.lat != lat) obj.lat = lat
            if (obj.lng != lng) obj.lng = lng
            if (obj.label != label) obj.label = label
            if (obj.address != address) obj.address = address
            if (obj.image != image) obj.image = image
        }
    }

    fun saveLocationsList(list: List<LocationInfoObject>) {
        realm.executeTransaction {
            list.forEach {
                val pos =
                    realm.createObject<LocationInfoRealmObject>("${it.lat}, ${it.lng}, ${it.label}, ${it.address}, ${it.image}".hashCode().toLong())
                with(pos) {
                    lat = it.lat
                    lng = it.lng
                    label = it.label
                    address = it.address
                    image = it.image
                }
            }
        }
    }

    fun findLocationObject(id: Long) =
        realm.where(LocationInfoRealmObject::class.java)
            .equalTo("id", id)
            .findFirst()
}