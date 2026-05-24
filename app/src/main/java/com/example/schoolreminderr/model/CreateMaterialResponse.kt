package com.example.schoolreminderr.model

data class CreateMaterialResponse(

    val status: Boolean,
    val message: String? = null,
    val data: MaterialData? = null
)