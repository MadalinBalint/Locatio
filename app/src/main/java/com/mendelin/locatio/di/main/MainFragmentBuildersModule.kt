package com.mendelin.locatio.di.main

import com.mendelin.locatio.ui.fragments.AddLocationFragment
import com.mendelin.locatio.ui.fragments.EditLocationFragment
import com.mendelin.locatio.ui.fragments.LocationInfoFragment
import com.mendelin.locatio.ui.fragments.LocationsListFragment
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