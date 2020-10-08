package com.mendelin.locatio.viewmodels

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mendelin.locatio.models.LocationInfoObject
import com.mendelin.locatio.models.LocationInfoRealmObject
import com.mendelin.locatio.repository.LocationsListRepository
import com.mendelin.locatio.repository.RealmRepository
import com.mendelin.locatio.retrofit.Resource
import timber.log.Timber
import javax.inject.Inject

class LocationsViewModel @Inject constructor(
    private val locationsListRepository: LocationsListRepository,
    private val repository: RealmRepository
) : ViewModel() {
    private val originalLocationsList: ArrayList<LocationInfoRealmObject> = arrayListOf()
    private val locationsList = MutableLiveData<ArrayList<LocationInfoRealmObject>>()
    private val error = MutableLiveData<String>()
    private var lastLocation: Location? = null

    fun getLocationsList(): LiveData<ArrayList<LocationInfoRealmObject>> = locationsList
    fun setLocationsList(list: List<LocationInfoRealmObject>) {
        originalLocationsList.apply {
            clear()
            addAll(list)
        }

        locationsList.postValue(originalLocationsList)
    }

    fun getError() = error

    fun readLocationsData(): LiveData<Resource<List<LocationInfoObject>>>? {
        val count = repository.countLocations()
        Timber.e("Locations count = $count")
        return if (count > 0) {
            Timber.e("Reading data from Realm database")
            val list = repository.readLocationsList()
            setLocationsList(list)
            setDistanceFromCurrentLocation()
            null
        } else {
            Timber.e("Reading data from REST API")
            locationsListRepository.readData()
        }
    }

    fun saveLastLocation(location: Location) {
        lastLocation = location
    }

    fun errorHandled() {
        error.value = ""
    }

    fun setDistanceFromCurrentLocation() {
        lastLocation?.let { location ->
            originalLocationsList.forEach {
                it.distance = getDistanceBetweenLocations(it.lat, it.lng, location)
            }

            locationsList.value = originalLocationsList
        }
    }

    private fun getDistanceBetweenLocations(lat: Double, lng: Double, lastSentLocation: Location): Float {
        val location = Location("l1")
        location.latitude = lat
        location.longitude = lng
        val lastLocation = Location("l2")
        lastLocation.latitude = lastSentLocation.latitude
        lastLocation.longitude = lastSentLocation.longitude
        return location.distanceTo(lastLocation) / 1000.0f
    }
}