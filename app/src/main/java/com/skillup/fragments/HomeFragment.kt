package com.skillup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillup.R
import com.skillup.adapters.CourseAdapter
import com.skillup.databinding.FragmentHomeBinding
import com.skillup.models.Course
import com.skillup.utils.SharedPrefHelper

class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefHelper: SharedPrefHelper
    private lateinit var courseAdapter: CourseAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sharedPrefHelper = SharedPrefHelper.getInstance(requireContext())
        setupRecyclerView()
        loadUserData()
        loadFeaturedCourses()
    }
    
    private fun setupRecyclerView() {
        courseAdapter = CourseAdapter(emptyList()) { course ->
            (activity as? com.skillup.activities.DashboardActivity)?.navigateToCourseDetail(course.id)
        }
        
        binding.recyclerViewFeaturedCourses.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = courseAdapter
        }
    }
    
    private fun loadUserData() {
        val user = sharedPrefHelper.getUser()
        user?.let {
            binding.tvWelcomeMessage.text = "Welcome back, ${it.name}!"
            binding.tvUserEmail.text = it.email
        }
    }
    
    private fun loadFeaturedCourses() {
        val featuredCourses = getFeaturedCourses()
        courseAdapter.updateCourses(featuredCourses)
        
        if (featuredCourses.isEmpty()) {
            binding.tvNoCourses.visibility = View.VISIBLE
            binding.recyclerViewFeaturedCourses.visibility = View.GONE
        } else {
            binding.tvNoCourses.visibility = View.GONE
            binding.recyclerViewFeaturedCourses.visibility = View.VISIBLE
        }
    }
    
    private fun getFeaturedCourses(): List<Course> {
        return listOf(
            Course(
                id = "1",
                title = "Complete Android Development",
                description = "Learn Android development from scratch with Kotlin and modern tools",
                instructor = "John Doe",
                duration = "8 weeks",
                difficulty = "Intermediate",
                rating = 4.5f,
                totalLessons = 25,
                completedLessons = 0,
                category = "Programming",
                price = 99.99,
                thumbnail = "course_android"
            ),
            Course(
                id = "2",
                title = "UI/UX Design Fundamentals",
                description = "Master the art of user interface and experience design",
                instructor = "Jane Smith",
                duration = "6 weeks",
                difficulty = "Beginner",
                rating = 4.8f,
                totalLessons = 18,
                completedLessons = 0,
                category = "Design",
                price = 79.99,
                thumbnail = "course_ui_ux"
            ),
            Course(
                id = "3",
                title = "Data Science with Python",
                description = "Learn data analysis, visualization, and machine learning",
                instructor = "Mike Johnson",
                duration = "10 weeks",
                difficulty = "Advanced",
                rating = 4.7f,
                totalLessons = 32,
                completedLessons = 0,
                category = "Data Science",
                price = 149.99,
                thumbnail = "course_data_science"
            ),
            Course(
                id = "4",
                title = "Web Development Bootcamp",
                description = "Build modern web applications with HTML, CSS, and JavaScript",
                instructor = "Sarah Wilson",
                duration = "12 weeks",
                difficulty = "Beginner",
                rating = 4.6f,
                totalLessons = 40,
                completedLessons = 0,
                category = "Web Development",
                price = 129.99,
                thumbnail = "course_web"
            ),
            Course(
                id = "5",
                title = "Artificial Intelligence & Machine Learning",
                description = "Explore AI concepts and build intelligent applications",
                instructor = "Dr. Alex Chen",
                duration = "14 weeks",
                difficulty = "Advanced",
                rating = 4.9f,
                totalLessons = 45,
                completedLessons = 0,
                category = "AI/ML",
                price = 199.99,
                thumbnail = "course_ai"
            )
        )
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
