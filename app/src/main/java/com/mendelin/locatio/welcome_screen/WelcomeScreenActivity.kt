package com.mendelin.locatio.welcome_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.mendelin.locatio.R
import com.mendelin.locatio.base_classes.BaseActivity
import com.mendelin.locatio.main.MainActivity


class WelcomeScreenActivity : BaseActivity(R.layout.activity_welcome_screen) {
    companion object {
        private const val SPLASH_TIME_OUT = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            loadMainScreen()
        }, SPLASH_TIME_OUT)
    }

    private fun loadMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }
}