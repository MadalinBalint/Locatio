package com.mendelin.locatio.edit_location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mendelin.locatio.EditLocationDataBinding
import com.mendelin.locatio.R
import com.mendelin.locatio.base_classes.BaseFragment
import com.mendelin.locatio.repository.RealmRepository
import kotlinx.android.synthetic.main.fragment_edit_location.*
import javax.inject.Inject

class EditLocationFragment : BaseFragment(R.layout.fragment_add_location) {

    private val args: EditLocationFragmentArgs by navArgs()

    lateinit var binding: EditLocationDataBinding

    @Inject
    lateinit var repository: RealmRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditLocationDataBinding.inflate(inflater, container, false)

        args.location?.let {
            binding.location = it
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        toolbarOff()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val obj = repository.findLocationObject(args.location?.id ?: 0)

        btnSaveLocation.setOnClickListener {
            val latNew = editLatitude.text.toString().trim().toDouble()
            val lngNew = editLongitude.text.toString().trim().toDouble()
            val labelNew = editLabel.text.toString().trim()
            val addressNew = editAddress.text.toString().trim()
            val imageNew = editImage.text.toString().trim()

            obj?.let {
                repository.updateLocationObject(it, latNew, lngNew, labelNew, addressNew, imageNew)
            }

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