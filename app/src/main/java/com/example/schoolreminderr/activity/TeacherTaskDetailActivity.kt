package com.example.schoolreminderr.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.adapter.SubmissionAdapter
import com.example.schoolreminderr.model.SubmissionResponse
import com.example.schoolreminderr.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeacherTaskDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvProgress: TextView
    private lateinit var tvTitle: TextView

    private var assignmentId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_teacher_task_detail)

        recyclerView =
            findViewById(R.id.recyclerSubmission)

        tvProgress =
            findViewById(R.id.tvProgress)

        tvTitle =
            findViewById(R.id.tvTitle)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        assignmentId =
            intent.getIntExtra("assignment_id", 0)

        loadSubmission(assignmentId)
    }

    // AUTO REFRESH PAS BALIK DARI HALAMAN NILAI
    override fun onResume() {
        super.onResume()

        loadSubmission(assignmentId)
    }

    private fun loadSubmission(id: Int) {

        RetrofitClient
            .getInstance(this)
            .getSubmissions(id)
            .enqueue(object : Callback<SubmissionResponse> {

                override fun onResponse(
                    call: Call<SubmissionResponse>,
                    response: Response<SubmissionResponse>
                ) {

                    if (
                        response.isSuccessful &&
                        response.body()?.status == true
                    ) {

                        val data = response.body()!!

                        tvTitle.text =
                            data.assignment_title

                        tvProgress.text =
                            "${data.total_submitted} / ${data.total_students} siswa sudah mengumpulkan"

                        recyclerView.adapter =
                            SubmissionAdapter(
                                data.submissions
                            )

                    } else {

                        Toast.makeText(
                            this@TeacherTaskDetailActivity,
                            "Gagal load submission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<SubmissionResponse>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@TeacherTaskDetailActivity,
                        t.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}