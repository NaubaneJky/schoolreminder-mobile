package com.example.schoolreminderr.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.adapter.ClassroomAdapter
import com.example.schoolreminderr.model.Classroom

class ClassStudentActivity : AppCompatActivity() {

    private lateinit var rvClass: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_student)

        rvClass = findViewById(R.id.rvClass)

        // 🔥 DUMMY DATA
        val list = listOf(
            Classroom("Matematika", "Pak Budi", "#B084CC"),
            Classroom("Bahasa Indonesia", "Bu Kalim", "#FF6B00"),
            Classroom("Informatika", "Pak Sunar", "#F06292"),
            Classroom("IPA", "Bu Iffah", "#81C784")
        )

        val adapter = ClassroomAdapter(list)

        rvClass.layoutManager = LinearLayoutManager(this)
        rvClass.adapter = adapter
    }
}