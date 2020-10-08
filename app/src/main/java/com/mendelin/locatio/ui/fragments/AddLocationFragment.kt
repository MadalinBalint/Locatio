package com.mendelin.locatio.ui.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.mendelin.locatio.R
import com.mendelin.locatio.repository.RealmRepository
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add_location.*
import javax.inject.Inject

class AddLocationFragment : DaggerFragment(R.layout.fragment_add_location) {

    @Inject
    lateinit var repository: RealmRepository

    override fun onResume() {
        super.onResume()

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editImage.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                btnAddLocation.performClick()
            }
            false
        }

        btnAddLocation.setOnClickListener {
            val lat = editLatitude.text.toString().trim().toDoubleOrNull()
            val lng = editLongitude.text.toString().trim().toDoubleOrNull()
            val label = editLabel.text.toString().trim()
            val address = editAddress.text.toString().trim()
            val image = editImage.text.toString().trim()

            if (lat == null) {
                Toast.makeText(requireContext(), "Input a numerical value for the latitude", Toast.LENGTH_SHORT).show()
                editLatitude.requestFocus()
                return@setOnClickListener
            }

            if (lng == null) {
                Toast.makeText(requireContext(), "Input a numerical value for the longitude", Toast.LENGTH_SHORT).show()
                editLongitude.requestFocus()
                return@setOnClickListener
            }

            if (label.isEmpty()) {
                Toast.makeText(requireContext(), "Input a value for the location label", Toast.LENGTH_SHORT).show()
                editLabel.requestFocus()
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                Toast.makeText(requireContext(), "Input a value for the location address", Toast.LENGTH_SHORT).show()
                editAddress.requestFocus()
                return@setOnClickListener
            }

            if (image.isEmpty()) {
                Toast.makeText(requireContext(), "Input a value for the location image URL", Toast.LENGTH_SHORT).show()
                editImage.requestFocus()
                return@setOnClickListener
            }

            repository.createLocationObject(lat, lng, label, address, image)

            findNavController().navigateUp()
        }

        btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}