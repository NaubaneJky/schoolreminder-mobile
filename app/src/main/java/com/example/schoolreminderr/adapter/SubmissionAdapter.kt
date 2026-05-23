package com.example.schoolreminderr.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.activity.GradeSubmissionActivity
import com.example.schoolreminderr.model.SubmissionData

class SubmissionAdapter(
    private val list: List<SubmissionData>
) : RecyclerView.Adapter<SubmissionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvStudentName: TextView =
            view.findViewById(R.id.tvStudentName)

        val btnOpen: Button =
            view.findViewById(R.id.btnOpen)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_submission_teacher,
                parent,
                false
            )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val item = list[position]

        // Nama siswa
        holder.tvStudentName.text =
            item.student?.name ?: "Unknown Student"

        // Kalau sudah dinilai tampilkan score
        if (item.score != null) {

            holder.btnOpen.text =
                item.score.toString()

        } else {

            holder.btnOpen.text =
                "Nilai"
        }

        holder.btnOpen.setOnClickListener {

            val context = holder.itemView.context

            val intent = Intent(
                context,
                GradeSubmissionActivity::class.java
            )

            intent.putExtra(
                "submission_id",
                item.id
            )

            intent.putExtra(
                "student_name",
                item.student?.name ?: "Unknown Student"
            )

            intent.putExtra(
                "file",
                "http://192.168.100.6:8000/storage/${item.file}"
            )

            intent.putExtra(
                "score",
                item.score ?: -1
            )

            context.startActivity(intent)
        }
    }
}