package com.skillup.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillup.R
import com.skillup.adapters.LessonAdapter
import com.skillup.databinding.ActivityCourseDetailBinding
import com.skillup.models.Course
import com.skillup.models.Lesson
import com.skillup.utils.Constants
import com.skillup.utils.SharedPrefHelper

class CourseDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCourseDetailBinding
    private lateinit var sharedPrefHelper: SharedPrefHelper
    private lateinit var course: Course
    private lateinit var lessonAdapter: LessonAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sharedPrefHelper = SharedPrefHelper.getInstance(this)
        
        setupToolbar()
        getCourseData()
        setupRecyclerView()
        setupClickListeners()
        populateCourseData()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    
    private fun getCourseData() {
        val courseId = intent.getStringExtra(Constants.EXTRA_COURSE_ID)
        course = getSampleCourse(courseId ?: "1")
    }
    
    private fun setupRecyclerView() {
        lessonAdapter = LessonAdapter(course.lessons) { lesson ->
            navigateToVideoPlayer(lesson)
        }
        
        binding.recyclerViewLessons.apply {
            layoutManager = LinearLayoutManager(this@CourseDetailActivity)
            adapter = lessonAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.btnEnroll.setOnClickListener {
            enrollInCourse()
        }
        
        binding.btnStartQuiz.setOnClickListener {
            startQuiz()
        }
    }
    
    private fun populateCourseData() {
        binding.apply {
            tvCourseTitle.text = course.title
            tvCourseDescription.text = course.description
            tvInstructor.text = "Instructor: ${course.instructor}"
            tvDuration.text = "Duration: ${course.duration}"
            tvDifficulty.text = "Difficulty: ${course.difficulty}"
            tvRating.text = "Rating: ${course.rating}/5.0"
            tvTotalLessons.text = "${course.totalLessons} Lessons"
            
            // Update progress
            val progressPercentage = if (course.totalLessons > 0) {
                (course.completedLessons * 100) / course.totalLessons
            } else 0
            
            progressBar.progress = progressPercentage
            tvProgress.text = "$progressPercentage% Complete"
            
            // Update button state
            if (course.isEnrolled) {
                btnEnroll.text = "Enrolled"
                btnEnroll.isEnabled = false
                btnStartQuiz.visibility = View.VISIBLE
            } else {
                btnEnroll.text = "Enroll Now"
                btnEnroll.isEnabled = true
                btnStartQuiz.visibility = View.GONE
            }
        }
    }
    
    private fun enrollInCourse() {
        val updatedCourse = course.copy(isEnrolled = true)
        sharedPrefHelper.updateCourse(updatedCourse)
        sharedPrefHelper.enrollInCourse(course.id)
        
        binding.btnEnroll.text = "Enrolled"
        binding.btnEnroll.isEnabled = false
        binding.btnStartQuiz.visibility = View.VISIBLE
        
        // Show success message
        android.widget.Toast.makeText(this, Constants.SUCCESS_COURSE_ENROLLED, android.widget.Toast.LENGTH_SHORT).show()
    }
    
    private fun startQuiz() {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra(Constants.EXTRA_QUIZ_ID, "quiz_${course.id}")
        startActivity(intent)
    }
    
    private fun navigateToVideoPlayer(lesson: Lesson) {
        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra(Constants.EXTRA_VIDEO_URL, lesson.videoUrl)
        intent.putExtra(Constants.EXTRA_LESSON_TITLE, lesson.title)
        startActivity(intent)
    }
    
    private fun getSampleCourse(courseId: String): Course {
        return Course(
            id = courseId,
            title = "Complete Android Development",
            description = "Learn Android development from scratch with this comprehensive course covering all essential topics.",
            instructor = "John Doe",
            duration = "8 weeks",
            difficulty = "Intermediate",
            rating = 4.5f,
            totalLessons = 5,
            completedLessons = 2,
            category = "Programming",
            price = 99.99,
            isEnrolled = courseId == "1",
            lessons = getSampleLessons()
        )
    }
    
    private fun getSampleLessons(): List<Lesson> {
        return listOf(
            Lesson(
                id = "1",
                title = "Introduction to Android",
                description = "Learn the basics of Android development",
                videoUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_1mb.mp4",
                duration = "15:30",
                isCompleted = true,
                order = 1
            ),
            Lesson(
                id = "2",
                title = "Setting up Development Environment",
                description = "Configure Android Studio and SDK",
                videoUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_2mb.mp4",
                duration = "22:15",
                isCompleted = true,
                order = 2
            ),
            Lesson(
                id = "3",
                title = "Creating Your First App",
                description = "Build a simple Hello World application",
                videoUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_5mb.mp4",
                duration = "18:45",
                isCompleted = false,
                order = 3
            ),
            Lesson(
                id = "4",
                title = "User Interface Design",
                description = "Design beautiful and responsive UIs",
                videoUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_10mb.mp4",
                duration = "25:20",
                isCompleted = false,
                order = 4
            ),
            Lesson(
                id = "5",
                title = "Data Storage and Management",
                description = "Learn about databases and data persistence",
                videoUrl = "https://sample-videos.com/zip/10/mp4/SampleVideo_1280x720_30mb.mp4",
                duration = "32:10",
                isCompleted = false,
                order = 5
            )
        )
    }
}
