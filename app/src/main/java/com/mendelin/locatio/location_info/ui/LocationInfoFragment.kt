package com.mendelin.locatio.location_info.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mendelin.locatio.LocationInfoDataBinding
import com.mendelin.locatio.R
import com.mendelin.locatio.base_classes.BaseFragment
import com.mendelin.locatio.utils.ResourceUtils
import kotlinx.android.synthetic.main.fragment_location_info.*


class LocationInfoFragment : BaseFragment(R.layout.fragment_location_info) {

    private val args: LocationInfoFragmentArgs by navArgs()

    lateinit var binding: LocationInfoDataBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LocationInfoDataBinding.inflate(inflater, container, false)

        args.location?.let {
            binding.location = it
            binding.fullAddress = ResourceUtils.getAddressLong(requireContext(), it.lat, it.lng)
            binding.coordinates = "${it.lat}, ${it.lng}"
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        toolbarOff()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtCoordinates.setOnClickListener {
            val label = args.location?.label
            label?.replace(" ", "+")

            val gmmIntentUri =
                Uri.parse("geo:0,0?z=15&q=${args.location?.lat},${args.location?.lng}($label)")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

            if (activity?.packageManager?.let { manager -> mapIntent.resolveActivity(manager) } != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(
                    context,
                    "Please install Google maps/Here WeGo or something similar to view the location on a map.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnEditLocation.setOnClickListener {
            val action = LocationInfoFragmentDirections.actionEditLocation(args.location)
            findNavController().navigate(action)
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}