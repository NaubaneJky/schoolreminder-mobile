package com.example.schoolreminderr.model

data class SubmissionData(
    val id: Int,
    val assignment_id: Int,
    val student_id: Int,
    val file: String?,
    val grade: Int?,
    val student: User?
)

data class SubmissionListResponse(
    val status: Boolean,
    val assignment_title: String?,
    val total_students: Int,
    val total_submitted: Int,
    val submissions: List<SubmissionData>
)