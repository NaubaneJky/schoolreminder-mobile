package com.example.schoolreminderr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.TeacherTask

class TeacherTaskAdapter(
    private val list: List<TeacherTask>
) : RecyclerView.Adapter<TeacherTaskAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvDeadline = view.findViewById<TextView>(R.id.tvDeadline)
        val tvSubmission = view.findViewById<TextView>(R.id.tvSubmission)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val progress = view.findViewById<ProgressBar>(R.id.progressTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_teacher_task, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        holder.tvTitle.text = item.title
        holder.tvDeadline.text = item.deadline
        holder.tvSubmission.text = item.submission
        holder.tvStatus.text = item.status
        holder.progress.progress = item.progress
    }
}