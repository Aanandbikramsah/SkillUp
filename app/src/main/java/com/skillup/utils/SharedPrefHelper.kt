package com.skillup.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.skillup.models.User
import com.skillup.models.Course
import com.skillup.models.QuizResult

class SharedPrefHelper private constructor(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val PREFS_NAME = "skillup_prefs"
        private const val KEY_USER = "user"
        private const val KEY_COURSES = "courses"
        private const val KEY_QUIZ_RESULTS = "quiz_results"
        private const val KEY_IS_FIRST_LAUNCH = "is_first_launch"
        private const val KEY_LAST_LOGIN = "last_login"
        
        @Volatile
        private var INSTANCE: SharedPrefHelper? = null
        
        fun getInstance(context: Context): SharedPrefHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPrefHelper(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    // User Management
    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        prefs.edit().putString(KEY_USER, userJson).apply()
    }
    
    fun getUser(): User? {
        val userJson = prefs.getString(KEY_USER, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else null
    }
    
    fun isUserLoggedIn(): Boolean {
        return getUser()?.isLoggedIn ?: false
    }
    
    fun logout() {
        val user = getUser()
        user?.let {
            saveUser(it.copy(isLoggedIn = false))
        }
    }
    
    // Course Management
    fun saveCourses(courses: List<Course>) {
        val coursesJson = gson.toJson(courses)
        prefs.edit().putString(KEY_COURSES, coursesJson).apply()
    }
    
    fun getCourses(): List<Course> {
        val coursesJson = prefs.getString(KEY_COURSES, null)
        return if (coursesJson != null) {
            val type = object : TypeToken<List<Course>>() {}.type
            gson.fromJson(coursesJson, type)
        } else emptyList()
    }
    
    fun updateCourse(course: Course) {
        val courses = getCourses().toMutableList()
        val index = courses.indexOfFirst { it.id == course.id }
        if (index != -1) {
            courses[index] = course
            saveCourses(courses)
        }
    }
    
    fun enrollInCourse(courseId: String) {
        val user = getUser()
        user?.let {
            val enrolledCourses = it.enrolledCourses.toMutableList()
            if (!enrolledCourses.contains(courseId)) {
                enrolledCourses.add(courseId)
                saveUser(it.copy(enrolledCourses = enrolledCourses))
            }
        }
    }
    
    // Quiz Results
    fun saveQuizResult(result: QuizResult) {
        val results = getQuizResults().toMutableList()
        results.add(result)
        saveQuizResults(results)
    }
    
    fun getQuizResults(): List<QuizResult> {
        val resultsJson = prefs.getString(KEY_QUIZ_RESULTS, null)
        return if (resultsJson != null) {
            val type = object : TypeToken<List<QuizResult>>() {}.type
            gson.fromJson(resultsJson, type)
        } else emptyList()
    }
    
    private fun saveQuizResults(results: List<QuizResult>) {
        val resultsJson = gson.toJson(results)
        prefs.edit().putString(KEY_QUIZ_RESULTS, resultsJson).apply()
    }
    
    // App Settings
    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(KEY_IS_FIRST_LAUNCH, true)
    }
    
    fun setFirstLaunchCompleted() {
        prefs.edit().putBoolean(KEY_IS_FIRST_LAUNCH, false).apply()
    }
    
    fun setLastLoginTime() {
        prefs.edit().putLong(KEY_LAST_LOGIN, System.currentTimeMillis()).apply()
    }
    
    fun getLastLoginTime(): Long {
        return prefs.getLong(KEY_LAST_LOGIN, 0)
    }
    
    // Clear all data
    fun clearAllData() {
        prefs.edit().clear().apply()
    }
}
