package com.example.schoolreminderr.model

data class CreateMaterialRequest(
    val classroom_id: Int,
    val title: String,
    val description: String
)