package com.mendelin.locatio

import androidx.appcompat.app.AppCompatDelegate
import com.mendelin.locatio.di.DaggerAppComponent
import com.mendelin.locatio.logging.TimberPlant
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

class LocatioApplication @Inject constructor() : DaggerApplication() {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        TimberPlant.plantTimberDebugLogger()
    }
}