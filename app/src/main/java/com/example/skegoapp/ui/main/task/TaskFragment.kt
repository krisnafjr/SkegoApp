package com.example.skegoapp.ui.main.task

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skegoapp.data.pref.Task
import com.example.skegoapp.data.pref.UserPreference
import com.example.skegoapp.data.pref.dataStore
import com.example.skegoapp.databinding.FragmentTaskBinding
import com.example.skegoapp.ui.adapter.TaskAdapter
import com.example.skegoapp.ui.main.add_task.AddTaskActivity
import com.example.skegoapp.ui.main.detail_task.DetailTaskActivity
import com.example.skegoapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()

    private var userId: Int = 0 // Variabel untuk menyimpan user_id
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)

        // Inisialisasi UserPreference
        userPreference = UserPreference.getInstance(requireContext().dataStore)

        // Mengambil user_id dan status login
        lifecycleScope.launch {
            userPreference.getSession().collect { user ->
                if (user.isLogin) {
                    userId = user.userId
                    fetchTasksFromApi() // Ambil tasks jika user login
                } else {
                    Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set up RecyclerView
        setupRecyclerView()

        // Set up button listeners
        setupListeners()

        return binding.root
    }

    // Fetch tasks from API only if userId is valid
    private fun fetchTasksFromApi() {
        if (userId == 0) {
            Log.e("TaskFragment", "User ID is invalid")
            return
        }

        ApiConfig.getApiService().getTasksByUserId(userId).enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    response.body()?.let { tasks ->
                        taskList.clear() // Clear the list to avoid duplicates
                        taskList.addAll(tasks)
                        taskAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("TaskFragment", "Failed to load tasks: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                Log.e("TaskFragment", "Error fetching tasks: ${t.message}")
            }
        })
    }

    // Setup RecyclerView and its adapter
    private fun setupRecyclerView() {
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

    // Setup listeners for buttons (e.g., Add Task button)
    private fun setupListeners() {
        binding.iconAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddTaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }

        binding.btnGenerate.setOnClickListener {
            Toast.makeText(requireContext(), "Generate clicked!", Toast.LENGTH_SHORT).show()
        }
    }

    // Handling the result from AddTaskActivity
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

    companion object {
        private const val ADD_TASK_REQUEST_CODE = 1
    }

    // Clean up the binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
