package com.mendelin.locatio.ui.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mendelin.locatio.EditLocationDataBinding
import com.mendelin.locatio.R
import com.mendelin.locatio.ui.fragments.EditLocationFragmentArgs
import com.mendelin.locatio.repository.RealmRepository
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_edit_location.*
import javax.inject.Inject

class EditLocationFragment : DaggerFragment(R.layout.fragment_edit_location) {

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

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val obj = repository.findLocationObject(args.location?.id ?: 0)

        editImage.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                btnSaveLocation.performClick()
            }
            false
        }

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