package com.example.skegoapp.data.pref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var id: Int,
    var title: String,
    var deadline: String,
    val fullDeadline: String,
    var priority: String,
    var type: String,
    var duration: String,
    var status: String = "Not Started", // Default status
    var detail: String = "" // Default detail
) : Parcelable




