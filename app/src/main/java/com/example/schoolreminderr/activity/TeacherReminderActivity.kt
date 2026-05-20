package com.example.schoolreminderr.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.adapter.TeacherReminderAdapter
import com.example.schoolreminderr.model.TeacherReminder

class TeacherReminderActivity : AppCompatActivity() {

    private lateinit var rvReminder: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_teacher_reminder)

        rvReminder = findViewById(R.id.rvReminderTeacher)

        val list = listOf(
            TeacherReminder(
                "Matematika Bab 5",
                "XI PPLG 3 • 30 dari 36 siswa sudah mengumpulkan",
                "H-1",
                "#FF5C5C"
            ),
            TeacherReminder(
                "Matematika Bab 5",
                "XI PPLG 2 • 20 dari 35 siswa sudah mengumpulkan",
                "H-3",
                "#FF7A00"
            ),
            TeacherReminder(
                "Matematika Bab 5",
                "XI PPLG 1 • 12 dari 36 siswa sudah mengumpulkan",
                "H-7",
                "#5BB7F0"
            )
        )

        rvReminder.layoutManager = LinearLayoutManager(this)
        rvReminder.adapter = TeacherReminderAdapter(list)
    }
}