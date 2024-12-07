package com.example.skegoapp.ui.main.add_task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddTaskViewModel : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    private val _dueDate = MutableLiveData<String>()
    val dueDate: LiveData<String> get() = _dueDate

    private val _difficulty = MutableLiveData<String>()
    val difficulty: LiveData<String> get() = _difficulty

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> get() = _category

    private val _estimatedDuration = MutableLiveData<String>()
    val estimatedDuration: LiveData<String> get() = _estimatedDuration

    private val _detail = MutableLiveData<String>()
    val detail: LiveData<String> get() = _detail

    fun saveTask() {
        // Logic to save the task can be added here
    }

    fun setTitle(title: String) {
        _title.value = title
    }

    fun setDueDate(dueDate: String) {
        _dueDate.value = dueDate
    }

    fun setDifficulty(difficulty: String) {
        _difficulty.value = difficulty
    }

    fun setCategory(category: String) {
        _category.value = category
    }

    fun setEstimatedDuration(duration: String) {
        _estimatedDuration.value = duration
    }

    fun setDetail(detail: String) {
        _detail.value = detail
    }
}