package com.mendelin.locatio.locations_list.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.material.snackbar.Snackbar
import com.mendelin.locatio.BuildConfig
import com.mendelin.locatio.R
import com.mendelin.locatio.constants.Status
import com.mendelin.locatio.di.viewmodels.ViewModelProviderFactory
import com.mendelin.locatio.locations_list.adapter.LocationsAdapter
import com.mendelin.locatio.locations_list.viewmodel.LocationsViewModel
import com.mendelin.locatio.repository.GpsLocationProvider
import com.mendelin.locatio.repository.RealmRepository
import com.mendelin.locatio.utils.ResourceUtils
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_locations_list.*
import timber.log.Timber
import javax.inject.Inject

class LocationsListFragment : DaggerFragment(R.layout.fragment_locations_list) {

    private var permissionDeniedShown = false

    companion object {
        const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 1000
    }

    @Inject
    lateinit var gpsLocationProvider: GpsLocationProvider

    @Inject
    lateinit var repository: RealmRepository

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var locationsAdapter: LocationsAdapter

    private lateinit var viewModel: LocationsViewModel

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult?.let { result ->
                result.locations.firstOrNull()?.let {
                    viewModel.saveLastLocation(it)
                    viewModel.setDistanceFromCurrentLocation()
                    Timber.e(it.toString())
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        gpsLocationProvider.stopLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.visibility = View.VISIBLE

        if (!ResourceUtils.getGpsStatus(requireContext())) {
            Snackbar.make(requireActivity().findViewById(R.id.layoutMainActivity), R.string.gsp_off_explanation, Snackbar.LENGTH_LONG).show()
        }

        gpsLocationProvider.startLocationUpdates(requireContext(), this, locationCallback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gpsLocationProvider.createFusedLocationProvider(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, providerFactory).get(LocationsViewModel::class.java)

        recyclerLocations.apply {
            adapter = locationsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            isNestedScrollingEnabled = true
        }

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerLocations)

        /* Setup observers */
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.readLocationsData()
            ?.observe(viewLifecycleOwner, { list ->
                list?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            recyclerLocations.visibility = View.VISIBLE
                            progressLocationsList.visibility = View.GONE
                            resource.data?.let { locations ->
                                repository.saveLocationsList(locations)
                                viewModel.setLocationsList(repository.readLocationsList())
                                viewModel.setDistanceFromCurrentLocation()
                            }
                        }
                        Status.ERROR -> {
                            recyclerLocations.visibility = View.VISIBLE
                            progressLocationsList.visibility = View.GONE
                            ResourceUtils.showErrorAlert(
                                requireContext(), list.message
                                    ?: getString(R.string.alert_error_unknown)
                            )
                        }
                        Status.LOADING -> {
                            progressLocationsList.visibility = View.VISIBLE
                            recyclerLocations.visibility = View.GONE
                        }
                    }
                }
            })

        viewModel.getLocationsList()
            .observe(viewLifecycleOwner, { list ->
                list?.let {
                    recyclerLocations.visibility = View.GONE
                    locationsAdapter.setList(list)
                    locationsAdapter.notifyDataSetChanged()
                    recyclerLocations.visibility = View.VISIBLE
                }
            })

        viewModel.getErrorFilter()
            .observe(viewLifecycleOwner, { error ->
                if (error.isNotEmpty()) {
                    ResourceUtils.showErrorAlert(requireContext(), error)
                    viewModel.getErrorFilter().postValue("")
                }
            })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Timber.d("onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    /* If user interaction was interrupted, the permission request is cancelled and you receive empty arrays */
                    Timber.d("User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    /* Permission was granted */
                    Timber.e("PERMISSION_GRANTED")
                    gpsLocationProvider.getLocation(locationCallback)
                }

                else -> {
                    /* Permission denied */
                    if (!permissionDeniedShown) {
                        permissionDeniedShown = true

                        Snackbar.make(requireActivity().findViewById(R.id.layoutMainActivity), R.string.permission_denied_explanation, Snackbar.LENGTH_LONG)
                            .setAction(R.string.settings) {
                                val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                                val intent = Intent()
                                with(intent) {
                                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    data = uri
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK

                                }
                                startActivity(intent)
                            }
                            .show()
                    }
                }
            }
        }
    }
}