package com.example.skegoapp.ui.main.task

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skegoapp.data.pref.Task
import com.example.skegoapp.databinding.FragmentTaskBinding
import com.example.skegoapp.ui.adapter.TaskAdapter
import com.example.skegoapp.ui.main.add_task.AddTaskActivity
import com.example.skegoapp.ui.main.detail_task.DetailTaskActivity

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)

        // Set up RecyclerView
        setupRecyclerView()

        // Set up button listeners
        setupListeners()

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            data?.getParcelableExtra<Task>("newTask")?.let { newTask ->
                Log.d("TaskFragment", "New Task Received: $newTask")
                taskList.add(newTask)
                taskAdapter.notifyItemInserted(taskList.size - 1)
            } ?: Log.d("TaskFragment", "No Task Received")
        }
    }

    private fun handleStatusChange(task: Task, newStatus: String) {
        task.status = newStatus
        if (newStatus == "Finish") {
            taskList.remove(task)
            Toast.makeText(requireContext(), "Task finished!", Toast.LENGTH_SHORT).show()
        }
        taskAdapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        taskList.add(Task(1, "Create Database", "26 Nov", "26 November 2024", "HIGH", "WORK", "WORK", "On Progress", "Test"))
        taskList.add(Task(2, "Fix Bugs", "28 Nov", "28 November 2024", "MEDIUM", "SCHOOL", "WORK", "Not Started", "Test"))

        taskAdapter = TaskAdapter(taskList) { task ->
            // Navigasi ke DetailTaskActivity saat item diklik
            val intent = Intent(requireContext(), DetailTaskActivity::class.java)
            intent.putExtra("TASK", task) // Kirim objek Task
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
    }

    private fun setupListeners() {
        binding.iconAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddTaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        binding.btnGenerate.setOnClickListener {
            Toast.makeText(requireContext(), "Generate clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val ADD_TASK_REQUEST_CODE = 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


