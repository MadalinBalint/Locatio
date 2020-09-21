package com.mendelin.locatio.location_info.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mendelin.locatio.LocationInfoDataBinding
import com.mendelin.locatio.R
import com.mendelin.locatio.base_classes.BaseFragment
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
        binding.location = args.location
        binding.coordinates = "${args.location?.lat}, ${args.location?.lng}"

        binding.txtCoordinates.movementMethod = LinkMovementMethod.getInstance()

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
            }
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}