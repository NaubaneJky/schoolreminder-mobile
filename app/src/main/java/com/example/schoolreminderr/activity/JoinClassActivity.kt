package com.example.schoolreminderr.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.JoinClassResponse
import com.example.schoolreminderr.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinClassActivity : AppCompatActivity() {
    private lateinit var etKodeKelas: EditText
    private lateinit var btnGabung: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_class)

        etKodeKelas = findViewById(R.id.etKodeKelas)
        btnGabung = findViewById(R.id.btnGabung)
        progressBar = findViewById(R.id.progressBar)

        btnGabung.setOnClickListener {
            val kodeKelas = etKodeKelas.text.toString().trim().uppercase()

            if (kodeKelas.isEmpty()) {
                etKodeKelas.error = "Masukkan kode kelas"
                return@setOnClickListener
            }

            joinClassProcess(kodeKelas)
        }
    }

    private fun joinClassProcess(kode: String) {
        progressBar.visibility = View.VISIBLE
        btnGabung.isEnabled = false

        // Gunakan uppercase() agar sinkron dengan database Laravel
        RetrofitClient.getInstance(this).joinClass(kode.uppercase()).enqueue(object : Callback<JoinClassResponse> {
            override fun onResponse(call: Call<JoinClassResponse>, response: Response<JoinClassResponse>) {
                progressBar.visibility = View.GONE
                btnGabung.isEnabled = true

                if (response.isSuccessful && response.body()?.status == true) {
                    Toast.makeText(this@JoinClassActivity, "Berhasil Gabung!", Toast.LENGTH_SHORT).show()

                    // PENTING: Sinyalkan ke activity sebelumnya bahwa ada data baru
                    setResult(RESULT_OK)
                    finish()
                } else {
                    val errorMsg = response.body()?.message ?: "Kode Kelas Tidak Valid"
                    Toast.makeText(this@JoinClassActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JoinClassResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnGabung.isEnabled = true
                Toast.makeText(this@JoinClassActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}