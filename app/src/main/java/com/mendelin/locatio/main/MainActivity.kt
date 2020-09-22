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
import com.mendelin.locatio.service.ForegroundOnlyLocationService
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity(R.layout.activity_main) {

    companion object {
        const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 1000
    }

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private var foregroundOnlyLocationServiceBound = false

    // Provides location updates for while-in-use feature.
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null


    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    override fun onStart() {
        super.onStart()

        val serviceIntent = Intent(this, ForegroundOnlyLocationService::class.java)
        bindService(serviceIntent, foregroundOnlyServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        if (foregroundOnlyLocationServiceBound) {
            unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }

        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (foregroundPermissionApproved()) {
            foregroundOnlyLocationService?.subscribeToLocationUpdates()
                ?: Timber.d("Service Not Bound")
        } else {
            requestForegroundPermissions()
        }

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

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        /* If the user denied a previous request, but didn't check "Don't ask again", provide additional rationale. */
        if (provideRationale) {
            Snackbar.make(
                findViewById(R.id.layoutMainActivity),
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE)
                }
                .show()
        } else {
            Timber.d("Request foreground only permission")
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Timber.d("onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    /* If user interaction was interrupted, the permission request is cancelled and you receive empty arrays */
                    Timber.d("User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    /* Permission was granted */
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()

                else -> {
                    /* Permission denied */
                    Snackbar.make(findViewById(R.id.layoutMainActivity), R.string.permission_denied_explanation, Snackbar.LENGTH_LONG)
                        .setAction(R.string.settings) {
                            val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            with(Intent()) {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = uri
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(this)
                            }
                        }
                        .show()
                }
            }
        }
    }
}