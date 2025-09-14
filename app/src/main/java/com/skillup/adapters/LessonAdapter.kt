package com.skillup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skillup.R
import com.skillup.models.Lesson

class LessonAdapter(
    private val lessons: List<Lesson>,
    private val onLessonClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {
    
    class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPlayIcon: ImageView = itemView.findViewById(R.id.ivPlayIcon)
        val tvLessonTitle: TextView = itemView.findViewById(R.id.tvLessonTitle)
        val tvLessonDescription: TextView = itemView.findViewById(R.id.tvLessonDescription)
        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        val ivCompleted: ImageView = itemView.findViewById(R.id.ivCompleted)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return LessonViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]
        
        holder.apply {
            tvLessonTitle.text = lesson.title
            tvLessonDescription.text = lesson.description
            tvDuration.text = lesson.duration
            
            // Update completion status
            if (lesson.isCompleted) {
                ivCompleted.visibility = View.VISIBLE
                ivPlayIcon.setImageResource(R.drawable.ic_check_circle)
                itemView.alpha = 0.7f
            } else {
                ivCompleted.visibility = View.GONE
                ivPlayIcon.setImageResource(R.drawable.ic_play_circle)
                itemView.alpha = 1.0f
            }
            
            // Set click listener
            itemView.setOnClickListener {
                onLessonClick(lesson)
            }
        }
    }
    
    override fun getItemCount(): Int = lessons.size
}
