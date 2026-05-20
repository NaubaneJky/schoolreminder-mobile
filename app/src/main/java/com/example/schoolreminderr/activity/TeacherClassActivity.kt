package com.example.schoolreminderr.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.adapter.TeacherClassAdapter
import com.example.schoolreminderr.model.TeacherClass

class TeacherClassActivity : AppCompatActivity() {

    private lateinit var rvClass: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_teacher_class)

        rvClass = findViewById(R.id.rvTeacherClass)

        val list = listOf(

            TeacherClass(
                1,
                "Matematika",
                "XI PPLG 3 • 36 siswa"
            ),

            TeacherClass(
                2,
                "Matematika",
                "XI PPLG 2 • 35 siswa"
            ),

            TeacherClass(
                3,
                "Matematika",
                "XI PPLG 1 • 36 siswa"
            )
        )

        rvClass.layoutManager =
            LinearLayoutManager(this)

        rvClass.adapter =
            TeacherClassAdapter(list)
    }
}