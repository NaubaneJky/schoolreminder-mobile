package com.example.schoolreminderr.model

data class SubmissionResponse(
    val status: Boolean,
    val assignment_title: String,
    val total_students: Int,
    val total_submitted: Int,
    val submissions: List<SubmissionData>
)