package com.example.schoolreminderr.adapter

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.activity.StudentClassDetailActivity
import com.example.schoolreminderr.model.TeacherClass

class StudentClassAdapter(
    private val list: List<TeacherClass>
) : RecyclerView.Adapter<StudentClassAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvClass: TextView = view.findViewById(R.id.tvClassName)
        val tvSubject: TextView = view.findViewById(R.id.tvSubject)
        val tvTeacher: TextView = view.findViewById(R.id.tvTeacher)
        val viewColor: View = view.findViewById(R.id.viewColor)
        val btnOpen: Button = view.findViewById(R.id.btnOpen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // 1. Daftar warna pastel
        val colors = listOf("#FF7675", "#74B9FF", "#55E6C1", "#FAD390", "#A29BFE", "#FAB1A0")
        val colorIndex = position % colors.size
        val selectedColor = Color.parseColor(colors[colorIndex])

        // 2. Terapkan warna
        holder.viewColor.setBackgroundColor(selectedColor)
        holder.btnOpen.backgroundTintList = ColorStateList.valueOf(selectedColor)

        // 3. Set Data (Sesuaikan dengan properti di model TeacherClass)
        // Karena di Activity Anda mengisi 'name' dan 'info', maka:
        holder.tvClass.text = item.name
        holder.tvSubject.text = item.info // info berisi Subject + Code
        holder.tvTeacher.text = "Guru: -" // Karena TeacherClass tidak punya field guru

        // 4. Klik Navigasi
        // Di dalam onBindViewHolder StudentClassAdapter
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, StudentClassDetailActivity::class.java)

            // Pastikan key ini sama dengan yang ditangkap di StudentClassDetailActivity
            intent.putExtra("CLASS_ID", item.id)
            intent.putExtra("CLASS_NAME", item.name)
            // Jika model TeacherClass punya field lain, kirim juga:
            // intent.putExtra("SUBJECT", item.subject)

            context.startActivity(intent)
        }

        holder.btnOpen.setOnClickListener {
            holder.itemView.performClick()
        }
    }
}