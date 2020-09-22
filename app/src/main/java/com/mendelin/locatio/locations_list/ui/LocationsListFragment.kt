package com.mendelin.locatio.locations_list.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.mendelin.catpedia.constants.Status
import com.mendelin.locatio.R
import com.mendelin.locatio.base_classes.BaseFragment
import com.mendelin.locatio.di.viewmodels.ViewModelProviderFactory
import com.mendelin.locatio.locations_list.viewmodel.LocationDataViewModel
import com.mendelin.locatio.locations_list.viewmodel.LocationsAdapter
import com.mendelin.locatio.locations_list.viewmodel.LocationsViewModel
import com.mendelin.locatio.repository.RealmRepository
import com.mendelin.locatio.service.ForegroundOnlyLocationService
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

    // Listens for location broadcasts from ForegroundOnlyLocationService.
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver

    override fun onResume() {
        super.onResume()

        toolbarOn()

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
            )
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
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

    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(ForegroundOnlyLocationService.EXTRA_LOCATION)

            location?.let {
                //dataViewModel.setLocation(it)
                viewModel.setDistanceFromCurrentLocation(it)
                Timber.e(it.toString())
            }
        }
    }
}