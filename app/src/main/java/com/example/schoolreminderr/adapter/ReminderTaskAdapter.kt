package com.example.schoolreminderr.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.ReminderTask

class ReminderTaskAdapter(
    private val list: List<ReminderTask>
) : RecyclerView.Adapter<ReminderTaskAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvDeadline = view.findViewById<TextView>(R.id.tvDeadline)
        val tvSubject = view.findViewById<TextView>(R.id.tvSubject)
        val tvDesc = view.findViewById<TextView>(R.id.tvDesc)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder_task, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        holder.tvDeadline.text = item.deadline
        holder.tvSubject.text = item.subject
        holder.tvDesc.text = item.description
        holder.tvStatus.text = item.status

        holder.tvDeadline.backgroundTintList =
            ColorStateList.valueOf(Color.parseColor(item.color))
    }
}