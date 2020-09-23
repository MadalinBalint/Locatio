package com.mendelin.locatio.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.mendelin.locatio.R
import com.mendelin.locatio.locations_list.ui.LocationsListFragment
import timber.log.Timber

class GpsLocationProvider {
    private var permissionAsked = false

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private fun gpsPermissionApproved(context: Context) =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun requestGpsPermissions(context: Context, fragment: Fragment) {
        val provideRationale = gpsPermissionApproved(context)

        /* If the user denied a previous request, but didn't check "Don't ask again", provide additional rationale. */
        if (provideRationale) {
            Snackbar.make(
                fragment.requireActivity().findViewById(R.id.layoutMainActivity),
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.ok) {
                    fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LocationsListFragment.REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE)
                }
                .show()
        } else {
            if (!permissionAsked) {
                permissionAsked = true
                Timber.d("Request foreground only permission")
                fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LocationsListFragment.REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE)
            }
        }
    }

    private fun createLocationRequest() =
        LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    fun createFusedLocationProvider(activity: FragmentActivity) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(context: Context, fragment: Fragment, locationCallback: LocationCallback) {
        if (gpsPermissionApproved(context)) {
            Timber.e("foregroundPermissionApproved")
            getLocation(locationCallback)
        } else {
            requestGpsPermissions(context, fragment)
        }
    }

    fun stopLocationUpdates(locationCallback: LocationCallback) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    fun getLocation(locationCallback: LocationCallback) {
        Timber.e("getLocation")

        fusedLocationClient.requestLocationUpdates(createLocationRequest(), locationCallback, Looper.getMainLooper())
    }
}