package com.example.dragonballapp.network

import okhttp3.OkHttpClient // <-- AÑADE ESTA IMPORTACIÓN
import okhttp3.logging.HttpLoggingInterceptor // <-- AÑADE ESTA IMPORTACIÓN
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit // Opcional, para timeouts

object RetrofitInstance {

    val api: ApiService by lazy {


        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)

            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()


        Retrofit.Builder()
            .baseUrl("https://dragonball-api.com/")
            .client(client) // <-- USA EL CLIENTE CON EL INTERCEPTOR
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}