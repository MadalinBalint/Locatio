package com.mendelin.locatio.ui.fragments

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mendelin.locatio.LocationInfoDataBinding
import com.mendelin.locatio.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_location_info.*
import java.io.IOException
import java.util.*


class LocationInfoFragment : DaggerFragment(R.layout.fragment_location_info) {

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
            binding.fullAddress = getAddressLong(requireContext(), it.lat, it.lng)
            binding.coordinates = "${it.lat}, ${it.lng}"
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.visibility = View.GONE
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
            findNavController().popBackStack()
        }
    }

    private fun getAddressLong(context: Context, lat: Double, lng: Double): String {
        val array = arrayListOf<String>()
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (addresses != null && addresses.size > 0) {
                val obj = addresses[0]
                if (obj != null) {
                    /* Address */
                    if (!obj.getAddressLine(0).isNullOrEmpty())
                        array.add(obj.getAddressLine(0))

                    /* Country */
                    /*if (!obj.countryName.isNullOrEmpty())
                        array.add(obj.countryName)*/

                    /* Country code */
                    if (!obj.countryCode.isNullOrEmpty())
                        array.add(obj.countryCode)

                    /* Admin Area */
                    /*if (!obj.adminArea.isNullOrEmpty())
                        array.add("Adamin "+obj.adminArea)*/

                    /* Postal Code */
                    /*if (!obj.postalCode.isNullOrEmpty())
                        array.add(obj.postalCode)*/

                    /* Sub Admin Area */
                    if (!obj.subAdminArea.isNullOrEmpty())
                        array.add(obj.subAdminArea)

                    /* Locality */
                    /*if (!obj.locality.isNullOrEmpty())
                        array.add(obj.locality)*/

                    /* Sub Locality */
                    /*if (!obj.subLocality.isNullOrEmpty())
                        array.add(obj.subLocality)*/

                    /* Sub Thoroughfare */
                    /*if (!obj.subThoroughfare.isNullOrEmpty())
                        array.add(obj.subThoroughfare)*/

                    return array.joinToString(separator = ", ")
                }
            } else {
                return ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
}