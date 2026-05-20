package com.example.schoolreminderr.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.adapter.StudentAdapter
import com.example.schoolreminderr.model.Student

class TeacherClassStudentActivity : AppCompatActivity() {

    private lateinit var rvStudent: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_class_student)

        rvStudent = findViewById(R.id.rvStudent)

        val list = listOf(
            Student("Adinda Octaviannisa Rosadi"),
            Student("Annisa Khurun’ain"),
            Student("Bisma Maheswara"),
            Student("Chantika Kusumawardani"),
            Student("Dafa Rahardian Darmawan"),
            Student("Ersamia Dyta Ramadhani"),
            Student("Faris Kahlil Haidar"),
            Student("Ghaisani Farras Husna"),
            Student("Hagia Sophia"),
            Student("Ian Sophian"),
            Student("Jeong Jaehyun"),
            Student("Kalingga Kusuma Arbi")
        )

        rvStudent.layoutManager = LinearLayoutManager(this)
        rvStudent.adapter = StudentAdapter(list)
    }
}