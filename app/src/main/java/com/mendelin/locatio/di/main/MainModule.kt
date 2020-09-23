package com.mendelin.locatio.di.main

import com.mendelin.locatio.locations_list.adapter.LocationsAdapter
import com.mendelin.locatio.repository.GpsLocationProvider
import com.mendelin.locatio.repository.LocationsListRepository
import com.mendelin.locatio.repository.RealmRepository
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
    fun provideLocationsRepository(service: LocatioApiService): LocationsListRepository =
        LocationsListRepository(service)

    @MainScope
    @Provides
    fun provideLocationsAdapter(): LocationsAdapter = LocationsAdapter()

    @MainScope
    @Provides
    fun provideRealmRepository(): RealmRepository = RealmRepository()

    @MainScope
    @Provides
    fun provideGpsLocationProvider(): GpsLocationProvider = GpsLocationProvider()
}