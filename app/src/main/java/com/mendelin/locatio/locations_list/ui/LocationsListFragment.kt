package com.mendelin.locatio.locations_list.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mendelin.catpedia.constants.Status
import com.mendelin.locatio.R
import com.mendelin.locatio.base_classes.BaseFragment
import com.mendelin.locatio.di.viewmodels.ViewModelProviderFactory
import com.mendelin.locatio.locations_list.adapter.LocationsAdapter
import com.mendelin.locatio.locations_list.adapter.MarginItemDecorationVertical
import com.mendelin.locatio.locations_list.viewmodel.LocationsViewModel
import com.mendelin.locatio.utils.ResourceUtils
import kotlinx.android.synthetic.main.fragment_locations_list.*
import javax.inject.Inject

class LocationsListFragment : BaseFragment(R.layout.fragment_locations_list) {

    private var internectBroadcastReceiver: BroadcastReceiver? = null

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var locationsAdapter: LocationsAdapter

    private lateinit var viewModel: LocationsViewModel

    override fun onResume() {
        super.onResume()

        toolbarOn()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, providerFactory).get(LocationsViewModel::class.java)

        recyclerLocations.apply {
            adapter = locationsAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = true

            addItemDecoration(
                MarginItemDecorationVertical(
                    resources.getDimension(R.dimen.recyclerview_padding).toInt(),
                    resources.getDimension(R.dimen.recyclerview_padding).toInt()
                )
            )
        }

        /* Broadcast receiver to fetch data when the internet connection is back */
        if (internectBroadcastReceiver == null) {
            internectBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    observeViewModel()
                }
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        activity?.registerReceiver(internectBroadcastReceiver, intentFilter)

        /* Setup observers */
        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()

        internectBroadcastReceiver?.let {
            activity?.unregisterReceiver(it)
        }
    }

    private fun observeViewModel() {
        if (viewModel.isListEmpty()) {
            viewModel.readLocationsData()
                .observe(viewLifecycleOwner, { list ->
                    list?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                recyclerLocations.visibility = View.VISIBLE
                                progressLocationsList.visibility = View.GONE
                                resource.data?.let { locations ->
                                    viewModel.setLocationsList(locations)
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
        }

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
}