package com.example.skegoapp.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skegoapp.R
import com.example.skegoapp.data.pref.Routine
import com.example.skegoapp.ui.main.detail_routine.DetailRoutineActivity
import com.example.skegoapp.data.remote.retrofit.ApiService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoutineAdapter(
    private val routineList: MutableList<Routine>,  // Changed to MutableList for removal
    private val apiService: ApiService            // Added ApiService for delete functionality
) : RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    // Create and return the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine, parent, false)
        return RoutineViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routineList[position]
        holder.routineTitle.text = routine.title
        holder.routineTime.text = routine.time
        holder.routineLocation.text = routine.location
        holder.routineDetail.text = routine.detail

        // Set click listener for the routine card to navigate to DetailRoutineActivity
        holder.itemView.setOnClickListener {
            val context = it.context
            val intent = Intent(context, DetailRoutineActivity::class.java)
            Log.d("RoutineAdapter", "Routine: ${routine.title}")  // Display log
            intent.putExtra("ROUTINE", routine)
            context.startActivity(intent)
        }

        // Set click listener for options icon to show popup menu
        holder.icOption.setOnClickListener { v ->
            val popupMenu = PopupMenu(v.context, v)
            popupMenu.menuInflater.inflate(R.menu.routine_options_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_edit -> {
                        // Handle Edit action
                        Log.d("RoutineAdapter", "Edit: ${routine.title}")
                        true
                    }
                    R.id.menu_delete -> {
                        // Handle Delete action
                        Log.d("RoutineAdapter", "Delete: ${routine.title}")
                        showDeleteConfirmationDialog(routine, position,v.context)  // Show confirmation dialog
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    // Return the total count of items
    override fun getItemCount(): Int {
        return routineList.size
    }

    // ViewHolder class for the Routine item
    inner class RoutineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val routineTitle: TextView = view.findViewById(R.id.routine_title)
        val routineTime: TextView = view.findViewById(R.id.routine_time)
        val routineLocation: TextView = view.findViewById(R.id.routine_location)
        val routineDetail: TextView = view.findViewById(R.id.routine_description)
        val icOption: ImageView = view.findViewById(R.id.ic_option)
    }

    // Function to show confirmation dialog for deletion
    private fun showDeleteConfirmationDialog(routine: Routine, position: Int, context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this routine?")
            .setPositiveButton("Yes") { dialog, which ->
                deleteRoutine(routine, position)  // Call delete function if confirmed
            }
            .setNegativeButton("No", null)  // Null will dismiss the dialog on click
            .show()
    }

    // Function to delete routine from the list and API
    private fun deleteRoutine(routine: Routine, position: Int) {
        // Call API to delete the routine
        apiService.deleteRoutine(routine.routineId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("RoutineAdapter", "Routine deleted: ${routine.title}")
                    // Remove from the list and notify adapter
                    routineList.removeAt(position)
                    notifyItemRemoved(position)
                } else {
                    Log.e("RoutineAdapter", "Error deleting routine: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("RoutineAdapter", "Error: ${t.message}")
            }
        })
    }
}
