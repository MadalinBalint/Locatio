package com.mendelin.locatio.di.main

import com.mendelin.locatio.locations_list.viewmodel.LocationsAdapter
import com.mendelin.locatio.locations_list.viewmodel.LocationDataViewModel
import com.mendelin.locatio.repository.LocationsRepository
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
    fun provideLocationsRepository(service: LocatioApiService): LocationsRepository =
        LocationsRepository(service)

    @MainScope
    @Provides
    fun provideLocationsAdapter(viewModel: LocationDataViewModel): LocationsAdapter = LocationsAdapter(viewModel)

    @MainScope
    @Provides
    fun provideRealmRepository(): RealmRepository = RealmRepository()
}