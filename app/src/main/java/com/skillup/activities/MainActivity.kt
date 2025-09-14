package com.skillup.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.skillup.R
import com.skillup.utils.Constants
import com.skillup.utils.SharedPrefHelper

class MainActivity : AppCompatActivity() {
    
    private lateinit var sharedPrefHelper: SharedPrefHelper
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        sharedPrefHelper = SharedPrefHelper.getInstance(this)
        
        // Delay for splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextActivity()
        }, Constants.SPLASH_DELAY)
    }
    
    private fun navigateToNextActivity() {
        val intent = if (sharedPrefHelper.isUserLoggedIn()) {
            Intent(this, DashboardActivity::class.java)
        } else {
            Intent(this, AuthActivity::class.java)
        }
        
        startActivity(intent)
        finish()
    }
}