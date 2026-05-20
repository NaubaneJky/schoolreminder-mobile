package com.example.schoolreminderr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolreminderr.model.SubmissionModel

class SubmissionTeacherActivity : AppCompatActivity() {

    private lateinit var rvSubmission: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission_teacher)

        rvSubmission = findViewById(R.id.rvSubmission)

        val list = listOf(
            SubmissionModel("Adinda Octaviannisa R"),
            SubmissionModel("Alden Fatih Hanif"),
            SubmissionModel("Anas Akbar Lajuma"),
            SubmissionModel("Annisa Khurun’ain"),
            SubmissionModel("Aqila Dzaky"),
            SubmissionModel("Arkamudya Aceananda P"),
            SubmissionModel("Azka Naufal Aleeswa A"),
            SubmissionModel("Bisma Mahes"),
            SubmissionModel("Daffa Rahadyan D"),
            SubmissionModel("Danish Attalla"),
            SubmissionModel("Davina Alicia Fitriani"),
            SubmissionModel("Defan Maulana Asyar"),
            SubmissionModel("Faris Kahlil Haidar"),
            SubmissionModel("Kalingga Kusuma A")
        )

        rvSubmission.layoutManager = LinearLayoutManager(this)
        rvSubmission.adapter = SubmissionAdapter(list)
    }
}