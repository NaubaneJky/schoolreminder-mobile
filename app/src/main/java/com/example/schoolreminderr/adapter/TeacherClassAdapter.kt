package com.example.schoolreminderr.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.TeacherClass

class TeacherClassAdapter(
    private val list: List<TeacherClass>
) : RecyclerView.Adapter<TeacherClassAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvClass = view.findViewById<TextView>(R.id.tvClassName)
        val tvInfo = view.findViewById<TextView>(R.id.tvInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_teacher_class, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        holder.tvClass.text = item.name
        holder.tvInfo.text = item.info
    }
}