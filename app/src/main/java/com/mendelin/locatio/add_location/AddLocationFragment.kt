package com.mendelin.locatio.add_location

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.mendelin.locatio.R
import com.mendelin.locatio.base_classes.BaseFragment
import com.mendelin.locatio.repository.RealmRepository
import kotlinx.android.synthetic.main.fragment_add_location.*
import javax.inject.Inject

class AddLocationFragment : BaseFragment(R.layout.fragment_add_location) {

    @Inject
    lateinit var repository: RealmRepository

    override fun onResume() {
        super.onResume()

        toolbarOff()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val obj = repository.findLocationObject(args.location?.id ?: 0)

        btnAddLocation.setOnClickListener {
            val latNew = editLatitude.text.toString().trim().toDouble()
            val lngNew = editLongitude.text.toString().trim().toDouble()
            val labelNew = editLabel.text.toString().trim()
            val addressNew = editAddress.text.toString().trim()
            val imageNew = editImage.text.toString().trim()

            /*obj?.let {
                repository.updateLocationObject(it, latNew, lngNew, labelNew, addressNew, imageNew)
            }*/

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