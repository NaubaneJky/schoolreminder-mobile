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
        val list = mutableListOf(

            Classroom(
                id = 1,
                name = "Matematika",
                teacher = "Pak Budi",
                color = "#B084CC"
            ),

            Classroom(
                id = 2,
                name = "Bahasa Indonesia",
                teacher = "Bu Kalim",
                color = "#FF6B00"
            ),

            Classroom(
                id = 3,
                name = "Informatika",
                teacher = "Pak Sunar",
                color = "#F06292"
            ),

            Classroom(
                id = 4,
                name = "IPA",
                teacher = "Bu Iffah",
                color = "#81C784"
            )
        )

        val adapter = ClassroomAdapter(list)

        rvClass.layoutManager = LinearLayoutManager(this)
        rvClass.adapter = adapter
    }
}