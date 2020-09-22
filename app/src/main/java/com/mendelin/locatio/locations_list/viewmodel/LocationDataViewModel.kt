package com.mendelin.locatio.locations_list.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LocationDataViewModel @Inject constructor() : ViewModel() {
    private val location: MutableLiveData<Location> = MutableLiveData()

    fun setLocation(pos: Location) {
        location.postValue(pos)
    }

    fun getLocation(): LiveData<Location> = location
}