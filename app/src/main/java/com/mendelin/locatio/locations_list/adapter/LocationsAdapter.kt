package com.mendelin.locatio.locations_list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mendelin.locatio.ItemLocationListDataBinding
import com.mendelin.locatio.locations_list.ui.LocationsListFragmentDirections
import com.mendelin.locatio.models.LocationInfoObject


class LocationsAdapter :
    ListAdapter<LocationInfoObject, LocationsAdapter.LocationInfoViewHolder>(
        DiffCallbackBreedsAdapter
    ) {

    private val locationsList: ArrayList<LocationInfoObject> = arrayListOf()
    lateinit var context: Context

    class LocationInfoViewHolder(var binding: ItemLocationListDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(property: LocationInfoObject) {
            binding.location = property
            binding.distance = 1f
            binding.executePendingBindings()
        }
    }

    companion object DiffCallbackBreedsAdapter : DiffUtil.ItemCallback<LocationInfoObject>() {
        override fun areItemsTheSame(
            oldItem: LocationInfoObject,
            newItem: LocationInfoObject
        ): Boolean {
            return (oldItem.lat == newItem.lat && oldItem.lng == newItem.lng)
        }

        override fun areContentsTheSame(
            oldItem: LocationInfoObject,
            newItem: LocationInfoObject
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationInfoViewHolder {
        context = parent.context

        return LocationInfoViewHolder(
            ItemLocationListDataBinding.inflate(LayoutInflater.from(context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LocationInfoViewHolder, position: Int) {
        val breed = locationsList[position]
        holder.bind(breed)

        with(holder.binding.locationCard) {
            setOnClickListener {
                val action = LocationsListFragmentDirections.actionLocationInfo(breed)

                val extras = FragmentNavigatorExtras(
                    holder.binding.imgLocation to "locationImage"
                )

                findNavController().navigate(action, extras)
            }
        }
    }

    fun setList(list: List<LocationInfoObject>) {
        list.sortedBy { it.address }

        locationsList.apply {
            clear()
            addAll(list)
        }

        submitList(locationsList)
        notifyDataSetChanged()
    }
}