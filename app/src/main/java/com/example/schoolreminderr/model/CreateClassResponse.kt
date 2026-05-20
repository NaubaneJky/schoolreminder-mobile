package com.example.schoolreminderr.model

data class CreateClassResponse(
    val status: Boolean,
    val message: String,
    val data: ClassroomData?
)

