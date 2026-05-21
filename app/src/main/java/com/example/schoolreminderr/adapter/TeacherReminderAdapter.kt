package com.example.schoolreminderr.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.ReminderData

class TeacherReminderAdapter(
    private val list: List<ReminderData>
) : RecyclerView.Adapter<TeacherReminderAdapter.ViewHolder>() {

    inner class ViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        val tvTitle =
            view.findViewById<TextView>(
                R.id.tvAssignmentTitle
            )

        val tvInfo =
            view.findViewById<TextView>(
                R.id.tvClassInfo
            )

        val tvBadge =
            view.findViewById<TextView>(
                R.id.tvDeadlineBadge
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.item_reminder_teacher,
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

        holder.tvTitle.text = item.title

        holder.tvInfo.text =
            "${item.classroom} • ${item.submission}"

        holder.tvBadge.text = item.badge

        // WARNA BADGE
        when {

            item.badge.contains("H-0") -> {

                holder.tvBadge
                    .setBackgroundColor(
                        Color.parseColor("#FF3B30")
                    )
            }

            item.badge.contains("H-1") -> {

                holder.tvBadge
                    .setBackgroundColor(
                        Color.parseColor("#FF9500")
                    )
            }

            else -> {

                holder.tvBadge
                    .setBackgroundColor(
                        Color.parseColor("#5BB7F0")
                    )
            }
        }
    }
}