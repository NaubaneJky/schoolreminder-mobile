package com.example.schoolreminderr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.SubmissionData

class SubmissionAdapter(
    private val list: List<SubmissionData>
) : RecyclerView.Adapter<SubmissionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvStudentName)
        val btnOpen: Button  = view.findViewById(R.id.btnOpen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_submission_student, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvName.text = item.student?.name ?: "Siswa #${item.student_id}"

        if (item.file != null) {
            holder.btnOpen.text = "Buka File"
            holder.btnOpen.alpha = 1.0f
            holder.btnOpen.setOnClickListener {
                // buka file URL
                val url = "http://192.168.100.6:8000/storage/${item.file}"
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse(url)
                holder.itemView.context.startActivity(intent)
            }
        } else {
            holder.btnOpen.text = "Belum"
            holder.btnOpen.alpha = 0.4f
            holder.btnOpen.setOnClickListener(null)
        }
    }
}