package com.mendelin.locatio.main

import android.os.Bundle
import androidx.navigation.Navigation
import com.mendelin.locatio.R
import com.mendelin.locatio.base_classes.BaseActivity
import com.mendelin.locatio.di.viewmodels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(R.layout.activity_main) {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        supportActionBar?.let {
            with(it) {
                setDisplayShowTitleEnabled(false)
                setHomeAsUpIndicator(null)
                setHomeButtonEnabled(false)
                setDisplayHomeAsUpEnabled(false)
            }
        }

        btnAddLocation.setOnClickListener {
            Navigation.findNavController(this, R.id.navHostFragment).navigate(R.id.addLocationFragment)
        }
    }
}