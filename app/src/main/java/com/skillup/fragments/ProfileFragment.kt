package com.skillup.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.skillup.R
import com.skillup.activities.AuthActivity
import com.skillup.databinding.FragmentProfileBinding
import com.skillup.utils.SharedPrefHelper

class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefHelper: SharedPrefHelper
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sharedPrefHelper = SharedPrefHelper.getInstance(requireContext())
        loadUserData()
        setupClickListeners()
    }
    
    private fun loadUserData() {
        val user = sharedPrefHelper.getUser()
        user?.let {
            binding.tvUserName.text = it.name
            binding.tvUserEmail.text = it.email
            binding.tvJoinDate.text = "Joined: ${formatDate(it.joinDate)}"
            
            // Set profile avatar
            binding.ivProfileImage.setImageResource(R.drawable.avatar_default)
            
            // Update profile statistics
            val courses = sharedPrefHelper.getCourses()
            val enrolledCourses = courses.count { course -> 
                user.enrolledCourses.contains(course.id) 
            }
            val completedLessons = courses.sumOf { it.completedLessons }
            val totalLessons = courses.sumOf { it.totalLessons }
            
            binding.tvEnrolledCourses.text = enrolledCourses.toString()
            binding.tvCompletedLessons.text = completedLessons.toString()
            binding.tvTotalLessons.text = totalLessons.toString()
            
            // Calculate completion percentage
            val completionPercentage = if (totalLessons > 0) {
                (completedLessons * 100) / totalLessons
            } else 0
            binding.tvCompletionPercentage.text = "$completionPercentage%"
        }
    }
    
    private fun setupClickListeners() {
        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }
        
        binding.btnSettings.setOnClickListener {
            showSettingsDialog()
        }
        
        binding.btnHelp.setOnClickListener {
            showHelpDialog()
        }
        
        binding.btnAbout.setOnClickListener {
            showAboutDialog()
        }
        
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
        
        binding.btnDeleteAccount.setOnClickListener {
            showDeleteAccountConfirmation()
        }
    }
    
    private fun showEditProfileDialog() {
        val user = sharedPrefHelper.getUser()
        user?.let {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Edit Profile")
            
            val input = android.widget.EditText(requireContext())
            input.setText(it.name)
            input.hint = "Enter your name"
            
            builder.setView(input)
            builder.setPositiveButton("Save") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val updatedUser = it.copy(name = newName)
                    sharedPrefHelper.saveUser(updatedUser)
                    loadUserData()
                    Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()
        }
    }
    
    private fun showSettingsDialog() {
        val options = arrayOf("Notifications", "Privacy", "Language", "Theme")
        
        AlertDialog.Builder(requireContext())
            .setTitle("Settings")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> Toast.makeText(requireContext(), "Notification settings coming soon!", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(requireContext(), "Privacy settings coming soon!", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(requireContext(), "Language settings coming soon!", Toast.LENGTH_SHORT).show()
                    3 -> Toast.makeText(requireContext(), "Theme settings coming soon!", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }
    
    private fun showHelpDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Help & Support")
            .setMessage("Need help? Contact us at:\n\nEmail: support@skillup.com\nPhone: +1 (555) 123-4567\n\nOr visit our FAQ section for common questions.")
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun showAboutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("About SkillUp")
            .setMessage("SkillUp v1.0\n\nA comprehensive learning platform designed to help you acquire new skills and advance your career.\n\n© 2024 SkillUp. All rights reserved.")
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showDeleteAccountConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone and all your progress will be lost.")
            .setPositiveButton("Delete") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun logout() {
        sharedPrefHelper.logout()
        
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        
        activity?.finish()
    }
    
    private fun deleteAccount() {
        sharedPrefHelper.clearAllData()
        
        Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show()
        
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        
        activity?.finish()
    }
    
    private fun formatDate(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
