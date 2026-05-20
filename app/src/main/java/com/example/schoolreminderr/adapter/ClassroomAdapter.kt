package com.example.schoolreminderr.adapter

import com.example.schoolreminderr.model.Classroom
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.SubmissionModel

class ClassroomAdapter(
    private val list: List<Classroom>
) : RecyclerView.Adapter<ClassroomAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvClass = view.findViewById<TextView>(R.id.tvClassName)
        val tvTeacher = view.findViewById<TextView>(R.id.tvTeacher)
        val btnOpen = view.findViewById<Button>(R.id.btnOpen)
        val colorView = view.findViewById<View>(R.id.viewColor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_class, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        holder.tvClass.text = item.name
        holder.tvTeacher.text = item.teacher

        val color = Color.parseColor(item.color)

        holder.colorView.setBackgroundColor(color)

        holder.btnOpen.backgroundTintList =
            ColorStateList.valueOf(color)
    }
}