package com.mendelin.locatio.di.main

import com.mendelin.locatio.locations_list.adapter.LocationsAdapter
import com.mendelin.locatio.repository.LocationsRepository
import com.mendelin.locatio.retrofit.LocatioApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {
    @MainScope
    @Provides
    fun provideRestApi(retrofit: Retrofit): LocatioApiService =
        retrofit.create(LocatioApiService::class.java)

    @MainScope
    @Provides
    fun provideLocationsRepository(service: LocatioApiService): LocationsRepository =
        LocationsRepository(service)

    @MainScope
    @Provides
    fun provideLocationsAdapter(): LocationsAdapter = LocationsAdapter()
}