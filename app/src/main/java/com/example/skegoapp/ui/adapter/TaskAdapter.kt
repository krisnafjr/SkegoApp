package com.example.skegoapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skegoapp.R
import com.example.skegoapp.data.pref.Task
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private val tasks: List<Task>,
    private val itemClickListener: (Task) -> Unit // Listener untuk item klik
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTitle: TextView = itemView.findViewById(R.id.task_title)
        val dueDate: TextView = itemView.findViewById(R.id.due_date)
        val priority: TextView = itemView.findViewById(R.id.task_difficulty)
        val taskType: TextView = itemView.findViewById(R.id.task_category)
        val statusSpinner: Spinner = itemView.findViewById(R.id.status_spinner)

        init {
            itemView.setOnClickListener {
                // Kirim task saat item diklik
                itemClickListener(tasks[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]

        // Set data untuk setiap elemen tampilan
        holder.taskTitle.text = currentTask.task_name ?: "No Task Name"
        holder.dueDate.text = "Due: ${formatDeadline(currentTask.deadline)}"
        holder.priority.text = currentTask.difficulty_level.toString()
        holder.taskType.text = currentTask.category

        // Set background warna untuk difficultyLevel
        holder.priority.setBackgroundColor(
            holder.itemView.context.getColor(
                when (currentTask.difficulty_level) {
                    1 -> R.color.red
                    2 -> R.color.yellow
                    3 -> R.color.green
                    else -> R.color.gray
                }
            )
        )

        // Set background warna untuk kategori
        holder.taskType.setBackgroundColor(
            holder.itemView.context.getColor(
                when (currentTask.category.uppercase()) {
                    "WORK" -> R.color.blue
                    "SCHOOL" -> R.color.orange
                    "PERSONAL" -> R.color.purple
                    else -> R.color.white
                }
            )
        )

        // Setup Spinner untuk status
        val statusAdapter = ArrayAdapter.createFromResource(
            holder.itemView.context,
            R.array.status_array,
            android.R.layout.simple_spinner_item
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.statusSpinner.adapter = statusAdapter

        // Set status spinner ke nilai yang sesuai
        val statusPosition = statusAdapter.getPosition(currentTask.status)
        holder.statusSpinner.setSelection(statusPosition)

        // Update status saat spinner dipilih
        holder.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val newStatus = parent.getItemAtPosition(position).toString()
                if (currentTask.status != newStatus) {
                    currentTask.status = newStatus
                    // Anda dapat menambahkan logika untuk menyimpan perubahan status ke server/DB
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun getItemCount(): Int = tasks.size

    private fun formatDeadline(deadline: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return try {
            val date = inputFormat.parse(deadline)
            date?.let { outputFormat.format(it) } ?: "Invalid Date"
        } catch (e: Exception) {
            "Invalid Date"
        }
    }
}
