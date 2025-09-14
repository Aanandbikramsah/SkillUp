package com.skillup.utils

object Constants {
    
    // App Configuration
    const val APP_NAME = "SkillUp"
    const val SPLASH_DELAY = 2000L // 2 seconds
    
    // SharedPreferences Keys
    const val PREFS_NAME = "skillup_prefs"
    const val KEY_USER = "user"
    const val KEY_COURSES = "courses"
    const val KEY_QUIZ_RESULTS = "quiz_results"
    
    // Intent Extras
    const val EXTRA_COURSE_ID = "course_id"
    const val EXTRA_QUIZ_ID = "quiz_id"
    const val EXTRA_LESSON_ID = "lesson_id"
    const val EXTRA_VIDEO_URL = "video_url"
    const val EXTRA_LESSON_TITLE = "lesson_title"
    
    // Fragment Tags
    const val FRAGMENT_HOME = "home"
    const val FRAGMENT_COURSES = "courses"
    const val FRAGMENT_PROGRESS = "progress"
    const val FRAGMENT_PROFILE = "profile"
    
    // Animation Durations
    const val ANIMATION_DURATION_SHORT = 300L
    const val ANIMATION_DURATION_MEDIUM = 500L
    const val ANIMATION_DURATION_LONG = 800L
    
    // Quiz Configuration
    const val DEFAULT_QUIZ_TIME_LIMIT = 30 // minutes
    const val DEFAULT_PASSING_SCORE = 60 // percentage
    const val MAX_QUIZ_ATTEMPTS = 3
    
    // Course Categories
    const val CATEGORY_PROGRAMMING = "Programming"
    const val CATEGORY_DESIGN = "Design"
    const val CATEGORY_BUSINESS = "Business"
    const val CATEGORY_MARKETING = "Marketing"
    const val CATEGORY_DATA_SCIENCE = "Data Science"
    
    // Difficulty Levels
    const val DIFFICULTY_BEGINNER = "Beginner"
    const val DIFFICULTY_INTERMEDIATE = "Intermediate"
    const val DIFFICULTY_ADVANCED = "Advanced"
    
    // API Endpoints (for future use)
    const val BASE_URL = "https://api.skillup.com/"
    const val ENDPOINT_COURSES = "courses"
    const val ENDPOINT_USERS = "users"
    const val ENDPOINT_QUIZZES = "quizzes"
    
    // Error Messages
    const val ERROR_NETWORK = "Network error. Please check your connection."
    const val ERROR_GENERIC = "Something went wrong. Please try again."
    const val ERROR_INVALID_CREDENTIALS = "Invalid email or password."
    const val ERROR_EMAIL_EXISTS = "Email already exists."
    const val ERROR_WEAK_PASSWORD = "Password should be at least 6 characters."
    
    // Success Messages
    const val SUCCESS_LOGIN = "Login successful!"
    const val SUCCESS_REGISTER = "Registration successful!"
    const val SUCCESS_COURSE_ENROLLED = "Successfully enrolled in course!"
    const val SUCCESS_QUIZ_COMPLETED = "Quiz completed successfully!"
    
    // Validation
    const val MIN_PASSWORD_LENGTH = 6
    const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    
    // File Extensions
    const val VIDEO_EXTENSIONS = "mp4,avi,mov,wmv,flv,webm"
    const val IMAGE_EXTENSIONS = "jpg,jpeg,png,gif,webp"
    
    // Progress Tracking
    const val PROGRESS_UPDATE_INTERVAL = 1000L // 1 second
    const val MIN_PROGRESS_TO_SAVE = 5 // percentage
}
