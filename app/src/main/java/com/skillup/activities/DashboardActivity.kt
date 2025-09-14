package com.skillup.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skillup.R
import com.skillup.databinding.ActivityDashboardBinding
import com.skillup.fragments.HomeFragment
import com.skillup.fragments.CoursesFragment
import com.skillup.fragments.ProgressFragment
import com.skillup.fragments.ProfileFragment
import com.skillup.utils.Constants

class DashboardActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    
    private lateinit var binding: ActivityDashboardBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupBottomNavigation()
        loadFragment(HomeFragment())
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        binding.bottomNavigation.selectedItemId = R.id.nav_home
    }
    
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: Fragment = when (item.itemId) {
            R.id.nav_home -> HomeFragment()
            R.id.nav_courses -> CoursesFragment()
            R.id.nav_progress -> ProgressFragment()
            R.id.nav_profile -> ProfileFragment()
            else -> HomeFragment()
        }
        
        loadFragment(fragment)
        return true
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    fun navigateToCourseDetail(courseId: String) {
        val intent = Intent(this, CourseDetailActivity::class.java)
        intent.putExtra(Constants.EXTRA_COURSE_ID, courseId)
        startActivity(intent)
    }
    
    fun navigateToQuiz(quizId: String) {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra(Constants.EXTRA_QUIZ_ID, quizId)
        startActivity(intent)
    }
    
    fun navigateToVideoPlayer(videoUrl: String, lessonTitle: String) {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra(Constants.EXTRA_VIDEO_URL, videoUrl)
        intent.putExtra(Constants.EXTRA_LESSON_TITLE, lessonTitle)
        startActivity(intent)
    }
}
