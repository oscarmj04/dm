package com.example.myapplication.data.network

import retrofit2.http.*


interface TaskApiService {

    @GET("tasks")
    suspend fun getTasks(): List<TaskDto>

    @POST("tasks")
    suspend fun addTask(@Body task: TaskDto): TaskDto

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body task: TaskDto)

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String)
}
