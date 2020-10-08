package com.mendelin.locatio.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.Navigation
import com.mendelin.locatio.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity(R.layout.activity_main) {

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                onBackPressed()
        }
        return true
    }
}