package com.skillup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillup.R
import com.skillup.models.Course

class CourseAdapter(
    private var courses: List<Course>,
    private val onCourseClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {
    
    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivThumbnail: ImageView = itemView.findViewById(R.id.ivCourseThumbnail)
        val tvTitle: TextView = itemView.findViewById(R.id.tvCourseTitle)
        val tvInstructor: TextView = itemView.findViewById(R.id.tvInstructor)
        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        val tvDifficulty: TextView = itemView.findViewById(R.id.tvDifficulty)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvProgress: TextView = itemView.findViewById(R.id.tvProgress)
        val progressBar: android.widget.ProgressBar = itemView.findViewById(R.id.progressBar)
        val btnEnroll: androidx.appcompat.widget.AppCompatButton = itemView.findViewById(R.id.btnEnroll)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        
        holder.apply {
            tvTitle.text = course.title
            tvInstructor.text = course.instructor
            tvDuration.text = course.duration
            tvDifficulty.text = course.difficulty
            tvRating.text = "${course.rating}/5.0"
            tvPrice.text = "$${course.price}"
            
            // Set thumbnail
            val thumbnailResId = if (course.thumbnail.isNotEmpty()) {
                val resourceId = itemView.context.resources.getIdentifier(
                    course.thumbnail, "drawable", itemView.context.packageName
                )
                if (resourceId != 0) resourceId else R.drawable.ic_logo
            } else {
                R.drawable.ic_logo
            }
            
            ivThumbnail.setImageResource(thumbnailResId)
            
            // Update progress if enrolled
            if (course.isEnrolled) {
                val progressPercentage = if (course.totalLessons > 0) {
                    (course.completedLessons * 100) / course.totalLessons
                } else 0
                
                tvProgress.text = "$progressPercentage% Complete"
                progressBar.progress = progressPercentage
                progressBar.visibility = View.VISIBLE
                tvProgress.visibility = View.VISIBLE
                btnEnroll.text = "Continue"
                btnEnroll.isEnabled = true
            } else {
                progressBar.visibility = View.GONE
                tvProgress.visibility = View.GONE
                btnEnroll.text = "Enroll"
                btnEnroll.isEnabled = true
            }
            
            // Set difficulty color
            when (course.difficulty) {
                "Beginner" -> tvDifficulty.setTextColor(itemView.context.getColor(R.color.green))
                "Intermediate" -> tvDifficulty.setTextColor(itemView.context.getColor(R.color.orange))
                "Advanced" -> tvDifficulty.setTextColor(itemView.context.getColor(R.color.red))
            }
            
            // Set click listeners
            itemView.setOnClickListener {
                onCourseClick(course)
            }
            
            btnEnroll.setOnClickListener {
                onCourseClick(course)
            }
        }
    }
    
    override fun getItemCount(): Int = courses.size
    
    fun updateCourses(newCourses: List<Course>) {
        courses = newCourses
        notifyDataSetChanged()
    }
}
