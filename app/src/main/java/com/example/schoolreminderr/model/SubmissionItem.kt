package com.example.schoolreminderr.model

data class SubmissionItem(
    val id: Int,
    val assignment_id: Int,
    val student_id: Int,
    val file: String?,
    val grade: Int?,
    val student: StudentData?
)