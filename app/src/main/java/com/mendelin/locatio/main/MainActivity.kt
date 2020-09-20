package com.mendelin.locatio.main

import android.os.Bundle
import com.mendelin.locatio.R
import com.mendelin.locatio.base_classes.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main) {
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
    }
}