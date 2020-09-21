package com.mendelin.locatio.retrofit

import com.mendelin.locatio.BuildConfig
import com.mendelin.locatio.models.LocationsResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface LocatioApiService {
    @GET(BuildConfig.ENDPOINT_MY_LOCATIONS)
    fun getMyLocations(): Single<LocationsResponse>
}