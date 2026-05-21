package com.example.schoolreminderr.activity

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.R
import com.example.schoolreminderr.adapter.SubmissionAdapter
import com.example.schoolreminderr.model.SubmissionData
import com.example.schoolreminderr.model.SubmissionListResponse
import com.example.schoolreminderr.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmissionTeacherActivity : AppCompatActivity() {

    private lateinit var rvSubmission: RecyclerView
    private lateinit var tvTitle: TextView
    private lateinit var tvSubmission: TextView

    private var assignmentId: Int = 0
    private var submissionList = ArrayList<SubmissionData>()
    private lateinit var adapter: SubmissionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission_teacher)

        assignmentId = intent.getIntExtra("ASSIGNMENT_ID", 0)

        rvSubmission = findViewById(R.id.rvSubmission)
        tvTitle      = findViewById(R.id.tvTitle)
        tvSubmission = findViewById(R.id.tvSubmission)

        adapter = SubmissionAdapter(submissionList)
        rvSubmission.layoutManager = LinearLayoutManager(this)
        rvSubmission.adapter = adapter

        loadSubmissions()
    }

    private fun loadSubmissions() {
        RetrofitClient.getInstance(this).getSubmissions(assignmentId)
            .enqueue(object : Callback<SubmissionListResponse> {
                override fun onResponse(
                    call: Call<SubmissionListResponse>,
                    response: Response<SubmissionListResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()!!
                        tvTitle.text = result.assignment_title ?: "Tugas"
                        tvSubmission.text = "${result.total_submitted}/${result.total_students} submissions"
                        submissionList.clear()
                        submissionList.addAll(result.submissions)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@SubmissionTeacherActivity, "Gagal load submissions", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<SubmissionListResponse>, t: Throwable) {
                    Toast.makeText(this@SubmissionTeacherActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}