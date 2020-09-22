package com.mendelin.locatio.main

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.mendelin.locatio.BuildConfig
import com.mendelin.locatio.R
import com.mendelin.locatio.base_classes.BaseActivity
import com.mendelin.locatio.di.viewmodels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(R.layout.activity_main) {

    companion object {
        const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 1000
    }

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        supportActionBar?.let {
            with(it) {
                setDisplayShowTitleEnabled(false)
                setHomeAsUpIndicator(null)
                setHomeButtonEnabled(false)
                setDisplayHomeAsUpEnabled(false)
            }
        }

        btnAddLocation.setOnClickListener {
            Navigation.findNavController(this, R.id.navHostFragment).navigate(R.id.addLocationFragment)
        }
    }
}