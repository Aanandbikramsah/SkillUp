package com.skillup.models

import java.io.Serializable

data class User(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val profileImage: String = "",
    val enrolledCourses: List<String> = emptyList(),
    val completedLessons: List<String> = emptyList(),
    val quizScores: Map<String, Int> = emptyMap(),
    val joinDate: Long = System.currentTimeMillis(),
    val isLoggedIn: Boolean = false
) : Serializable
