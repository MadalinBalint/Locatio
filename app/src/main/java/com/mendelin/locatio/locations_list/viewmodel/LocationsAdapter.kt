package com.mendelin.locatio.locations_list.viewmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mendelin.locatio.ItemLocationListDataBinding
import com.mendelin.locatio.locations_list.ui.LocationsListFragmentDirections
import com.mendelin.locatio.models.LocationInfoRealmObject
import com.mendelin.locatio.utils.ResourceUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class LocationsAdapter(private val viewModel: LocationDataViewModel) :
    ListAdapter<LocationInfoRealmObject, LocationsAdapter.LocationInfoViewHolder>(
        DiffCallbackLocationsAdapter
    ) {

    private val locationsList: ArrayList<LocationInfoRealmObject> = arrayListOf()
    lateinit var context: Context

    class LocationInfoViewHolder(var binding: ItemLocationListDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(property: LocationInfoRealmObject) {
            binding.location = property
            binding.executePendingBindings()
        }
    }

    companion object DiffCallbackLocationsAdapter : DiffUtil.ItemCallback<LocationInfoRealmObject>() {
        override fun areItemsTheSame(
            oldItem: LocationInfoRealmObject,
            newItem: LocationInfoRealmObject
        ): Boolean {
            return (oldItem.lat == newItem.lat && oldItem.lng == newItem.lng)
        }

        override fun areContentsTheSame(
            oldItem: LocationInfoRealmObject,
            newItem: LocationInfoRealmObject
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationInfoViewHolder {
        context = parent.context

        return LocationInfoViewHolder(
            ItemLocationListDataBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LocationInfoViewHolder, position: Int) {
        val location = locationsList[position]
        holder.bind(location)

        with(holder.binding) {
            with(locationCard) {
                lifecycleOwner?.let {
                    viewModel.getLocation().observe(it) { userLocation ->
                        userLocation?.let {
                            Timber.e("$userLocation")
                            holder.binding.distance = ResourceUtils.getDistanceBetweenLocations(
                                location.lat,
                                location.lng,
                                userLocation
                            )
                        }
                    }
                }

                setOnClickListener {
                    GlobalScope.launch {
                        delay(200L)
                        val action = LocationsListFragmentDirections.actionLocationInfo(location)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    fun setList(list: List<LocationInfoRealmObject>) {
        list.sortedBy { it.address }

        locationsList.apply {
            clear()
            addAll(list)
        }

        submitList(locationsList)
        notifyDataSetChanged()
    }
}