package com.mendelin.locatio.locations_list.ui

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.snackbar.Snackbar
import com.mendelin.catpedia.constants.Status
import com.mendelin.locatio.BuildConfig
import com.mendelin.locatio.R
import com.mendelin.locatio.base_classes.BaseFragment
import com.mendelin.locatio.di.viewmodels.ViewModelProviderFactory
import com.mendelin.locatio.locations_list.viewmodel.LocationDataViewModel
import com.mendelin.locatio.locations_list.viewmodel.LocationsAdapter
import com.mendelin.locatio.locations_list.viewmodel.LocationsViewModel
import com.mendelin.locatio.main.MainActivity
import com.mendelin.locatio.repository.RealmRepository
import com.mendelin.locatio.utils.ResourceUtils
import kotlinx.android.synthetic.main.fragment_locations_list.*
import timber.log.Timber
import javax.inject.Inject

class LocationsListFragment : BaseFragment(R.layout.fragment_locations_list) {

    @Inject
    lateinit var repository: RealmRepository

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var locationsAdapter: LocationsAdapter

    private lateinit var viewModel: LocationsViewModel

    private lateinit var dataViewModel: LocationDataViewModel

    override fun onResume() {
        super.onResume()

        toolbarOn()
    }

    private fun getLocation() {
        val locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager?

        val locationListener = LocationListener {
            viewModel.setDistanceFromCurrentLocation(it)
            Timber.e(it.toString())
        }

        try {
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000L, 0f, locationListener)
        } catch (ex: SecurityException) {
            Toast.makeText(activity, "Error while trying to retrieve location!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, providerFactory).get(LocationsViewModel::class.java)

        dataViewModel = ViewModelProvider(this, providerFactory).get(LocationDataViewModel::class.java)

        recyclerLocations.apply {
            adapter = locationsAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            isNestedScrollingEnabled = true
        }

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerLocations)

        if (foregroundPermissionApproved()) {
            getLocation()
        } else {
            requestForegroundPermissions()
        }

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

    private fun foregroundPermissionApproved() =
        PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        /* If the user denied a previous request, but didn't check "Don't ask again", provide additional rationale. */
        if (provideRationale) {
            Snackbar.make(
                requireActivity().findViewById(R.id.layoutMainActivity),
                R.string.permission_rationale,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MainActivity.REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE)
                }
                .show()
        } else {
            Timber.d("Request foreground only permission")
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MainActivity.REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Timber.d("onRequestPermissionResult")

        when (requestCode) {
            MainActivity.REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    /* If user interaction was interrupted, the permission request is cancelled and you receive empty arrays */
                    Timber.d("User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED ->
                    /* Permission was granted */
                    getLocation()

                else -> {
                    /* Permission denied */
                    Snackbar.make(requireActivity().findViewById(R.id.layoutMainActivity), R.string.permission_denied_explanation, Snackbar.LENGTH_LONG)
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