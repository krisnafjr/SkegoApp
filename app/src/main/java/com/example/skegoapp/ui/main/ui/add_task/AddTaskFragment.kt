package com.example.skegoapp.ui.main.ui.add_task

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.skegoapp.R

class AddTaskFragment : Fragment() {

    companion object {
        fun newInstance() = AddTaskFragment()
    }

    private val viewModel: AddTaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }
}