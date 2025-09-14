package com.skillup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skillup.R
import com.skillup.models.Question

class QuizAdapter(
    private val questions: List<Question>,
    private val onAnswerSelected: (Int, Int) -> Unit
) : RecyclerView.Adapter<QuizAdapter.QuestionViewHolder>() {
    
    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestionNumber: TextView = itemView.findViewById(R.id.tvQuestionNumber)
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val radioGroup: RadioGroup = itemView.findViewById(R.id.radioGroup)
        val rbOption1: RadioButton = itemView.findViewById(R.id.rbOption1)
        val rbOption2: RadioButton = itemView.findViewById(R.id.rbOption2)
        val rbOption3: RadioButton = itemView.findViewById(R.id.rbOption3)
        val rbOption4: RadioButton = itemView.findViewById(R.id.rbOption4)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz, parent, false)
        return QuestionViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        
        holder.apply {
            tvQuestionNumber.text = "Question ${position + 1}"
            tvQuestion.text = question.question
            
            // Set options
            val radioButtons = listOf(rbOption1, rbOption2, rbOption3, rbOption4)
            val options = question.options
            
            radioButtons.forEachIndexed { index, radioButton ->
                if (index < options.size) {
                    radioButton.text = options[index]
                    radioButton.visibility = View.VISIBLE
                } else {
                    radioButton.visibility = View.GONE
                }
            }
            
            // Set selected answer if any
            if (question.selectedAnswer != -1) {
                radioGroup.check(radioButtons[question.selectedAnswer].id)
            } else {
                radioGroup.clearCheck()
            }
            
            // Handle radio button selection
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val selectedIndex = when (checkedId) {
                    R.id.rbOption1 -> 0
                    R.id.rbOption2 -> 1
                    R.id.rbOption3 -> 2
                    R.id.rbOption4 -> 3
                    else -> -1
                }
                
                if (selectedIndex != -1 && selectedIndex < options.size) {
                    onAnswerSelected(position, selectedIndex)
                }
            }
        }
    }
    
    override fun getItemCount(): Int = questions.size
}
