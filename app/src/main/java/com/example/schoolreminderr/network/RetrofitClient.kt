package com.example.schoolreminderr.network

import android.content.Context
import com.example.schoolreminderr.utils.SessionManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://192.168.100.6:8000/api/"

    fun getInstance(context: Context): ApiService {

        val sessionManager = SessionManager(context)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)

            .addInterceptor { chain ->

                val token = sessionManager.getToken()

                val requestBuilder = chain.request()
                    .newBuilder()
                    .addHeader(
                        "Accept",
                        "application/json"
                    )
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )

                // KIRIM TOKEN KE LARAVEL
                if (token != null) {

                    requestBuilder.addHeader(
                        "Authorization",
                        "Bearer $token"
                    )
                }

                chain.proceed(requestBuilder.build())
            }

            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create(gson)
            )
            .build()
            .create(ApiService::class.java)
    }
}