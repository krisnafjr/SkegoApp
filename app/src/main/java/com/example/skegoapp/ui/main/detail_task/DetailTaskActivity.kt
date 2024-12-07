package com.example.skegoapp.ui.main.detail_task

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.skegoapp.R
import com.example.skegoapp.data.pref.Task

class DetailTaskActivity : AppCompatActivity() {
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        // Ambil data dari Intent
        task = intent.getParcelableExtra("TASK") ?: return // Ambil objek Task

        val backButton: ImageButton = findViewById(R.id.btn_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Tampilkan detail task
        displayTaskDetails(task)

        // Set up button listeners
        setupButtonListeners()
    }

    private fun displayTaskDetails(task: Task) {
        findViewById<TextView>(R.id.task_name_text_view).text = task.title
        findViewById<TextView>(R.id.task_date_text_view).text = task.fullDeadline
        findViewById<TextView>(R.id.task_priority_text_view).text = task.priority
        findViewById<TextView>(R.id.task_category_text_view).text = task.type
        findViewById<TextView>(R.id.task_detail_text_view).text = task.detail
    }

    private fun setupButtonListeners() {
        // Hapus tombol edit dan hapus jika tidak diperlukan
        // Jika Anda ingin menambahkan logika lain, silakan sesuaikan di sini
    }
}