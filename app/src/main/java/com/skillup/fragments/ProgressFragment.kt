package com.skillup.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.skillup.R
import com.skillup.databinding.FragmentProgressBinding
import com.skillup.models.QuizResult
import com.skillup.utils.SharedPrefHelper

class ProgressFragment : Fragment() {
    
    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPrefHelper: SharedPrefHelper
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sharedPrefHelper = SharedPrefHelper.getInstance(requireContext())
        loadProgressData()
        setupCharts()
    }
    
    private fun loadProgressData() {
        val user = sharedPrefHelper.getUser()
        val courses = sharedPrefHelper.getCourses()
        val quizResults = sharedPrefHelper.getQuizResults()
        
        user?.let {
            binding.tvUserName.text = it.name
            binding.tvUserEmail.text = it.email
        }
        
        // Calculate overall progress
        val totalLessons = courses.sumOf { it.totalLessons }
        val completedLessons = courses.sumOf { it.completedLessons }
        val overallProgress = if (totalLessons > 0) {
            (completedLessons * 100) / totalLessons
        } else 0
        
        binding.progressBarOverall.progress = overallProgress
        binding.tvOverallProgress.text = "$overallProgress% Complete"
        
        // Update statistics
        binding.tvTotalCourses.text = courses.size.toString()
        binding.tvCompletedLessons.text = completedLessons.toString()
        binding.tvTotalLessons.text = totalLessons.toString()
        binding.tvQuizzesTaken.text = quizResults.size.toString()
        
        // Calculate average quiz score
        val averageScore = if (quizResults.isNotEmpty()) {
            quizResults.map { it.score }.average().toInt()
        } else 0
        
        binding.tvAverageScore.text = "$averageScore%"
        
        // Update streak (mock data)
        binding.tvCurrentStreak.text = "7 days"
        binding.tvLongestStreak.text = "15 days"
        
        // Show/hide empty state
        if (courses.isEmpty()) {
            binding.layoutEmptyState.visibility = View.VISIBLE
            binding.layoutProgressContent.visibility = View.GONE
        } else {
            binding.layoutEmptyState.visibility = View.GONE
            binding.layoutProgressContent.visibility = View.VISIBLE
        }
    }
    
    private fun setupCharts() {
        setupProgressChart()
        setupCategoryChart()
    }
    
    private fun setupProgressChart() {
        val courses = sharedPrefHelper.getCourses()
        
        if (courses.isNotEmpty()) {
            val completedLessons = courses.sumOf { it.completedLessons }
            val totalLessons = courses.sumOf { it.totalLessons }
            val remainingLessons = totalLessons - completedLessons
            
            val entries = mutableListOf<PieEntry>()
            if (completedLessons > 0) {
                entries.add(PieEntry(completedLessons.toFloat(), "Completed"))
            }
            if (remainingLessons > 0) {
                entries.add(PieEntry(remainingLessons.toFloat(), "Remaining"))
            }
            
            if (entries.isNotEmpty()) {
                val dataSet = PieDataSet(entries, "")
                dataSet.colors = listOf(
                    Color.parseColor("#4CAF50"),
                    Color.parseColor("#E0E0E0")
                )
                dataSet.valueTextSize = 12f
                dataSet.valueTextColor = Color.WHITE
                
                val data = PieData(dataSet)
                data.setValueFormatter(PercentFormatter(binding.chartProgress))
                
                binding.chartProgress.apply {
                    this.data = data
                    description.isEnabled = false
                    legend.isEnabled = false
                    setHoleColor(Color.TRANSPARENT)
                    setTransparentCircleColor(Color.TRANSPARENT)
                    setTransparentCircleAlpha(0)
                    holeRadius = 50f
                    transparentCircleRadius = 55f
                    setDrawEntryLabels(false)
                    animateY(1000)
                    invalidate()
                }
            }
        }
    }
    
    private fun setupCategoryChart() {
        val courses = sharedPrefHelper.getCourses()
        val categoryMap = courses.groupBy { it.category }
        
        val entries = categoryMap.map { (category, courseList) ->
            PieEntry(courseList.size.toFloat(), category)
        }
        
        if (entries.isNotEmpty()) {
            val dataSet = PieDataSet(entries, "")
            dataSet.colors = listOf(
                Color.parseColor("#FF5722"),
                Color.parseColor("#2196F3"),
                Color.parseColor("#4CAF50"),
                Color.parseColor("#FF9800"),
                Color.parseColor("#9C27B0")
            )
            dataSet.valueTextSize = 10f
            dataSet.valueTextColor = Color.WHITE
            
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter(binding.chartCategories))
            
            binding.chartCategories.apply {
                this.data = data
                description.isEnabled = false
                legend.isEnabled = true
                legend.textSize = 12f
                setHoleColor(Color.TRANSPARENT)
                setTransparentCircleColor(Color.TRANSPARENT)
                setTransparentCircleAlpha(0)
                holeRadius = 40f
                transparentCircleRadius = 45f
                setDrawEntryLabels(false)
                animateY(1000)
                invalidate()
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
