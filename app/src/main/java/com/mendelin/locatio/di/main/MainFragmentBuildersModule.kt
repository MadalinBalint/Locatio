package com.mendelin.locatio.di.main

import com.mendelin.locatio.add_location.AddLocationFragment
import com.mendelin.locatio.edit_location.EditLocationFragment
import com.mendelin.locatio.location_info.ui.LocationInfoFragment
import com.mendelin.locatio.locations_list.ui.LocationsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeLocationsListFragment(): LocationsListFragment

    @ContributesAndroidInjector
    abstract fun contributeLocationInfoFragment(): LocationInfoFragment

    @ContributesAndroidInjector
    abstract fun contributeAddLocationFragment(): AddLocationFragment

    @ContributesAndroidInjector
    abstract fun contributeEditLocationFragment(): EditLocationFragment
}