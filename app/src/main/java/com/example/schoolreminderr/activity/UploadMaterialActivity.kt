package com.example.schoolreminderr.activity

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolreminderr.R

class UploadMaterialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_material)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnSaveMaterial).setOnClickListener {
            Toast.makeText(this, "Materi disimpan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}