package com.mendelin.locatio.di

import com.mendelin.locatio.di.main.MainFragmentBuildersModule
import com.mendelin.locatio.di.main.MainModule
import com.mendelin.locatio.di.main.MainScope
import com.mendelin.locatio.di.main.MainViewModelsModule
import com.mendelin.locatio.di.welcome_screen.WelcomeScreenModule
import com.mendelin.locatio.di.welcome_screen.WelcomeScreenScope
import com.mendelin.locatio.di.welcome_screen.WelcomeScreenViewModelsModule
import com.mendelin.locatio.ui.activity.MainActivity
import com.mendelin.locatio.ui.activity.WelcomeScreenActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @WelcomeScreenScope
    @ContributesAndroidInjector(
        modules = [
            WelcomeScreenViewModelsModule::class,
            WelcomeScreenModule::class
        ]
    )
    abstract fun contributeWelcomeScreenActivity(): WelcomeScreenActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [
            MainFragmentBuildersModule::class,
            MainViewModelsModule::class,
            MainModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}