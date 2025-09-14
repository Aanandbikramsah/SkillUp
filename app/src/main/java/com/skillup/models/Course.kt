package com.skillup.models

import java.io.Serializable

data class Course(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val instructor: String = "",
    val duration: String = "",
    val difficulty: String = "",
    val rating: Float = 0f,
    val totalLessons: Int = 0,
    val completedLessons: Int = 0,
    val thumbnail: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val isEnrolled: Boolean = false,
    val lessons: List<Lesson> = emptyList()
) : Serializable

data class Lesson(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val videoUrl: String = "",
    val duration: String = "",
    val isCompleted: Boolean = false,
    val order: Int = 0
) : Serializable
