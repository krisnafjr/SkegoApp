package com.example.skegoapp.ui.adapter

import android.animation.ObjectAnimator
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.skegoapp.R
import com.example.skegoapp.data.pref.Routine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarAdapter(
    private val daysInMonth: List<Date>,
    private val routines: List<Routine>,
    private val onDateClickListener: (Date) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var selectedPosition: Int = -1
    private var highlightedDate: Date? = null
    private val today: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayTextView: TextView = itemView.findViewById(R.id.text_day)
        private val dateTextView: TextView = itemView.findViewById(R.id.text_date)
        private val categoryDot: View = itemView.findViewById(R.id.category_dot)

        fun bind(date: Date) {
            val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
            val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
            val fullDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val isToday = fullDateFormat.format(date) == today
            val isHighlighted = highlightedDate != null && fullDateFormat.format(date) == fullDateFormat.format(highlightedDate)

            // Set day and date text
            dayTextView.text = dayFormat.format(date)
            dateTextView.text = dateFormat.format(date)

            // Update background and text color based on state
            when {
                adapterPosition == selectedPosition -> {
                    itemView.setBackgroundResource(R.drawable.selected_calendar_item)
                    dayTextView.setTextColor(Color.WHITE)
                    dateTextView.setTextColor(Color.WHITE)
                }
                isHighlighted -> {
                    itemView.setBackgroundResource(R.drawable.highlighted_calendar_item)
                    dayTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    dateTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                }
                else -> {
                    itemView.setBackgroundResource(R.drawable.shape_calendar)
                    dayTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                    dateTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                }
            }

            // Highlight today
            if (isToday) {
                dayTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
                dateTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
            }

            // Normalize and check routines
            val apiDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val dateOnlyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val formattedDate = dateOnlyFormat.format(date)
            val routineExists = routines.any { routine ->
                val apiDate = apiDateFormat.parse(routine.date)
                val normalizedApiDate = apiDate?.let { dateOnlyFormat.format(it) }
                normalizedApiDate == formattedDate
            }

            // Show or hide category dot
            categoryDot.visibility = if (routineExists) View.VISIBLE else View.GONE

            // Handle click events
            itemView.setOnClickListener {
                playClickAnimation(it)
                selectedPosition = adapterPosition
                onDateClickListener(date)
                notifyDataSetChanged()
            }
        }

        private fun playClickAnimation(view: View) {
            val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1.0f)
            val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1.0f)
            scaleX.duration = 100
            scaleY.duration = 100
            scaleX.start()
            scaleY.start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar, parent, false)
        return CalendarViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(daysInMonth[position])
    }

    override fun getItemCount(): Int = daysInMonth.size

    fun highlightDate(date: Date) {
        highlightedDate = date
        notifyDataSetChanged()
    }
}