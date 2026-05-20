package com.example.schoolreminderr.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.adapter.ReminderTaskAdapter
import com.example.schoolreminderr.model.ReminderTask

class ReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        val rvTask = findViewById<RecyclerView>(R.id.rvTask)

        val list = listOf(
            ReminderTask("IPA", "Presentasi bab 5", "H-3", "Belum Selesai", "#FF6B00"),
            ReminderTask("Informatika", "Membuat kalkulator", "H-6", "Belum Selesai", "#56B4E9"),
            ReminderTask("Matematika", "Mengerjakan halaman 125", "H-8", "Belum Selesai", "#F06292"),
            ReminderTask("Fisika", "Hukum Newton II", "H-10", "Belum Selesai", "#F06292")
        )

        rvTask.layoutManager = LinearLayoutManager(this)
        rvTask.adapter = ReminderTaskAdapter(list)
    }
}