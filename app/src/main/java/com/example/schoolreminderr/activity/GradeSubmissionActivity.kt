package com.example.schoolreminderr.activity

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.schoolreminderr.R
import com.example.schoolreminderr.model.BaseResponse
import com.example.schoolreminderr.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GradeSubmissionActivity : AppCompatActivity() {

    private lateinit var ivFile: ImageView
    private lateinit var webView: WebView
    private lateinit var etGrade: EditText
    private lateinit var btnSave: Button
    private lateinit var tvStudent: TextView

    private var submissionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grade_submission)

        ivFile = findViewById(R.id.ivFile)
        webView = findViewById(R.id.webView)
        etGrade = findViewById(R.id.etGrade)
        btnSave = findViewById(R.id.btnSave)
        tvStudent = findViewById(R.id.tvStudent)

        submissionId = intent.getIntExtra("submission_id", 0)

        val studentName =
            intent.getStringExtra("student_name")

        val fileUrl =
            intent.getStringExtra("file")

        val score =
            intent.getIntExtra("score", -1)

        tvStudent.text = studentName

        // Kalau sudah ada nilai tampilkan
        if (score != -1) {
            etGrade.setText(score.toString())
        }

        // Load file
        if (fileUrl != null) {

            // Kalau gambar
            if (
                fileUrl.endsWith(".jpg") ||
                fileUrl.endsWith(".jpeg") ||
                fileUrl.endsWith(".png")
            ) {

                ivFile.visibility = View.VISIBLE
                webView.visibility = View.GONE

                Glide.with(this)
                    .load(fileUrl)
                    .into(ivFile)

            } else {

                // PDF / DOC buka di webview
                ivFile.visibility = View.GONE
                webView.visibility = View.VISIBLE

                webView.settings.javaScriptEnabled = true
                webView.webViewClient = WebViewClient()

                webView.loadUrl(
                    "https://docs.google.com/gview?embedded=true&url=$fileUrl"
                )
            }
        }

        btnSave.setOnClickListener {

            val grade =
                etGrade.text.toString().trim()

            if (grade.isEmpty()) {

                Toast.makeText(
                    this,
                    "Masukkan nilai",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            saveGrade(grade.toInt())
        }
    }

    private fun saveGrade(grade: Int) {

        RetrofitClient
            .getInstance(this)
            .gradeSubmission(
                submissionId,
                grade
            )
            .enqueue(object : Callback<BaseResponse> {

                override fun onResponse(
                    call: Call<BaseResponse>,
                    response: Response<BaseResponse>
                ) {

                    if (
                        response.isSuccessful &&
                        response.body()?.status == true
                    ) {

                        Toast.makeText(
                            this@GradeSubmissionActivity,
                            "Nilai berhasil disimpan",
                            Toast.LENGTH_SHORT
                        ).show()

                        finish()

                    } else {

                        Toast.makeText(
                            this@GradeSubmissionActivity,
                            "Gagal simpan nilai",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@GradeSubmissionActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}