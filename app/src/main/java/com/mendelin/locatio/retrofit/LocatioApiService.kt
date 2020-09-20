package com.mendelin.locatio.retrofit

import com.mendelin.locatio.BuildConfig
import com.mendelin.locatio.locations_list.models.LocationInfoResponse
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET

interface LocatioApiService {
    @GET(BuildConfig.ENDPOINT_MY_LOCATIONS)
    fun getMyLocations(): Flowable<List<LocationInfoResponse>>
}