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

class TaskAdapter(
    private val tasks: List<Task>,
    private val itemClickListener: (Task) -> Unit // Listener untuk item click
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

        holder.taskTitle.text = currentTask.title
        holder.priority.text = currentTask.priority
        holder.taskType.text = currentTask.type
        holder.dueDate.text = "Due ${currentTask.deadline}"

        // Set background color based on priority
        holder.priority.setBackgroundColor(
            holder.itemView.context.getColor(
                when (currentTask.priority) {
                    "HIGH" -> R.color.red
                    "MEDIUM" -> R.color.yellow
                    "LOW" -> R.color.green
                    else -> R.color.gray
                }
            )
        )

        holder.taskType.setBackgroundColor(
            holder.itemView.context.getColor(
                when (currentTask.type) {
                    "WORK" -> R.color.blue
                    "SCHOOL" -> R.color.orange
                    "PERSONAL" -> R.color.purple
                    else -> R.color.white
                }
            )
        )

        // Set the status spinner
        val statusAdapter = ArrayAdapter.createFromResource(
            holder.itemView.context,
            R.array.status_array,
            android.R.layout.simple_spinner_item
        )
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.statusSpinner.adapter = statusAdapter

        // Set current selection for the spinner
        val statusPosition = statusAdapter.getPosition(currentTask.status)
        holder.statusSpinner.setSelection(statusPosition)

        // Listen for status change
        holder.statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val newStatus = parent.getItemAtPosition(position).toString()
                // Update the task status
                currentTask.status = newStatus
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun getItemCount(): Int = tasks.size
}

