package com.example.schoolreminderr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.Task

class TaskAdapter(private val list: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.tvTitle)
        val desc = view.findViewById<TextView>(R.id.tvDesc)
        val deadline = view.findViewById<TextView>(R.id.tvDeadline)
        val status = view.findViewById<TextView>(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.title.text = data.title
        holder.desc.text = data.desc
        holder.deadline.text = data.deadline
        holder.status.text = data.status
    }
}