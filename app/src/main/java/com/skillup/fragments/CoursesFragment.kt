package com.skillup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.skillup.R
import com.skillup.adapters.CourseAdapter
import com.skillup.databinding.FragmentCoursesBinding
import com.skillup.models.Course
import com.skillup.utils.SharedPrefHelper

class CoursesFragment : Fragment() {
    
    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefHelper: SharedPrefHelper
    private lateinit var courseAdapter: CourseAdapter
    private var allCourses: List<Course> = emptyList()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sharedPrefHelper = SharedPrefHelper.getInstance(requireContext())
        setupRecyclerView()
        setupClickListeners()
        loadCourses()
    }
    
    private fun setupRecyclerView() {
        courseAdapter = CourseAdapter(emptyList()) { course ->
            (activity as? com.skillup.activities.DashboardActivity)?.navigateToCourseDetail(course.id)
        }
        
        binding.recyclerViewCourses.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = courseAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.btnAllCourses.setOnClickListener {
            filterCourses("All")
        }
        
        binding.btnProgramming.setOnClickListener {
            filterCourses("Programming")
        }
        
        binding.btnDesign.setOnClickListener {
            filterCourses("Design")
        }
        
        binding.btnBusiness.setOnClickListener {
            filterCourses("Business")
        }
        
        binding.btnDataScience.setOnClickListener {
            filterCourses("Data Science")
        }
        
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchCourses(query ?: "")
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                searchCourses(newText ?: "")
                return true
            }
        })
    }
    
    private fun loadCourses() {
        allCourses = getAllCourses()
        courseAdapter.updateCourses(allCourses)
        updateCourseCount()
        updateEmptyState()
    }
    
    private fun filterCourses(category: String) {
        val filteredCourses = if (category == "All") {
            allCourses
        } else {
            allCourses.filter { it.category == category }
        }
        
        courseAdapter.updateCourses(filteredCourses)
        updateFilterButtons(category)
        updateCourseCount(filteredCourses.size)
        updateEmptyState(filteredCourses.isEmpty())
    }
    
    private fun searchCourses(query: String) {
        val filteredCourses = if (query.isEmpty()) {
            allCourses
        } else {
            allCourses.filter { 
                it.title.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.instructor.contains(query, ignoreCase = true)
            }
        }
        
        courseAdapter.updateCourses(filteredCourses)
        updateCourseCount(filteredCourses.size)
        updateEmptyState(filteredCourses.isEmpty())
    }
    
    private fun updateFilterButtons(selectedCategory: String) {
        binding.btnAllCourses.isSelected = selectedCategory == "All"
        binding.btnProgramming.isSelected = selectedCategory == "Programming"
        binding.btnDesign.isSelected = selectedCategory == "Design"
        binding.btnBusiness.isSelected = selectedCategory == "Business"
        binding.btnDataScience.isSelected = selectedCategory == "Data Science"
    }
    
    private fun updateCourseCount(count: Int = allCourses.size) {
        binding.tvCourseCount.text = "$count courses available"
    }
    
    private fun updateEmptyState(isEmpty: Boolean = false) {
        if (isEmpty) {
            binding.recyclerViewCourses.visibility = View.GONE
            binding.layoutEmptyState.visibility = View.VISIBLE
        } else {
            binding.recyclerViewCourses.visibility = View.VISIBLE
            binding.layoutEmptyState.visibility = View.GONE
        }
    }
    
    private fun getAllCourses(): List<Course> {
        return listOf(
            Course(
                id = "1",
                title = "Complete Android Development",
                description = "Learn Android development from scratch with this comprehensive course",
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
                description = "Master the art of user interface and user experience design",
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
                category = "Programming",
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
                category = "Data Science",
                price = 199.99,
                thumbnail = "course_ai"
            ),
            Course(
                id = "6",
                title = "Digital Marketing Mastery",
                description = "Comprehensive guide to digital marketing strategies",
                instructor = "Lisa Chen",
                duration = "5 weeks",
                difficulty = "Beginner",
                rating = 4.3f,
                totalLessons = 15,
                completedLessons = 0,
                category = "Business",
                price = 69.99,
                thumbnail = "course_ui_ux"
            ),
            Course(
                id = "7",
                title = "iOS App Development",
                description = "Build iOS applications using Swift and Xcode",
                instructor = "David Brown",
                duration = "9 weeks",
                difficulty = "Intermediate",
                rating = 4.6f,
                totalLessons = 28,
                completedLessons = 0,
                category = "Programming",
                price = 119.99,
                thumbnail = "course_android"
            ),
            Course(
                id = "8",
                title = "Web Design with Figma",
                description = "Create stunning web designs using Figma",
                instructor = "Robert Taylor",
                duration = "4 weeks",
                difficulty = "Beginner",
                rating = 4.4f,
                totalLessons = 12,
                completedLessons = 0,
                category = "Design",
                price = 59.99,
                thumbnail = "course_ui_ux"
            ),
            Course(
                id = "9",
                title = "Business Analytics",
                description = "Learn to analyze business data and make informed decisions",
                instructor = "Dr. Emily Davis",
                duration = "7 weeks",
                difficulty = "Intermediate",
                rating = 4.5f,
                totalLessons = 20,
                completedLessons = 0,
                category = "Business",
                price = 89.99,
                thumbnail = "course_data_science"
            ),
            Course(
                id = "10",
                title = "Machine Learning Basics",
                description = "Introduction to machine learning algorithms and applications",
                instructor = "Dr. Emily Davis",
                duration = "12 weeks",
                difficulty = "Advanced",
                rating = 4.9f,
                totalLessons = 35,
                completedLessons = 0,
                category = "Data Science",
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
