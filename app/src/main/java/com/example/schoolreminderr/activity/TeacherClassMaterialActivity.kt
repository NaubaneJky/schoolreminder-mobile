package com.example.schoolreminderr.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.adapter.MaterialTeacherAdapter
import com.example.schoolreminderr.model.MaterialTeacher

class TeacherClassMaterialActivity : AppCompatActivity() {

    private lateinit var rvMaterial: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_class_material)

        rvMaterial = findViewById(R.id.rvMaterial)

        val list = listOf(
            MaterialTeacher("Bab 5: Matriks"),
            MaterialTeacher("Bab 4: Integral"),
            MaterialTeacher("Bab 3: Trigonometri"),
            MaterialTeacher("Bab 2: Bentuk Akar"),
            MaterialTeacher("Bab 1: Eksponen")
        )

        rvMaterial.layoutManager = LinearLayoutManager(this)
        rvMaterial.adapter = MaterialTeacherAdapter(list)
    }
}