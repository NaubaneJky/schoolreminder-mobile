package com.example.schoolreminderr

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class JoinClassActivity : AppCompatActivity() {

    private lateinit var etNamaKelas: EditText
    private lateinit var etKodeKelas: EditText
    private lateinit var btnGabung: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_class)

        etNamaKelas = findViewById(R.id.etNamaKelas)
        etKodeKelas = findViewById(R.id.etKodeKelas)
        btnGabung = findViewById(R.id.btnGabung)

        btnGabung.setOnClickListener {

            val namaKelas = etNamaKelas.text.toString()
            val kodeKelas = etKodeKelas.text.toString()

            if (namaKelas.isEmpty() || kodeKelas.isEmpty()) {
                Toast.makeText(this, "Isi semua field!", Toast.LENGTH_SHORT).show()
            } else {

                // 🔥 nanti connect API disini
                Toast.makeText(
                    this,
                    "Berhasil gabung ke kelas $namaKelas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}