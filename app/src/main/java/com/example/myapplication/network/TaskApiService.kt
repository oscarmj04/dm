package com.example.myapplication.network

import com.example.myapplication.Task
import retrofit2.http.*

interface TaskApiService {

    // GET /tasks  → obtiene todas las tareas
    @GET("tasks")
    suspend fun getTasks(): List<Task>

    // POST /tasks → crea nueva tarea
    @POST("tasks")
    suspend fun addTask(@Body task: Task): Task

    // PUT /tasks/{id} → actualiza tarea existente
    @PUT("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: String,
        @Body task: Task
    )

    // DELETE /tasks/{id} → elimina una tarea
    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String)
}
