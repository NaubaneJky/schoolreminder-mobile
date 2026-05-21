package com.example.schoolreminderr.model

data class CreateAssignmentRequest(
    val classroom_id: Int,
    val title: String,
    val description: String,
    val deadline: String
)