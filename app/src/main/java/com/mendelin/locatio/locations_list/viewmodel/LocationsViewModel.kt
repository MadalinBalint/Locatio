package com.mendelin.locatio.locations_list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mendelin.locatio.repository.LocationsRepository
import com.mendelin.locatio.models.LocationInfoObject
import javax.inject.Inject

class LocationsViewModel @Inject constructor(
    private val locationsRepository: LocationsRepository,
) : ViewModel() {
    private val originalBreedList: ArrayList<LocationInfoObject> = arrayListOf()

    private val breedsList = MutableLiveData<ArrayList<LocationInfoObject>>()

    private val errorFilter = MutableLiveData<String>()

    fun getOriginalBreedList(): ArrayList<LocationInfoObject> = originalBreedList
    fun setOriginalBreedList(list: List<LocationInfoObject>) {
        originalBreedList.apply {
            clear()
            addAll(list)
        }

        breedsList.postValue(originalBreedList)
    }

    fun getBreedsList() = breedsList
    fun getErrorFilter() = errorFilter

    fun readBreedsData() = locationsRepository.readData()
}