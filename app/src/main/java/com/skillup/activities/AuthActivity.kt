package com.skillup.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.skillup.R
import com.skillup.databinding.ActivityAuthBinding
import com.skillup.models.User
import com.skillup.utils.Constants
import com.skillup.utils.SharedPrefHelper

class AuthActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAuthBinding
    private lateinit var sharedPrefHelper: SharedPrefHelper
    private var isLoginMode = true
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sharedPrefHelper = SharedPrefHelper.getInstance(this)
        
        setupUI()
        setupClickListeners()
    }
    
    private fun setupUI() {
        updateUIForMode()
    }
    
    private fun setupClickListeners() {
        binding.btnToggleMode.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUIForMode()
        }
        
        binding.btnAuth.setOnClickListener {
            if (isLoginMode) {
                performLogin()
            } else {
                performRegistration()
            }
        }
        
        binding.tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
    }
    
    private fun updateUIForMode() {
        if (isLoginMode) {
            binding.tvTitle.text = getString(R.string.welcome_back)
            binding.tvSubtitle.text = getString(R.string.login_subtitle)
            binding.btnAuth.text = getString(R.string.login)
            binding.btnToggleMode.text = getString(R.string.dont_have_account)
            binding.tvForgotPassword.visibility = View.VISIBLE
            binding.etConfirmPassword.visibility = View.GONE
        } else {
            binding.tvTitle.text = getString(R.string.create_account)
            binding.tvSubtitle.text = getString(R.string.register_subtitle)
            binding.btnAuth.text = getString(R.string.register)
            binding.btnToggleMode.text = getString(R.string.already_have_account)
            binding.tvForgotPassword.visibility = View.GONE
            binding.etConfirmPassword.visibility = View.VISIBLE
        }
    }
    
    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        
        if (validateLoginInput(email, password)) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnAuth.isEnabled = false
            
            // Simulate login process
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                val user = User(
                    id = "user_${System.currentTimeMillis()}",
                    email = email,
                    name = email.substringBefore("@"),
                    isLoggedIn = true
                )
                
                sharedPrefHelper.saveUser(user)
                sharedPrefHelper.setLastLoginTime()
                
                binding.progressBar.visibility = View.GONE
                binding.btnAuth.isEnabled = true
                
                Toast.makeText(this, Constants.SUCCESS_LOGIN, Toast.LENGTH_SHORT).show()
                navigateToDashboard()
            }, 1500)
        }
    }
    
    private fun performRegistration() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPasswordInput.text.toString().trim()
        
        if (validateRegistrationInput(email, password, confirmPassword)) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnAuth.isEnabled = false
            
            // Simulate registration process
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                val user = User(
                    id = "user_${System.currentTimeMillis()}",
                    email = email,
                    name = email.substringBefore("@"),
                    isLoggedIn = true
                )
                
                sharedPrefHelper.saveUser(user)
                sharedPrefHelper.setLastLoginTime()
                
                binding.progressBar.visibility = View.GONE
                binding.btnAuth.isEnabled = true
                
                Toast.makeText(this, Constants.SUCCESS_REGISTER, Toast.LENGTH_SHORT).show()
                navigateToDashboard()
            }, 1500)
        }
    }
    
    private fun validateLoginInput(email: String, password: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            binding.etEmail.error = getString(R.string.email_required)
            return false
        }
        
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = getString(R.string.invalid_email)
            return false
        }
        
        if (TextUtils.isEmpty(password)) {
            binding.etPassword.error = getString(R.string.password_required)
            return false
        }
        
        if (password.length < Constants.MIN_PASSWORD_LENGTH) {
            binding.etPassword.error = getString(R.string.password_too_short)
            return false
        }
        
        return true
    }
    
    private fun validateRegistrationInput(email: String, password: String, confirmPassword: String): Boolean {
        if (!validateLoginInput(email, password)) {
            return false
        }
        
        if (TextUtils.isEmpty(confirmPassword)) {
            binding.etConfirmPasswordInput.error = getString(R.string.confirm_password_required)
            return false
        }
        
        if (password != confirmPassword) {
            binding.etConfirmPasswordInput.error = getString(R.string.passwords_dont_match)
            return false
        }
        
        return true
    }
    
    private fun showForgotPasswordDialog() {
        // Simple implementation - in real app, you'd send reset email
        Toast.makeText(this, "Password reset feature coming soon!", Toast.LENGTH_SHORT).show()
    }
    
    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
