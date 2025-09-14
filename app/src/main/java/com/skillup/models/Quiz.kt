package com.skillup.models

import java.io.Serializable

data class Quiz(
    val id: String = "",
    val courseId: String = "",
    val title: String = "",
    val description: String = "",
    val questions: List<Question> = emptyList(),
    val timeLimit: Int = 0, // in minutes
    val passingScore: Int = 60,
    val isCompleted: Boolean = false,
    val score: Int = 0,
    val attempts: Int = 0
) : Serializable

data class Question(
    val id: String = "",
    val question: String = "",
    val options: List<String> = emptyList(),
    val correctAnswer: Int = 0,
    val explanation: String = "",
    var selectedAnswer: Int = -1
) : Serializable

data class QuizResult(
    val quizId: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val correctAnswers: Int = 0,
    val timeTaken: Long = 0,
    val completedAt: Long = System.currentTimeMillis(),
    val isPassed: Boolean = false
) : Serializable
