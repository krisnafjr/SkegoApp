package com.example.skegoapp.data.pref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val idTask: Int,               // id_task
    val user_id: Int,               // user_id
    val task_name: String,          // task_name
    val difficulty_level: Int,      // difficulty_level
    val deadline: String,          // deadline
    val duration: Int,             // duration
    val priorityScore: Int,        // priority_score
    var status: String,            // status
    val category: String,          // category
    val detail: String,            // detail
    val hourOfDay: Int,            // hour_of_day
    val dayOfWeek: Int,            // day_of_week
    val daysUntilDeadline: Int,    // days_until_deadline
    val createdAt: String,         // created_at
    val updatedAt: String?         // updated_at
) : Parcelable





