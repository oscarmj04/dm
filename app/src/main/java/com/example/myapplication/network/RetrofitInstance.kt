package com.example.myapplication.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {


    private const val BASE_URL =
        "https://crudcrud.com/api/feff4e1a503d4381a61abf9a9d982580/"

    // Moshi con soporte para Kotlin
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // Retrofit configurado con Moshi
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    // Servicio que usarás en el ViewModel
    val api: TaskApiService = retrofit.create(TaskApiService::class.java)
}
