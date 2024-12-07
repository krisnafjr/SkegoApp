package com.example.skegoapp.ui.main.routine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoutineViewModel : ViewModel() {
    // MutableLiveData to hold the list of routines
    private val _routines = MutableLiveData<List<Routine>>()
    val routines: LiveData<List<Routine>> = _routines

    init {
        // Initialize with some mock data
        _routines.value = listOf(
            Routine("Routine 1", "Description for routine 1", "Info 1"),
            Routine("Routine 2", "Description for routine 2", "Info 2"),
            Routine("Routine 3", "Description for routine 3", "Info 3")
        )
    }

    // Add a new routine
    fun addRoutine(routine: Routine) {
        val currentList = _routines.value.orEmpty().toMutableList()
        currentList.add(routine)
        _routines.value = currentList
    }
}

data class Routine(
    val title: String,
    val description: String,
    val info: String
)
