package com.example.skegoapp.ui.main.routine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skegoapp.R
import com.example.skegoapp.databinding.FragmentRoutineBinding
import com.example.skegoapp.ui.adapter.RoutineAdapter
import com.example.skegoapp.ui.main.add_routine.AddRoutineActivity
import com.example.skegoapp.data.remote.retrofit.ApiConfig
import com.example.skegoapp.data.pref.Routine
import com.example.skegoapp.ui.adapter.CalendarAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class RoutineFragment : Fragment() {

    private var _binding: FragmentRoutineBinding? = null
    private val binding get() = _binding!!

    private lateinit var monthCalendarAdapter: CalendarAdapter
    private lateinit var calendar: Calendar
    private lateinit var routineAdapter: RoutineAdapter
    private var routines: List<Routine> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutineBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize calendar instance
        calendar = Calendar.getInstance()

        setupMonthCalendar()
        setupMonthNavigation()
        setupCalendarButton()
        setupAddRoutineButton()

        return root
    }

    private fun setupMonthCalendar() {
        updateCalendarDays()
        highlightToday()
    }

    private fun updateCalendarDays() {
        // Get all dates in the current month
        val daysInMonth = getDatesInMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))

        // Set adapter with date click listener
        monthCalendarAdapter = CalendarAdapter(daysInMonth, routines) { selectedDate ->
            // Show selected date in a text view
            binding.currentDate.text =
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate)
        }

        // Configure horizontal calendar layout
        binding.horizontalCalendar.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.horizontalCalendar.adapter = monthCalendarAdapter
    }

    private fun setupMonthNavigation() {
        binding.prevMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, -1) // Navigate to previous month
            updateCalendarDays()
        }
        binding.nextMonth.setOnClickListener {
            calendar.add(Calendar.MONTH, 1) // Navigate to next month
            updateCalendarDays()
        }
    }

    private fun highlightToday() {
        val today = Calendar.getInstance().time
        monthCalendarAdapter.highlightDate(today) // Highlight today's date
    }

    private fun setupCalendarButton() {
        binding.iconCalendar.setOnClickListener {
            // Initialize MaterialDatePicker
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(parentFragmentManager, "DATE_PICKER")

            // Listener when user selects a date
            datePicker.addOnPositiveButtonClickListener { selection ->
                val selectedDate = Date(selection)
                // Show selected date
                binding.currentDate.text =
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate)

                // Call method to update the view dynamically
                updateCalendarViewToSelectedDate(selectedDate)
            }
        }
    }

    private fun getPositionOfDate(selectedDate: Date): Int {
        val daysInMonth = getDatesInMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
        return daysInMonth.indexOfFirst {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it) ==
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate)
        }
    }

    private fun updateCalendarViewToSelectedDate(selectedDate: Date) {
        // Set calendar to selected date
        calendar.time = selectedDate

        // Update days in the horizontal calendar
        updateCalendarDays()

        // Highlight the selected date
        monthCalendarAdapter.highlightDate(selectedDate)

        // Scroll the horizontal calendar to the selected date
        val selectedPosition = getPositionOfDate(selectedDate)
        binding.horizontalCalendar.layoutManager?.scrollToPosition(selectedPosition)
    }

    private fun setupAddRoutineButton() {
        binding.addRoutine.setOnClickListener {
            Toast.makeText(requireContext(), "Tambah rutinitas ditekan!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), AddRoutineActivity::class.java))
        }
    }

    private fun getDatesInMonth(year: Int, month: Int): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val dates = mutableListOf<Date>()
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 1..maxDay) {
            dates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dates
    }

    override fun onResume() {
        super.onResume()
        // Load routines when Fragment resumes
        loadRoutines()
    }

    private fun loadRoutines() {
        ApiConfig.getApiService().getRoutines().enqueue(object : Callback<List<Routine>> {
            override fun onResponse(call: Call<List<Routine>>, response: Response<List<Routine>>) {
                if (response.isSuccessful) {
                    routines = response.body() ?: emptyList()
                    // Update routine list in RecyclerView
                    updateRoutineList(routines)

                    // Update the calendar adapter with the new routines
                    monthCalendarAdapter = CalendarAdapter(
                        getDatesInMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)),
                        routines
                    ) { selectedDate ->
                        binding.currentDate.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate)
                    }

                    // Notify adapter to update the view with new routines
                    monthCalendarAdapter.notifyDataSetChanged()

                    // Update the horizontal calendar with the updated adapter
                    binding.horizontalCalendar.adapter = monthCalendarAdapter

                } else {
                    Toast.makeText(requireContext(), "Gagal memuat rutinitas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Routine>>, t: Throwable) {
                Toast.makeText(requireContext(), "Kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateRoutineList(routines: List<Routine>) {
        routineAdapter = RoutineAdapter(routines)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = routineAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
