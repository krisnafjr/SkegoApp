package com.example.skegoapp.ui.main.add_task

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.skegoapp.R
import com.example.skegoapp.data.pref.Task
import com.example.skegoapp.data.pref.UserPreference
import com.example.skegoapp.data.pref.dataStore
import com.example.skegoapp.data.remote.retrofit.ApiConfig
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // Initialize UserPreference with DataStore
        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { onBackPressed() }
        setupDropdowns()
        findViewById<Button>(R.id.btn_save).setOnClickListener { saveTask() }

        val dueDateEditText = findViewById<TextInputEditText>(R.id.et_due_date)
        dueDateEditText.apply {
            isFocusable = false
            isClickable = true
            setOnClickListener { showDatePicker() }
        }
    }

    private fun setupDropdowns() {
        setupDropdown(R.id.dropdown_difficulty, R.array.difficulty_array)
        setupDropdown(R.id.dropdown_category, R.array.category_array)
        setupDropdown(R.id.dropdown_duration, R.array.duration_array)
    }

    private fun setupDropdown(viewId: Int, arrayId: Int) {
        val array = resources.getStringArray(arrayId)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, array)
        findViewById<AutoCompleteTextView>(viewId).setAdapter(adapter)
    }

    private fun saveTask() {
        // Input fields
        val title = findViewById<TextInputEditText>(R.id.et_title_task).text.toString()
        val dueDate = findViewById<TextInputEditText>(R.id.et_due_date).text.toString()
        val difficulty = findViewById<AutoCompleteTextView>(R.id.dropdown_difficulty).text.toString()
        val category = findViewById<AutoCompleteTextView>(R.id.dropdown_category).text.toString()
        val duration = findViewById<AutoCompleteTextView>(R.id.dropdown_duration).text.toString()
        val detail = findViewById<TextInputEditText>(R.id.et_detail).text.toString()

        // Validation
        if (title.isEmpty() || dueDate.isEmpty() || difficulty.isEmpty() || category.isEmpty() || duration.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Get userId from UserPreference
        lifecycleScope.launch {
            val user = userPreference.getSession().collect { userModel ->
                val userId = userModel.userId

                try {
                    val formattedDueDate = formatDueDate(dueDate)
                    val daysUntilDeadline = calculateDaysUntilDeadline(dueDate)

                    val newTask = Task(
                        idTask = 0, // Will be replaced by server
                        user_id = userId,
                        task_name = title,
                        difficulty_level = difficulty.toIntOrNull() ?: 1,
                        deadline = formattedDueDate,
                        duration = duration.toIntOrNull() ?: 0,
                        priorityScore = calculatePriorityScore(difficulty, duration),
                        status = "pending",
                        category = category,
                        detail = detail,
                        hourOfDay = 22, // Adjust as needed
                        dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
                        daysUntilDeadline = daysUntilDeadline,
                        createdAt = "", // Will be set by server
                        updatedAt = null
                    )

                    val apiService = ApiConfig.getApiService()
                    apiService.addTask(newTask).enqueue(object : Callback<Task> {
                        override fun onResponse(call: Call<Task>, response: Response<Task>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@AddTaskActivity, "Task added successfully!", Toast.LENGTH_SHORT).show()
                                setResult(RESULT_OK)
                                finish()
                            } else {
                                Toast.makeText(this@AddTaskActivity, "Failed to add task: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Task>, t: Throwable) {
                            Toast.makeText(this@AddTaskActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                } catch (e: Exception) {
                    Toast.makeText(this@AddTaskActivity, "Invalid data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun formatDueDate(dueDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            outputFormat.format(inputFormat.parse(dueDate)!!)
        } catch (e: Exception) {
            dueDate // Return the original value if formatting fails
        }
    }

    private fun calculateDaysUntilDeadline(dueDate: String): Int {
        val currentDate = Calendar.getInstance().time
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val due = sdf.parse(dueDate)
        val diff = due.time - currentDate.time
        return (diff / (1000 * 60 * 60 * 24)).toInt()
    }

    private fun calculatePriorityScore(difficulty: String, duration: String): Int {
        return (difficulty.toIntOrNull() ?: 1) * (duration.toIntOrNull() ?: 1)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            findViewById<TextInputEditText>(R.id.et_due_date).setText(
                String.format("%02d/%02d/%d", day, month + 1, year)
            )
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}
