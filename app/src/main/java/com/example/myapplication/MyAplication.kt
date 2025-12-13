package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.network.RetrofitInstance
import com.example.myapplication.data.repository.TaskRepository

class MyApplication : Application() {

    // Repositorio accesible en toda la app
    lateinit var repository: TaskRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val db = AppDatabase.getDatabase(this)
        repository = TaskRepository(
            dao = db.taskDao(),
            api = RetrofitInstance.api
        )
    }
}
