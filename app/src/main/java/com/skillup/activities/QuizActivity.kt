package com.skillup.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillup.R
import com.skillup.adapters.QuizAdapter
import com.skillup.databinding.ActivityQuizBinding
import com.skillup.models.Quiz
import com.skillup.models.QuizResult
import com.skillup.utils.Constants
import com.skillup.utils.SharedPrefHelper

class QuizActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityQuizBinding
    private lateinit var sharedPrefHelper: SharedPrefHelper
    private lateinit var quiz: Quiz
    private lateinit var quizAdapter: QuizAdapter
    private var countDownTimer: CountDownTimer? = null
    private var timeRemaining: Long = 0
    private var startTime: Long = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        sharedPrefHelper = SharedPrefHelper.getInstance(this)
        
        setupToolbar()
        getQuizData()
        setupRecyclerView()
        setupClickListeners()
        startQuiz()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        
        binding.toolbar.setNavigationOnClickListener {
            showExitConfirmation()
        }
    }
    
    private fun getQuizData() {
        val quizId = intent.getStringExtra(Constants.EXTRA_QUIZ_ID)
        quiz = getSampleQuiz(quizId ?: "quiz_1")
    }
    
    private fun setupRecyclerView() {
        quizAdapter = QuizAdapter(quiz.questions) { questionIndex, selectedAnswer ->
            quiz.questions[questionIndex].selectedAnswer = selectedAnswer
        }
        
        binding.recyclerViewQuestions.apply {
            layoutManager = LinearLayoutManager(this@QuizActivity)
            adapter = quizAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.btnSubmitQuiz.setOnClickListener {
            submitQuiz()
        }
    }
    
    private fun startQuiz() {
        startTime = System.currentTimeMillis()
        timeRemaining = quiz.timeLimit * 60 * 1000L // Convert minutes to milliseconds
        
        binding.apply {
            tvQuizTitle.text = quiz.title
            tvQuizDescription.text = quiz.description
            tvTimeLimit.text = "Time Limit: ${quiz.timeLimit} minutes"
            tvPassingScore.text = "Passing Score: ${quiz.passingScore}%"
        }
        
        startTimer()
    }
    
    private fun startTimer() {
        countDownTimer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeRemaining = millisUntilFinished
                updateTimerDisplay()
            }
            
            override fun onFinish() {
                timeRemaining = 0
                showTimeUpDialog()
            }
        }.start()
    }
    
    private fun updateTimerDisplay() {
        val minutes = timeRemaining / (1000 * 60)
        val seconds = (timeRemaining % (1000 * 60)) / 1000
        
        binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
        
        // Change color when time is running low
        if (timeRemaining < 5 * 60 * 1000) { // Less than 5 minutes
            binding.tvTimer.setTextColor(getColor(R.color.red))
        } else {
            binding.tvTimer.setTextColor(getColor(R.color.primary))
        }
    }
    
    private fun submitQuiz() {
        val answeredQuestions = quiz.questions.count { it.selectedAnswer != -1 }
        val totalQuestions = quiz.questions.size
        
        if (answeredQuestions < totalQuestions) {
            AlertDialog.Builder(this)
                .setTitle("Incomplete Quiz")
                .setMessage("You have answered $answeredQuestions out of $totalQuestions questions. Do you want to submit anyway?")
                .setPositiveButton("Submit") { _, _ ->
                    calculateAndShowResults()
                }
                .setNegativeButton("Continue", null)
                .show()
        } else {
            calculateAndShowResults()
        }
    }
    
    private fun calculateAndShowResults() {
        countDownTimer?.cancel()
        
        val correctAnswers = quiz.questions.count { question ->
            question.selectedAnswer == question.correctAnswer
        }
        
        val totalQuestions = quiz.questions.size
        val score = (correctAnswers * 100) / totalQuestions
        val timeTaken = (System.currentTimeMillis() - startTime) / 1000
        val isPassed = score >= quiz.passingScore
        
        val result = QuizResult(
            quizId = quiz.id,
            score = score,
            totalQuestions = totalQuestions,
            correctAnswers = correctAnswers,
            timeTaken = timeTaken,
            isPassed = isPassed
        )
        
        sharedPrefHelper.saveQuizResult(result)
        
        showResultsDialog(result)
    }
    
    private fun showResultsDialog(result: QuizResult) {
        val message = buildString {
            append("Quiz Results:\n\n")
            append("Score: ${result.score}%\n")
            append("Correct Answers: ${result.correctAnswers}/${result.totalQuestions}\n")
            append("Time Taken: ${formatTime(result.timeTaken)}\n")
            append("Status: ${if (result.isPassed) "PASSED" else "FAILED"}\n\n")
            
            if (result.isPassed) {
                append("Congratulations! You passed the quiz.")
            } else {
                append("Don't worry! You can retake the quiz to improve your score.")
            }
        }
        
        AlertDialog.Builder(this)
            .setTitle("Quiz Completed")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun showTimeUpDialog() {
        AlertDialog.Builder(this)
            .setTitle("Time's Up!")
            .setMessage("The quiz time has expired. Your answers will be submitted automatically.")
            .setPositiveButton("View Results") { _, _ ->
                calculateAndShowResults()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun showExitConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Exit Quiz")
            .setMessage("Are you sure you want to exit? Your progress will be lost.")
            .setPositiveButton("Exit") { _, _ ->
                finish()
            }
            .setNegativeButton("Continue", null)
            .show()
    }
    
    private fun formatTime(seconds: Long): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
    
    private fun getSampleQuiz(quizId: String): Quiz {
        return Quiz(
            id = quizId,
            courseId = "1",
            title = "Android Development Quiz",
            description = "Test your knowledge of Android development concepts",
            timeLimit = 30,
            passingScore = 60,
            questions = getSampleQuestions()
        )
    }
    
    private fun getSampleQuestions(): List<com.skillup.models.Question> {
        return listOf(
            com.skillup.models.Question(
                id = "1",
                question = "What is the main component of an Android application?",
                options = listOf("Activity", "Service", "BroadcastReceiver", "ContentProvider"),
                correctAnswer = 0,
                explanation = "Activity is the main component that represents a single screen with a user interface."
            ),
            com.skillup.models.Question(
                id = "2",
                question = "Which method is called when an Activity is first created?",
                options = listOf("onStart()", "onResume()", "onCreate()", "onPause()"),
                correctAnswer = 2,
                explanation = "onCreate() is called when the activity is first created and is where you should perform basic setup."
            ),
            com.skillup.models.Question(
                id = "3",
                question = "What is the purpose of the AndroidManifest.xml file?",
                options = listOf(
                    "To define app permissions",
                    "To declare app components",
                    "To specify app configuration",
                    "All of the above"
                ),
                correctAnswer = 3,
                explanation = "AndroidManifest.xml serves all these purposes and more."
            ),
            com.skillup.models.Question(
                id = "4",
                question = "Which layout is used for arranging views in a single row or column?",
                options = listOf("RelativeLayout", "LinearLayout", "FrameLayout", "ConstraintLayout"),
                correctAnswer = 1,
                explanation = "LinearLayout arranges its children in a single row or column."
            ),
            com.skillup.models.Question(
                id = "5",
                question = "What is SharedPreferences used for?",
                options = listOf(
                    "Storing large amounts of data",
                    "Storing key-value pairs",
                    "Storing images",
                    "Storing videos"
                ),
                correctAnswer = 1,
                explanation = "SharedPreferences is used for storing simple key-value pairs persistently."
            )
        )
    }
    
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
