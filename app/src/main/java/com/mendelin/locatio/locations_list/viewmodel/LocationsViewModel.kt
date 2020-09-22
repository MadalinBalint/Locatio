package com.mendelin.locatio.locations_list.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mendelin.catpedia.retrofit.Resource
import com.mendelin.locatio.models.LocationInfoObject
import com.mendelin.locatio.models.LocationInfoRealmObject
import com.mendelin.locatio.repository.LocationsRepository
import com.mendelin.locatio.repository.RealmRepository
import com.mendelin.locatio.utils.ResourceUtils
import timber.log.Timber
import javax.inject.Inject

class LocationsViewModel @Inject constructor(
    private val locationsRepository: LocationsRepository,
    private val repository: RealmRepository
) : ViewModel() {
    private val originalLocationsList: ArrayList<LocationInfoRealmObject> = arrayListOf()

    private val locationsList = MutableLiveData<ArrayList<LocationInfoRealmObject>>()
    private val errorFilter = MutableLiveData<String>()
    private val lastLocation = MutableLiveData<Location>()

    fun getLocationsList(): LiveData<ArrayList<LocationInfoRealmObject>> = locationsList
    fun setLocationsList(list: List<LocationInfoRealmObject>) {
        originalLocationsList.apply {
            clear()
            addAll(list)
        }

        locationsList.postValue(originalLocationsList)
    }

    fun getErrorFilter() = errorFilter

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
            locationsRepository.readData()
        }
    }

    fun saveLastLocation(location: Location) = lastLocation.postValue(location)
    fun setDistanceFromCurrentLocation() {
        val location = lastLocation.value

        location?.let {
            originalLocationsList.forEach {
                it.distance = ResourceUtils.getDistanceBetweenLocations(it.lat, it.lng, location)
            }

            locationsList.postValue(originalLocationsList)
        }
    }
}