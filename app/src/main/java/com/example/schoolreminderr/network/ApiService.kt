package com.example.schoolreminderr.network

import com.example.schoolreminderr.model.LoginRequest
import com.example.schoolreminderr.model.LoginResponse
import com.example.schoolreminderr.model.RegisterRequest
import com.example.schoolreminderr.model.RegisterResponse
import com.example.schoolreminderr.model.CreateClassRequest
import com.example.schoolreminderr.model.CreateClassResponse
import com.example.schoolreminderr.model.ClassroomResponse
import com.example.schoolreminderr.model.ClassMembersResponse
import com.example.schoolreminderr.model.CreateAssignmentResponse
import com.example.schoolreminderr.model.BaseResponse
import com.example.schoolreminderr.model.SubmissionResponse
import com.example.schoolreminderr.model.CreateMaterialResponse
import com.example.schoolreminderr.model.ReminderResponse
import com.example.schoolreminderr.model.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("create-class")
    fun createClass(@Body request: CreateClassRequest): Call<CreateClassResponse>

    @GET("classes")
    fun getClasses(): Call<ClassroomResponse>

    @GET("class-members/{id}")
    fun getClassMembers(@Path("id") classId: Int): Call<ClassMembersResponse>

    @Multipart
    @POST("create-assignment")
    fun createAssignment(
        @Part("classroom_id") classroomId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("deadline") deadline: RequestBody,
        @Part file: MultipartBody.Part?
    ): Call<CreateAssignmentResponse>

    @GET("assignment/{id}/submissions")
    fun getSubmissions(@Path("id") assignmentId: Int): Call<SubmissionResponse>

    @Multipart
    @POST("create-material")
    fun createMaterial(
        @Part("classroom_id") classroomId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part?
    ): Call<CreateMaterialResponse>

    @GET("materials/{classroom}")
    fun getMaterials(@Path("classroom") classroomId: Int): Call<CreateMaterialResponse>

    @GET("reminders")
    fun getReminders(): Call<ReminderResponse>

    @GET("profile")
    fun getProfile(): Call<ProfileResponse>

    // Update profile pakai multipart untuk support foto
    @Multipart
    @POST("profile/update")
    fun updateProfile(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("gender") gender: RequestBody?,
        @Part("school") school: RequestBody?,
        @Part("phone_number") phone: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part photo: MultipartBody.Part?
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @POST("grade-submission")
    fun gradeSubmission(
        @Field("submission_id") submissionId: Int,
        @Field("score") score: Int
    ): Call<BaseResponse>
}
