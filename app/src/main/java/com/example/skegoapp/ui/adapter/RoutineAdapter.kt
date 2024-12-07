package com.example.skegoapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skegoapp.R
import com.example.skegoapp.data.pref.Routine

class RoutineAdapter(private val routineList: List<Routine>) :
    RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder>() {

    // Create and return the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_routine, parent, false)
        return RoutineViewHolder(view)
    }

    // Bind the data to the ViewHolder
    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routineList[position]
        holder.routineTitle.text = routine.title
        //holder.routineDate.text = routine.date
        holder.routineTime.text = routine.time
        holder.routineLocation.text = routine.location
        holder.routineDetail.text = routine.detail
        //holder.routineCategory.text = routine.category
    }

    // Return the total count of items
    override fun getItemCount(): Int {
        return routineList.size
    }

    // ViewHolder class for the Routine item
    inner class RoutineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val routineTitle: TextView = view.findViewById(R.id.routine_title)
        // val routineDate: TextView = view.findViewById(R.id.routine_date)
        val routineTime: TextView = view.findViewById(R.id.routine_time)
        val routineLocation: TextView = view.findViewById(R.id.routine_location)
        val routineDetail: TextView = view.findViewById(R.id.routine_description)
        //val routineCategory: TextView = view.findViewById(R.id.routine_category)
    }
}