package com.mendelin.locatio.base_classes

import android.os.Bundle
import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity(private val layoutId: Int) : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                onBackPressed()
        }
        return true
    }
}