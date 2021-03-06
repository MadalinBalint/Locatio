package com.mendelin.locatio.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mendelin.locatio.models.LocationInfoObject
import com.mendelin.locatio.retrofit.LocatioApiService
import com.mendelin.locatio.retrofit.Resource
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LocationsListRepository @Inject constructor(
    private val service: LocatioApiService
) {
    private fun getLocationsList(): LiveData<Resource<List<LocationInfoObject>>> {
        val locationsList: MutableLiveData<Resource<List<LocationInfoObject>>> = MutableLiveData()
        locationsList.postValue(Resource.loading(data = null))

        service.getMyLocations()
            .subscribeOn(Schedulers.io())
            .subscribe({
                locationsList.postValue(Resource.success(it.locations))
            },
                {
                    locationsList.postValue(
                        Resource.error(data = null, message = it.message ?: "Error Occurred!")
                    )
                }
            )

        return locationsList
    }

    fun readData() = getLocationsList()
}