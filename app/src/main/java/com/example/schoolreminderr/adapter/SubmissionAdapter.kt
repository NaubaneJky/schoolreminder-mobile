package com.example.schoolreminderr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.model.SubmissionModel

class SubmissionAdapter(
    private val list: List<SubmissionModel>
) : RecyclerView.Adapter<SubmissionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvName: TextView = view.findViewById(R.id.tvStudentName)
        val btnOpen: Button = view.findViewById(R.id.btnOpen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_submission_student, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        holder.tvName.text = item.studentName
    }
}