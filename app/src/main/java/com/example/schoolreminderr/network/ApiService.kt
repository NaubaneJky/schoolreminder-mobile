package com.example.schoolreminderr.network

import com.example.schoolreminderr.model.LoginRequest
import com.example.schoolreminderr.model.LoginResponse
import com.example.schoolreminderr.model.RegisterRequest
import com.example.schoolreminderr.model.RegisterResponse
import com.example.schoolreminderr.model.CreateClassRequest
import com.example.schoolreminderr.model.CreateClassResponse
import com.example.schoolreminderr.model.ClassroomResponse

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST("register")
    fun register(
        @Body request: RegisterRequest
    ): Call<RegisterResponse>


    @POST("create-class")
    fun createClass(
        @Body request: CreateClassRequest
    ): Call<CreateClassResponse>

    @GET("classes")
    fun getClasses(): Call<ClassroomResponse>
}