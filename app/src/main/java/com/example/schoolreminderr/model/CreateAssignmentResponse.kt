package com.example.schoolreminderr.model

data class CreateAssignmentResponse(
    val status: Boolean,
    val message: String? = null,
    val data: AssignmentData? = null
)