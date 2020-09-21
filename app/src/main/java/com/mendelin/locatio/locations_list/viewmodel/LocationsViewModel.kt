package com.mendelin.locatio.locations_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mendelin.locatio.models.LocationInfoObject
import com.mendelin.locatio.repository.LocationsRepository
import javax.inject.Inject

class LocationsViewModel @Inject constructor(
    private val locationsRepository: LocationsRepository,
) : ViewModel() {
    private val originalLocationsList: ArrayList<LocationInfoObject> = arrayListOf()

    private val locationsList = MutableLiveData<ArrayList<LocationInfoObject>>()
    private val errorFilter = MutableLiveData<String>()

    fun isListEmpty() = originalLocationsList.isEmpty()
    fun getLocationsList(): LiveData<ArrayList<LocationInfoObject>> = locationsList
    fun setLocationsList(list: List<LocationInfoObject>) {
        originalLocationsList.apply {
            clear()
            addAll(list)
        }

        locationsList.postValue(originalLocationsList)
    }

    fun getErrorFilter() = errorFilter

    fun readLocationsData() = locationsRepository.readData()
}