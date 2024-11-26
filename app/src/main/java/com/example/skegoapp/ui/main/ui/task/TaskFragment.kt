package com.example.skegoapp.ui.main.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skegoapp.R
import com.example.skegoapp.databinding.FragmentTaskBinding

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)

        // Listener untuk ikon menu
        binding.iconMenu.setOnClickListener {
            // Tampilkan log atau aksi lainnya
            Toast.makeText(requireContext(), "Menu clicked!", Toast.LENGTH_SHORT).show()
        }

        // Listener untuk ikon notifikasi
        binding.iconNotification.setOnClickListener {
            // Tampilkan log atau aksi lainnya
            Toast.makeText(requireContext(), "Notification clicked!", Toast.LENGTH_SHORT).show()
        }

        // Listener untuk tombol generate
        binding.btnGenerate.setOnClickListener {
            // Tampilkan log atau aksi lainnya
            Toast.makeText(requireContext(), "Generate clicked!", Toast.LENGTH_SHORT).show()
        }

        // Navigasi saat FAB diklik
        binding.addTask.setOnClickListener {
            findNavController().navigate(R.id.action_taskFragment_to_addTaskFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


