package com.example.skegoapp.ui.main.add_task

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.skegoapp.R
import com.example.skegoapp.data.Task
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // Inisialisasi ImageButton untuk tombol back
        val backButton: ImageButton = findViewById(R.id.btn_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Inisialisasi adapter untuk dropdown
        setupDropdowns()

        // Inisialisasi tombol Save
        val saveButton: Button = findViewById(R.id.btn_save)
        saveButton.setOnClickListener {
            saveTask()
        }

        val dueDateEditText: TextInputEditText = findViewById(R.id.et_due_date)
        dueDateEditText.isFocusable = false
        dueDateEditText.isClickable = true
        dueDateEditText.setOnClickListener {
            showDatePicker(it)
        }
    }

    private fun setupDropdowns() {
        val difficultyArray = resources.getStringArray(R.array.difficulty_array)
        if (difficultyArray.isNotEmpty()) {
            val difficultyAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, difficultyArray)
            findViewById<AutoCompleteTextView>(R.id.dropdown_difficulty).setAdapter(difficultyAdapter)
        }

        val categoryArray = resources.getStringArray(R.array.category_array)
        if (categoryArray.isNotEmpty()) {
            val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryArray)
            findViewById<AutoCompleteTextView>(R.id.dropdown_category).setAdapter(categoryAdapter)
        }
    }

    private fun saveTask() {
        val title = findViewById<TextInputEditText>(R.id.et_title_task).text.toString()
        val dueDateInput = findViewById <TextInputEditText>(R.id.et_due_date).text.toString()
        val difficulty = findViewById<AutoCompleteTextView>(R.id.dropdown_difficulty).text.toString()
        val category = findViewById<AutoCompleteTextView>(R.id.dropdown_category).text.toString()
        val detail = findViewById<TextInputEditText>(R.id.et_detail).text.toString()

        // Validasi input
        if (title.isEmpty() || dueDateInput.isEmpty() || difficulty.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Format tanggal
        val formattedDueDate = formatDueDate(dueDateInput)
        val fullFormattedDueDate = formatFullDueDate(dueDateInput)

        // Buat task baru dengan ID unik menggunakan UUID
        val newTask = Task(UUID.randomUUID().hashCode(), title, formattedDueDate, fullFormattedDueDate, difficulty, category, 60, "Not Started", detail)

        // Kembalikan data ke TaskFragment
        val resultIntent = Intent()
        resultIntent.putExtra("newTask", newTask)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun formatDueDate(dueDate: String): String {
        // Misalnya, jika inputnya adalah "26/11/2024", kita ingin mengubahnya menjadi "26 Nov"
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
            val date = inputFormat.parse(dueDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            // Jika terjadi kesalahan, kembalikan input asli atau tangani kesalahan sesuai kebutuhan
            dueDate
        }
    }

    private fun formatFullDueDate(dueDate: String): String {
        // Misalnya, jika inputnya adalah "26/11/2024", kita ingin mengubahnya menjadi "26 November 2024"
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(dueDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            // Jika terjadi kesalahan, kembalikan input asli atau tangani kesalahan sesuai kebutuhan
            dueDate
        }
    }

    fun showDatePicker(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val dueDateEditText: TextInputEditText = findViewById(R.id.et_due_date)
            // Format tanggal menjadi "dd/MM/yyyy"
            dueDateEditText.setText(String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear))
        }, year, month, day)

        datePickerDialog.show()
    }
}
