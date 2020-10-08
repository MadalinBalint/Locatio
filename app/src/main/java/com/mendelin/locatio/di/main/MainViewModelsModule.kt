package com.mendelin.locatio.di.main

import androidx.lifecycle.ViewModel
import com.mendelin.locatio.di.viewmodels.ViewModelKey
import com.mendelin.locatio.viewmodels.LocationsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(LocationsViewModel::class)
    abstract fun bindLocationsViewModel(locationsViewModel: LocationsViewModel): ViewModel
}