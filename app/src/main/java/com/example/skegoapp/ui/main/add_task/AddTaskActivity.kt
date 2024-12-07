package com.example.skegoapp.ui.main.add_task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.skegoapp.R
import com.example.skegoapp.data.pref.Task
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // Tombol Back
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            onBackPressed()
        }

        // Inisialisasi dropdown
        setupDropdowns()

        // Tombol Save
        findViewById<Button>(R.id.btn_save).setOnClickListener {
            saveTask()
        }

        // Input Due Date
        val dueDateEditText = findViewById<TextInputEditText>(R.id.et_due_date)
        dueDateEditText.apply {
            isFocusable = false
            isClickable = true
            setOnClickListener { showDatePicker() }
        }
    }

    private fun setupDropdowns() {
        // Dropdown untuk Difficulty
        val difficultyArray = resources.getStringArray(R.array.difficulty_array)
        val difficultyAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, difficultyArray)
        findViewById<AutoCompleteTextView>(R.id.dropdown_difficulty).setAdapter(difficultyAdapter)

        // Dropdown untuk Category
        val categoryArray = resources.getStringArray(R.array.category_array)
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryArray)
        findViewById<AutoCompleteTextView>(R.id.dropdown_category).setAdapter(categoryAdapter)

        // Dropdown untuk Duration
        val durationArray = resources.getStringArray(R.array.duration_array)
        val durationAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, durationArray)
        findViewById<AutoCompleteTextView>(R.id.dropdown_duration).setAdapter(durationAdapter)
    }

    private fun saveTask() {
        val title = findViewById<TextInputEditText>(R.id.et_title_task).text.toString()
        val dueDate = findViewById<TextInputEditText>(R.id.et_due_date).text.toString()
        val difficulty = findViewById<AutoCompleteTextView>(R.id.dropdown_difficulty).text.toString()
        val category = findViewById<AutoCompleteTextView>(R.id.dropdown_category).text.toString()
        val duration = findViewById<AutoCompleteTextView>(R.id.dropdown_duration).text.toString()
        val detail = findViewById<TextInputEditText>(R.id.et_detail).text.toString()

        // Validasi input
        if (title.isEmpty() || dueDate.isEmpty() || difficulty.isEmpty() || category.isEmpty() || duration.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Format tanggal
        val formattedDueDate = formatDueDate(dueDate)
        val fullFormattedDueDate = formatFullDueDate(dueDate)

        // Buat objek Task baru
        val newTask = Task(
            id = UUID.randomUUID().hashCode(),
            title = title,
            deadline = formattedDueDate,
            fullDeadline = fullFormattedDueDate,
            priority = difficulty,
            type = category,
            duration = duration,
            status = "Not Started",
            detail = detail
        )

        // Kirim data kembali ke TaskFragment
        setResult(RESULT_OK, Intent().putExtra("newTask", newTask))
        finish()
    }

    private fun formatDueDate(dueDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
            outputFormat.format(inputFormat.parse(dueDate)!!)
        } catch (e: Exception) {
            dueDate
        }
    }

    private fun formatFullDueDate(dueDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            outputFormat.format(inputFormat.parse(dueDate)!!)
        } catch (e: Exception) {
            dueDate
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val dueDateEditText = findViewById<TextInputEditText>(R.id.et_due_date)
            dueDateEditText.setText(String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear))
        }, year, month, day).show()
    }
}

